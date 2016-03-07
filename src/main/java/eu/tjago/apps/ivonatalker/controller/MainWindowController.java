package eu.tjago.apps.ivonatalker.controller;

import eu.tjago.apps.ivonatalker.IvonaTalkerApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

/**
 * Created by Tomasz on 2015-12-28.
 */
public class MainWindowController {

    private IvonaTalkerApp app;

    public void setMainApp(IvonaTalkerApp app) {
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
        app.saveVoiceToFile();
    }
}
