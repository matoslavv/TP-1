package bulkupload.tp1;

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

        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        Scene scene = new Scene(root, 600, 400);
        stage.setTitle("Welcome");

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

    public void changeScene(String fxml) throws IOException{
        Parent pane = FXMLLoader.load(getClass().getResource(fxml));
        stg.getScene().setRoot(pane);
    }

    public static void main(String[] args) {
        launch();
    }
}