package eu.tjago.apps.pollytalker.controller;

import eu.tjago.apps.pollytalker.PollyTalkerApp;
import eu.tjago.apps.pollytalker.api.AwsClientSingleton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

/**
 * Created by tjago
 */
public class MainWindowController {

    private PollyTalkerApp app;

    public void setMainApp(PollyTalkerApp app) {
        this.app = app;
    }

    @FXML
    private void clickedCredentialsSttings (ActionEvent event) {
        app.showCredentialsDialog();
    }

    @FXML
    private void clickedCloseProgram(ActionEvent event) {
        try {
            app.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onAboutClicked(ActionEvent event) {
        app.showAboutDialog();
    }

    public void handleSave(ActionEvent actionEvent) {
        app.saveVoiceFileToUserSpecifiedLocation(AwsClientSingleton.getLastSavedFileLoc().get());
    }
}
