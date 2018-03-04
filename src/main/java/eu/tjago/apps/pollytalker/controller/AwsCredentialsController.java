package eu.tjago.apps.pollytalker.controller;

import com.amazonaws.SdkClientException;
import eu.tjago.apps.pollytalker.PollyTalkerApp;
import eu.tjago.apps.pollytalker.api.AwsClientSingleton;
import eu.tjago.apps.pollytalker.model.PollyCredentials;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


/**
 * Created by tjago
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

    /**
     * Save credentials action
     *
     * @param event
     */
    @FXML
    private void onSave(ActionEvent event) {

        if (accessKey.getLength() > 0 && secretKey.getLength() > 0) {
            app.setCredentials(
                    new PollyCredentials(accessKey.getText(), secretKey.getText())
            );
            app.saveCredentials();
            dialogStage.close();
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
        if (accessKey.toString().isEmpty() ||  secretKey.toString().isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Missing credentials");
            alert.setHeaderText("WARNING");
            alert.setContentText("Missing access key or secret key - please fill the fields.");
            return;
        }
        try {
            app.setCredentials(
                    new PollyCredentials(accessKey.getText(), secretKey.getText())
            );
            // If we can get voices list from AWS client we are all good, otherwise exception is thrown
            AwsClientSingleton.getInstance().getAllVoicesList();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Success Save");
            alert.setHeaderText("INFO");
            alert.setContentText("Your credentials are valid");

            alert.showAndWait();
        } catch (SdkClientException e) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error - Could not Authenticate");
            alert.setHeaderText("Warning");
            alert.setContentText("Credentials are incorrect");

            alert.showAndWait();
        }
    }

    @FXML
    private void onClose() {
        dialogStage.close();
    }

    public void setStage(Stage dialogStage) {
        this.dialogStage = dialogStage;

        if (app.getCredentials().areCredentialsInitialized()) {
            this.accessKey.setText(app.getCredentials().getAWSAccessKeyId());
            this.secretKey.setText(app.getCredentials().getAWSSecretKey());
        }
    }
}
