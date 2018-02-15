package eu.tjago.apps.pollytalker.controller;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.polly.model.OutputFormat;
import com.amazonaws.services.polly.model.Voice;
import eu.tjago.apps.pollytalker.PollyTalkerApp;
import eu.tjago.apps.pollytalker.api.SpeechCloudSingleton;
import eu.tjago.apps.pollytalker.util.Constants;
import eu.tjago.apps.pollytalker.util.ThreadedVoicePlayer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

/**
 * Created by Tomasz on 2015-12-29.
 */
public class ContentAreaController {

    private PollyTalkerApp appInstance;
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
            this.voicesList = SpeechCloudSingleton.getInstance().getAllVoicesList();
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
     *
     * @param event
     */
    @FXML
    private void btnReadTextPressed(ActionEvent event) throws InterruptedException {
//        String voiceCode = voicesComboBox.getSelectionModel().getSelectedItem();
        Voice voice = SpeechCloudSingleton.getInstance().getAllVoicesList().get(0);
        if (threadedVoicePlayer != null) {
            threadedVoicePlayer.close();
        }
        try {
            Optional<InputStream> speechStream
                    = Optional.ofNullable(SpeechCloudSingleton.synthesize(textArea.getText(),
                    OutputFormat.Mp3,
                    voice
                    )
            );
            speechStream.ifPresent(ContentAreaController::doSaveFile);
        } catch (IOException e) {
            System.out.println("Exception Error: " + e.getMessage());
        }

        Media audioMedia = new Media(Paths.get(SpeechCloudSingleton.getTmpSpeechFilename())
                .toUri()
                .toString());

        this.threadedVoicePlayer = new ThreadedVoicePlayer(new MediaPlayer(audioMedia));

        threadedVoicePlayer.run();
    }

    static void doSaveFile(InputStream stream) {
        try {
            Path path = Paths.get(SpeechCloudSingleton.getTmpSpeechFilename());
            java.nio.file.Files.copy(stream, path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.out.println("Error occured while saving the file: " + e.getLocalizedMessage());
        }
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
            if (voice.getLanguageCode().equals(pickedLanguage)) {
                voicesObsList.add(voice.getName());
            }
        }

        voicesComboBox.setItems(voicesObsList);
        voicesComboBox.getSelectionModel().selectFirst();
    }

    private void setLanguageCombobox() {
        ObservableSet<String> languagesSet = FXCollections.observableSet();

        for (Voice voice : voicesList) {
            languagesSet.add(voice.getLanguageCode());
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

    public void setMainApp(PollyTalkerApp ivonaTalkerApp) {
        this.appInstance = ivonaTalkerApp;
    }


    @FXML
    private void stopReading(ActionEvent actionEvent) {
        threadedVoicePlayer.halt();
    }
}
