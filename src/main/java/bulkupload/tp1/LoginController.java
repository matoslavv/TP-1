package bulkupload.tp1;

import bulkupload.tp1.common.AppToken;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    private AppToken appToken; // Property to store the API response

    public AppToken getApiResponse() {
        return appToken;
    }

    public void setAppToken(AppToken appToken) {
        this.appToken = appToken;
    }

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private Button button;

    @FXML
    private Label wrongLogin;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize any actions or logic when the FXML components are loaded.
    }

    public void userLogin(ActionEvent event) throws IOException {
        checkLogin();
    }

    public void checkLogin() {
        String apiUrl = "https://www.test.hcportal.eu/api/rest/login.php";
//        String requestBody = "{\"username\": \"" + username.getText() + "\", \"password\": \"" + password.getText() + "\"}";
        String requestBody = "{\"username\": \"" + "student" + "\", \"password\": \"" + "38f97150663a0324ce8ee81d3e0d218059229dae06d717e2acfefebfdd3b24a5" + "\"}";

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = requestBody.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    String responseJson = response.toString();
                    System.out.println("API Response: " + responseJson);


                    if (!responseJson.contains("invalid")) {
                        wrongLogin.setText("Success!");
                        appToken.setApiResponse(responseJson);
                        MainApplication m = new MainApplication();
                        m.changeScene("hello-view.fxml",appToken);
                    } else {
                        wrongLogin.setText("Invalid username or password");
                    }
                }
            } else {
                System.out.println("HTTP Response Code: " + responseCode);
                wrongLogin.setText("Wrong username or password");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to connect to the API");
            wrongLogin.setText("Failed to connect to the API");
        }
    }

}
