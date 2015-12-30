package eu.tjago.apps.ivonatalker.controller;

import com.ivona.services.tts.model.Voice;
import eu.tjago.apps.ivonatalker.IvonaTalkerApp;
import eu.tjago.apps.ivonatalker.api.CreateSpeech;
import eu.tjago.apps.ivonatalker.api.ListVoicesService;
import eu.tjago.apps.ivonatalker.util.Constants;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.nio.file.Paths;
import java.util.List;

/**
 * Created by Tomasz on 2015-12-29.
 */
public class ContentAreaController {

    public static final String TEMP_MP3_FILE = "/tmp/speech.mp3";
    private IvonaTalkerApp appInstance;
    private List<Voice> voicesList;
    private static MediaPlayer audioMediaPlayer;

    @FXML
    private TextArea textArea;

    @FXML
    private ComboBox<String> languageComboBox;

    @FXML
    private ComboBox<String> voicesComboBox;

    @FXML
    private void initialize() {
        ListVoicesService listVoicesService = new ListVoicesService();
        this.voicesList = listVoicesService.getAllVoicesList();

        setLanguageCombobox();
        setVoiceCombobox();
        setLanguageCombobox();
    }

    @FXML
    private void onChangeLanguageCombobox(ActionEvent event) {
        setVoiceCombobox();
    }

    @FXML
    private void btnReadTextPressed(ActionEvent event) {
        String voice = voicesComboBox.getSelectionModel().getSelectedItem();

        //save audio mp3 file to disk
        new CreateSpeech(voice, textArea.getText());
//        playSoundFile(TEMP_MP3_FILE);
        playAudioFile(TEMP_MP3_FILE);
    }

    private void playAudioFile(String file) {
        Media audioMedia = new Media(Paths.get(file).toUri().toString());
        audioMediaPlayer = new MediaPlayer(audioMedia);
        audioMediaPlayer.play();
    }

    private void playSoundFile(String file) {

        AudioClip speech = new AudioClip(Paths.get(file).toUri().toString());
        speech.play();
    }

    private void setVoiceCombobox() {

        String pickedLanguage = languageComboBox.getSelectionModel().getSelectedItem();
        ObservableList<String> voicesObsList = FXCollections.observableArrayList();

        for (Voice voice : voicesList) {
            if (voice.getLanguage() == pickedLanguage) {
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


    public void setMainApp(IvonaTalkerApp ivonaTalkerApp) {
        this.appInstance = ivonaTalkerApp;
    }


}
