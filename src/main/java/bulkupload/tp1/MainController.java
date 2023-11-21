package bulkupload.tp1;

import bulkupload.tp1.common.AppToken;
import bulkupload.tp1.common.CSV;
import bulkupload.tp1.data.Postcard;
import com.google.gson.GsonBuilder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Region;
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

    @FXML
    private TextArea detailsTextArea;

    private List<Postcard> postcards  = new ArrayList<>();;

    private Stage stage;

    private AppToken appToken;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setAppToken(AppToken appToken) {
        this.appToken = appToken;
    }

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    /**
     * Handle the upload when the button is clicked
     */
    @FXML
    protected void onUploadButtonClick() throws IOException {
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
//            postcards = csv.extractAll();
            postcards = csv.loadPostcardsFromCSV(appToken.getApiResponse());

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


        // Add a listener to handle key events in the itemList
        itemList.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP:
                case DOWN:
                    showSelectedItemJSON();
//                    showList();
                    break;
                default:
                    break;
            }
        });



        itemList.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                if (event.getClickCount() == 1) {
                    // Single-click detected
                    showSelectedItemJSON();
//                    showList();
                } else if (event.getClickCount() == 2) {
                    // Double-click detected
//                    showSelectedIteamDetails();
                    showModal();
                }
            }
        });
    }


    private void showSelectedItemJSON() {
        String selectedItem = itemList.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            // Assuming you have a method to retrieve Postcard based on name
            Postcard selectedPostcard = findPostcardByName(selectedItem);

            if (selectedPostcard != null) {
                // Use Gson to convert Postcard to JSON
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String json = gson.toJson(selectedPostcard);

                // Display the details in the detailsTextArea
                detailsTextArea.setText(json);
            }
        }
    }

    public void showSelectedIteamDetails(){
        itemList.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                // Double-click detected
                String selectedItem = itemList.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    // Find the corresponding Postcard by name
                    Postcard selectedPostcard = findPostcardByName(selectedItem);

                    if (selectedPostcard != null) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Postcard Details");
                        alert.setHeaderText(null);

                        // Set the content of the Alert with the Postcard details
                        StringBuilder details = new StringBuilder();
                        details.append("Token: ").append(selectedPostcard.getToken()).append("\n");
                        details.append("Name: ").append(selectedPostcard.getName()).append("\n");
                        details.append("Description: ").append(selectedPostcard.getDescription()).append("\n");
                        details.append("Image URL: ").append(selectedPostcard.getImageURL()).append("\n");
                        details.append("Day: ").append(selectedPostcard.getDay()).append("\n");
                        details.append("Month: ").append(selectedPostcard.getMonth()).append("\n");
                        details.append("Year: ").append(selectedPostcard.getYear()).append("\n");
                        details.append("Flag: ").append(selectedPostcard.getFlag()).append("\n");
                        details.append("Location: ").append(selectedPostcard.getLocation()).append("\n");
                        details.append("Language: ").append(selectedPostcard.getLanguage()).append("\n");
                        details.append("Solved: ").append(selectedPostcard.getSolved()).append("\n");
                        details.append("Availability: ").append(selectedPostcard.getAvailability()).append("\n");
                        details.append("Sender: ").append(selectedPostcard.getSender()).append("\n");
                        details.append("Recipient: ").append(selectedPostcard.getRecipient()).append("\n");

                        // Check for null values before joining
                        if (selectedPostcard.getCategory() != null) {
                            details.append("Category: ").append(selectedPostcard.getCategory().getName()).append("\n");
                        } else {
                            details.append("Category: N/A\n");
                        }

                        if (selectedPostcard.getTags() != null) {
                            details.append("Tags: ").append(String.join(", ", selectedPostcard.getTags())).append("\n");
                        } else {
                            details.append("Tags: N/A\n");
                        }

                        if (selectedPostcard.getGroups() != null) {
                            details.append("Groups: ").append(String.join(", ", selectedPostcard.getGroups())).append("\n");
                        } else {
                            details.append("Groups: N/A\n");
                        }

                        if (selectedPostcard.getData() != null) {
                            details.append("Data: ").append(selectedPostcard.getData()).append("\n");
                        } else {
                            details.append("Data: N/A\n");
                        }

                        alert.setContentText(details.toString());
                        alert.showAndWait();
                    }
                }
            }
        });
    }



    private void showModal() {
        String selectedItem = itemList.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            // Assuming you have a method to retrieve Postcard based on name
            Postcard selectedPostcard = findPostcardByName(selectedItem);

            if (selectedPostcard != null) {
                // Use Gson to convert Postcard to JSON
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String json = gson.toJson(selectedPostcard);

                // Create an Alert with a TextArea for displaying JSON content
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Postcard JSON");
                alert.setHeaderText(null);

                // Create a TextArea and set its content to the JSON text
                TextArea textArea = new TextArea(json);
                textArea.setEditable(false);
                textArea.setWrapText(true);

                // Set padding around the TextArea
                textArea.setStyle("-fx-padding: 10px;");

                // Set the content of the Alert to the TextArea
                alert.getDialogPane().setContent(textArea);

                // Increase the size of the Alert (optional)
                alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);

                // Show the Alert and wait for the user's response
                alert.showAndWait();
            }
        }
    }


    public void onUploadDBButtonClick(ActionEvent event) {
        CryptogramUploader uploader = new CryptogramUploader();
        uploader.uploadPostcards(postcards);
    }

}