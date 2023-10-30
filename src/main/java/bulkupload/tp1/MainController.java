package bulkupload.tp1;

import bulkupload.tp1.common.CSV;
import bulkupload.tp1.data.Postcard;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import com.google.gson.Gson;


import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MainController {
    @FXML
    private Label welcomeText;

    @FXML
    private ListView<String> itemList;

    private List<Postcard> postcards  = new ArrayList<>();;

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

//                List<Postcard> postcards = csv.extractAll();
            postcards = csv.extractAll();

            for (Postcard postcard : postcards) {
                try {
                    String json = new Gson().toJson(postcard);
                    System.out.println(json);
                } catch (Exception e) {
                    System.err.println("Error converting Postcard to JSON: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Show all postcards when the button is clicked
     */
    @FXML
    protected void onShowListButtonClick() {
        itemList.getItems().clear();

        // Counter for numbering
        int counter = 1;

        if (postcards.isEmpty()) {
            // Show an alert if the postcards list is empty
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Empty List");
            alert.setHeaderText(null);
            alert.setContentText("There are no postcards to display.");
            alert.showAndWait();
        } else {
            for (Postcard postcard : postcards) {
                try {
                    String name = postcard.getName();
//                    itemList.getItems().add(counter + ". " + name);
                    itemList.getItems().add(name);
                    counter++;
                } catch (Exception e) {
                    System.err.println("Error getting the name of the Postcard: " + e.getMessage());
                }
            }
        }
    }


    private Postcard findPostcardByName(String name) {
        for (Postcard postcard : postcards) {
            if (name.equals(postcard.getName())) {
                return postcard;
            }
        }
        return null; // Postcard not found
    }

    /**
     * Get the extension filter for the file chooser
     * @return FileChooser.ExtensionFilter
     */
    private FileChooser.ExtensionFilter getExtensionFilter() {
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
        return extFilter;
    }

    /**
     * Show the details of a selected Postcard in an Alert dialog.
     */
    public void initialize() {
        itemList.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                // Double-click detected
                String selectedItem = itemList.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    // Find the corresponding Postcard by name
                    Postcard selectedPostcard = findPostcardByName(selectedItem);

                    if (selectedPostcard != null) {
                        // Create an Alert to display the details
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Postcard Details");
                        alert.setHeaderText(null);

                        // Set the content of the Alert with the Postcard details
                        String details = "Token: " + selectedPostcard.getToken() + "\n" +
                                "Name: " + selectedPostcard.getName() + "\n" +
                                "Description: " + selectedPostcard.getDescription() + "\n" +
                                "Image URL: " + selectedPostcard.getImageURL() + "\n" +
                                "Day: " + selectedPostcard.getDay() + "\n" +
                                "Month: " + selectedPostcard.getMonth() + "\n" +
                                "Year: " + selectedPostcard.getYear() + "\n" +
                                "Flag: " + selectedPostcard.getFlag() + "\n" +
                                "Location: " + selectedPostcard.getLocation() + "\n" +
                                "Language: " + selectedPostcard.getLanguage() + "\n" +
                                "Solved: " + selectedPostcard.getSolved() + "\n" +
                                "Availability: " + selectedPostcard.getAvailability() + "\n" +
                                "Sender: " + selectedPostcard.getSender() + "\n" +
                                "Recipient: " + selectedPostcard.getRecipient() + "\n";
                        // Add more fields as needed

                        alert.setContentText(details);

                        // Show the Alert
                        alert.showAndWait();
                    }
                }
            }
        });
    }
}