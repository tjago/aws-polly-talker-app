package eu.tjago.apps.pollytalker.controller;

import com.amazonaws.services.polly.model.OutputFormat;
import com.amazonaws.services.polly.model.Voice;
import eu.tjago.apps.pollytalker.PollyTalkerApp;
import eu.tjago.apps.pollytalker.api.AwsClientSingleton;
import eu.tjago.apps.pollytalker.util.Constants;
import eu.tjago.apps.pollytalker.util.FileHelper;
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
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

/**
 * Created by tjago
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
    public void initialize() {
        this.voicesList = AwsClientSingleton.getInstance().getAllVoicesList();
        if (!this.voicesList.isEmpty()) {
            setLanguageCombobox();
            setVoiceCombobox();
        }
    }

    @FXML
    private void onChangeLanguageCombobox(ActionEvent event) {
        setVoiceCombobox();
    }

    /**
     * Communicate with Amazon Cloud Voice service to obtain sound file
     *
     * @param event
     */
    @FXML
    private void btnReadTextPressed(ActionEvent event) throws InterruptedException {
        String voiceName = voicesComboBox.getSelectionModel().getSelectedItem();
        Optional<Voice> voice = AwsClientSingleton.getInstance().getVoiceByName(voiceName);
        Path filepath = FileHelper.generateUniqueRecordingFilename();
        if (threadedVoicePlayer != null) {
            threadedVoicePlayer.close();
        }
        try {
            Optional<InputStream> speechStream = Optional.ofNullable(AwsClientSingleton.synthesize(
                    textArea.getText(),
                    OutputFormat.Mp3,
                    voice.get())
            );
            speechStream.ifPresent((InputStream is) -> {
                ContentAreaController.doSaveFile(is, filepath);
                AwsClientSingleton.setLastSavedFileLoc(filepath);
            });
        } catch (IOException e) {
            System.out.println("Exception Error: " + e.getMessage());
        }

        Media audioMedia = new Media(filepath.toUri().toString());
        new ThreadedVoicePlayer(new MediaPlayer(audioMedia)).run();
    }

    /**
     * Helper method to avoid catching exception in lambda function
     *
     * @param stream
     * @param filepath
     */

    private static void doSaveFile(InputStream stream, Path filepath) {
        try {
            java.nio.file.Files.copy(stream, filepath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.out.println("Error occured while saving the file: " + e.getLocalizedMessage());
        }
    }

    /**
     * Opens a FileChooser window to let save MP3 file,
     * it actually just copies the previously saved file to a new, user defined location.
     */
    @FXML
    private void handleSave() {
        if (AwsClientSingleton.getLastSavedFileLoc().isPresent()) {
            appInstance.saveVoiceFileToUserSpecifiedLocation(AwsClientSingleton.getLastSavedFileLoc().get());
        } else {
            //TODO display pop-up to play file first
        }
    }

    /**
     * Method called after choosing Language
     */
    private void setVoiceCombobox() {

        String pickedLanguageName = languageComboBox.getSelectionModel().getSelectedItem().toString();
        ObservableList<String> voicesObsList = FXCollections.observableArrayList();

        for (Voice voice : voicesList) {
            if (voice.getLanguageName().equals(pickedLanguageName)) {
                voicesObsList.add(voice.getName());
            }
        }

        voicesComboBox.setItems(voicesObsList);
        voicesComboBox.getSelectionModel().selectFirst();
    }

    private void setLanguageCombobox() {
        ObservableSet<String> languagesSet = FXCollections.observableSet();

        voicesList.stream()
                .forEach(item -> languagesSet.add(item.getLanguageName()));

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

    public void setMainApp(PollyTalkerApp pollyTalkerApp) {
        this.appInstance = pollyTalkerApp;
    }


    @FXML
    private void stopReading(ActionEvent actionEvent) {
        threadedVoicePlayer.halt();
    }
}
