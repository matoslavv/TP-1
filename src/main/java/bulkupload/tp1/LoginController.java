package bulkupload.tp1;

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
        String requestBody = "{\"username\": \"" + username.getText() + "\", \"password\": \"" + password.getText() + "\"}";

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

                    // You should parse the JSON response to get the token
                    // String token = /* Parse the response JSON to get the token */;
                    // Save the token to use it in your application

                    if (!responseJson.contains("invalid")) {
                        // You should parse the JSON response to get the token
                        // String token = /* Parse the response JSON to get the token */;
                        // Save the token to use it in your application

                        wrongLogin.setText("Success!");
                        MainApplication m = new MainApplication();
                        m.changeScene("hello-view.fxml");
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
