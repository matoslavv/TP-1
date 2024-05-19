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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

    public void userLogin(ActionEvent event) throws IOException, NoSuchAlgorithmException {
        checkLogin();
    }

    public String byteToHex(byte[] nums) {
        StringBuilder sb = new StringBuilder();
        for (byte num: nums
             ) {
            char[] hexDigits = new char[2];
            hexDigits[0] = Character.forDigit((num >> 4) & 0xF, 16);
            hexDigits[1] = Character.forDigit((num & 0xF), 16);
            sb.append(new String(hexDigits));
        }
        return sb.toString();
    }

    public void checkLogin() throws NoSuchAlgorithmException {
        String apiUrl = "https://www.test.hcportal.eu/api/rest/login.php";
//        String requestBody = "{\"username\": \"" + username.getText() + "\", \"password\": \"" + password.getText() + "\"}";
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedhash = digest.digest(password.getText().getBytes(StandardCharsets.UTF_8));
        String pass = byteToHex(encodedhash);

        String requestBody = "{\"username\": \"" + username.getText() + "\", \"password\": \"" + pass + "\"}";

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


                    if (!responseJson.contains("invalid") && !responseJson.isEmpty()) {
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
