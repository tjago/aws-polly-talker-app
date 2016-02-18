package eu.tjago.apps.ivonatalker.controller;

import eu.tjago.apps.ivonatalker.IvonaTalkerApp;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;


/**
 * Created by Tomasz on 2016-01-03.
 */

public class AboutModalController {

    private IvonaTalkerApp app;
    private Stage dialogStage;

    @FXML
    private ImageView ivonaServiceLogo;


    public void setMainApp(IvonaTalkerApp talkerApp) {
        this.app = talkerApp;
    }

    @FXML
    private void initialize() {
//            File file = new File(AboutModalController.class.getResource("/img/ivona_tts_amzn.png").getFile());
//            Image image = new Image(file.toURI().toString());
//            Image image = new Image(AboutModalController.class.getResource("/img/ivona_tts_amzn.png").toString());
            Image image = new Image(AboutModalController.class.getResourceAsStream("/img/ivona_tts_amzn.png"));
//        Image image = new Image("/img/ivona_tts_amzn.png");
        ivonaServiceLogo = new ImageView(image);
    }

    @FXML
    private void onClose() {
        dialogStage.close();
    }

    public void setStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
}
