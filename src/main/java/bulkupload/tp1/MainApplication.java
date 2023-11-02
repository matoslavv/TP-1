package bulkupload.tp1;

import bulkupload.tp1.common.AppToken;
import bulkupload.tp1.db.DbContext;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MainApplication extends Application {


    private static Stage stg;

    @Override
    public void start(Stage stage) throws IOException, SQLException {
        stg = stage;
        stage.setResizable(false);

        // Initialize the AppToken
        AppToken appToken = new AppToken();

        // Load the login scene
        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("login.fxml"));
        Parent loginRoot = loginLoader.load();
        LoginController loginController = loginLoader.getController();
        loginController.setAppToken(appToken);

        // Load the main scene
//        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
//        Parent mainRoot = mainLoader.load();
//        MainController mainController = mainLoader.getController();
//        mainController.setAppToken(appToken);

        Scene scene = new Scene(loginRoot, 600, 400);
        stage.setTitle("Welcome");

//        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
//        Scene scene = new Scene(root, 600, 400);
//        stage.setTitle("Welcome");

//        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("hello-view.fxml"));
//        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
//        stage.setTitle("Crypto postcards");
//
//        Connection conn = DbContext.create();
//        Statement stmt = conn.createStatement();
//        ResultSet res = stmt.executeQuery("SELECT * FROM location");
//
//        MainController controller = fxmlLoader.getController();
//        controller.setStage(stage);

        stage.setScene(scene);
        stage.show();
    }

    public void changeScene(String fxml, AppToken appToken) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
        Parent pane = loader.load();
        stg.getScene().setRoot(pane);

        // Get the controller for the new scene
        if (loader.getController() instanceof MainController) {
            MainController mainController = loader.getController();
            mainController.setAppToken(appToken);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}