package eu.tjago.apps.pollytalker.controller;

import eu.tjago.apps.pollytalker.PollyTalkerApp;
import javafx.fxml.FXML;
import javafx.stage.Stage;


/**
 * Created by tjago
 */

public class AboutModalController {

    private PollyTalkerApp app;
    private Stage dialogStage;

    public void setMainApp(PollyTalkerApp talkerApp) {
        this.app = talkerApp;
    }

    @FXML
    private void onClose() {
        dialogStage.close();
    }

    public void setStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
}
