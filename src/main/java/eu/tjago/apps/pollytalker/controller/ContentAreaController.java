package eu.tjago.apps.pollytalker.controller;

import com.amazonaws.SdkClientException;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by tjago
 */
public class ContentAreaController {

    private PollyTalkerApp appInstance;
    private List<Voice> voicesList;
    private Optional<ThreadedVoicePlayer> threadedVoicePlayer;

    @FXML private TextArea textArea;
    @FXML private ComboBox<String> languageComboBox;
    @FXML private ComboBox<String> voicesComboBox;
    @FXML private Button saveButton;
    @FXML private Button readText;
    @FXML private Button stopRead;

    @FXML
    public void initialize() {
        try {
            this.voicesList = AwsClientSingleton.getInstance().getAllVoicesList();
            textArea.setText("Hello human, type something for me to read.");
            enableInterface();
        } catch (SdkClientException ex) {
            this.voicesList = Collections.emptyList();
            textArea.setText("AWS credentials are incorrect, please set them in settings.");
            disableInterface();
        }
    }

    private void disableInterface() {
        languageComboBox.setDisable(true);
        voicesComboBox.setDisable(true);
        saveButton.setDisable(true);
        readText.setDisable(true);
        stopRead.setDisable(true);
        textArea.setEditable(false);
    }

    private void enableInterface() {
        languageComboBox.setDisable(false);
        voicesComboBox.setDisable(false);
        saveButton.setDisable(false);
        readText.setDisable(false);
        stopRead.setDisable(false);
        textArea.setEditable(true);

        setLanguageCombobox();
        setVoiceCombobox();
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
        this.threadedVoicePlayer = Optional.of(new ThreadedVoicePlayer(new MediaPlayer(audioMedia)));
        this.threadedVoicePlayer.ifPresent(ThreadedVoicePlayer::run);
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
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Could not save sound to a File:\n");
            alert.setContentText("Make sure you press read button first, to synthesize the text");

            alert.showAndWait();
        }
    }

    /**
     * Method called after choosing Language.
     * Based on selected Language, voices gonna get filtered from voice list.
     *
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
        if (languageComboBox.getItems().contains(Constants.PREFERRED_LANGUAGE)) {
            languageComboBox.getSelectionModel().select(Constants.PREFERRED_LANGUAGE);
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
        threadedVoicePlayer.ifPresent(ThreadedVoicePlayer::halt);
    }
}
