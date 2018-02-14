package eu.tjago.apps.pollytalker.controller;

import com.amazonaws.AmazonClientException;
import eu.tjago.apps.pollytalker.PollyTalkerApp;
import eu.tjago.apps.pollytalker.api.IvonaCredentials;
import eu.tjago.apps.pollytalker.api.SpeechCloudSingleton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


/**
 * Created by Tomasz on 2016-01-03.
 */

public class AwsCredentialsController {

    private PollyTalkerApp app;
    private Stage dialogStage;

    @FXML
    private TextField accessKey;

    @FXML
    private PasswordField secretKey;


    public void setMainApp(PollyTalkerApp talkerApp) {
        this.app = talkerApp;
    }

    @FXML
    private void initialize() {
        //set img
    }

    /**
     * Save credentials to file
     *
     * @param event
     */
    @FXML
    private void onSave(ActionEvent event) {


        if (accessKey.getLength() > 0 && secretKey.getLength() > 0) {

//            System.setProperty(ACCESS_KEY_ENV_VAR, accessKey.getText());
//            System.setProperty(SECRET_KEY_ENV_VAR, secretKey.getText());

            app.setCredentials(
                    new IvonaCredentials(accessKey.getText(),
                            secretKey.getText())
            );

//            Checking if properties are saved
//            System.getProperties().list(System.out);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning - empty values");
            alert.setHeaderText("Warning");
            alert.setContentText("Please fill the credential form");

            alert.showAndWait();
        }
    }

    @FXML
    private void onTest(ActionEvent event) {
        try {
            onSave(event);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Success Save");
            alert.setHeaderText("INFO");
            alert.setContentText("Your credentials are maybe valid");

            alert.showAndWait();
        } catch (AmazonClientException e) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error - Could not Authenticate");
            alert.setHeaderText("INFO");
            alert.setContentText(e.getMessage());

            alert.showAndWait();

        }
    }

    @FXML
    private void onClose() {
        dialogStage.close();
    }

    public void setStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
}