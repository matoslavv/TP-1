package bulkupload.tp1;

import bulkupload.tp1.common.CSV;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;

public class MainController {
    @FXML
    private Label welcomeText;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    /**
     * Handle the upload when the button is clicked
     */
    @FXML
    protected void onUploadButtonClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
                CSV csv = new CSV(selectedFile.getAbsolutePath());

                csv.readAll();
                System.out.println();
                csv.readAllWithHeader();
                System.out.println();
                csv.read("name");
                csv.read(0);
        }
    }

    /**
     * Get the extension filter for the file chooser
     * @return FileChooser.ExtensionFilter
     */
    private FileChooser.ExtensionFilter getExtensionFilter() {
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
        return extFilter;
    }
}