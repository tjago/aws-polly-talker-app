package eu.tjago.apps.ivonatalker.controller;

import com.amazonaws.AmazonServiceException;
import com.ivona.services.tts.model.Voice;
import eu.tjago.apps.ivonatalker.IvonaTalkerApp;
import eu.tjago.apps.ivonatalker.api.SpeechCloudSingleton;
import eu.tjago.apps.ivonatalker.util.Constants;
import eu.tjago.apps.ivonatalker.util.ThreadedVoicePlayer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.nio.file.Paths;
import java.util.List;

/**
 * Created by Tomasz on 2015-12-29.
 */
public class ContentAreaController {

    private IvonaTalkerApp appInstance;
    private List<Voice> voicesList;
    private ThreadedVoicePlayer threadedVoicePlayer;

    @FXML
    private TextArea textArea;

    @FXML
    private ComboBox<String> languageComboBox;

    @FXML
    private ComboBox<String> voicesComboBox;

    @FXML
    private void initialize() {
        try {
            this.voicesList = SpeechCloudSingleton.getAllVoicesList();
            setLanguageCombobox();
            setVoiceCombobox();

        } catch (AmazonServiceException asEx) {
            System.out.println(" Warning: Wrong Credentials");
        }
    }

    @FXML
    private void onChangeLanguageCombobox(ActionEvent event) {
        setVoiceCombobox();
    }

    /**
     * Commounicate with Amazon Cloud Voice service to obtain sound file
     * @param event
     */
    @FXML
    private void btnReadTextPressed(ActionEvent event) throws InterruptedException {
        String voice = voicesComboBox.getSelectionModel().getSelectedItem();

        SpeechCloudSingleton.createSpeech(voice, textArea.getText());

        Media audioMedia = new Media(Paths.get(SpeechCloudSingleton.getTmpSpeechFilename())
                .toUri()
                .toString());

        this.threadedVoicePlayer = new ThreadedVoicePlayer(new MediaPlayer(audioMedia));

        threadedVoicePlayer.run();
    }

    /**
     * Opens a FileChooser to let save MP3 file
     */
    @FXML
    private void handleSave() {
        appInstance.saveVoiceToFile();
    }

    /**
     * Method called after choosing Language
     */
    private void setVoiceCombobox() {

        String pickedLanguage = languageComboBox.getSelectionModel().getSelectedItem().toString();
        ObservableList<String> voicesObsList = FXCollections.observableArrayList();

        for (Voice voice : voicesList) {
            if (voice.getLanguage().equals(pickedLanguage)) {
                voicesObsList.add(voice.getName());
            }
        }

        voicesComboBox.setItems(voicesObsList);
        voicesComboBox.getSelectionModel().selectFirst();
    }

    private void setLanguageCombobox() {
        ObservableSet<String> languagesSet = FXCollections.observableSet();

        for (Voice voice : voicesList) {
            languagesSet.add(voice.getLanguage());
        }

        languageComboBox.setItems(FXCollections.observableArrayList(languagesSet));
        if (languageComboBox.getItems().contains(Constants.PREFERED_LANGUAGE)) {
            languageComboBox.getSelectionModel().select(Constants.PREFERED_LANGUAGE);
        } else {
            languageComboBox.getSelectionModel().selectFirst();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    public void setMainApp(IvonaTalkerApp ivonaTalkerApp) {
        this.appInstance = ivonaTalkerApp;
    }


    @FXML
    private void stopReading(ActionEvent actionEvent) {
        threadedVoicePlayer.halt();
    }
}
