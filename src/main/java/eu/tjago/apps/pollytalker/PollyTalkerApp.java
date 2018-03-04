package eu.tjago.apps.pollytalker;

import com.amazonaws.regions.Region;
import eu.tjago.apps.pollytalker.api.AwsClientSingleton;
import eu.tjago.apps.pollytalker.controller.AboutModalController;
import eu.tjago.apps.pollytalker.controller.AwsCredentialsController;
import eu.tjago.apps.pollytalker.controller.ContentAreaController;
import eu.tjago.apps.pollytalker.controller.MainWindowController;
import eu.tjago.apps.pollytalker.model.PollyCredentials;
import eu.tjago.apps.pollytalker.util.Constants;
import eu.tjago.apps.pollytalker.util.FileHelper;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

/**
 * Created by tjago
 */
public class PollyTalkerApp extends Application {

    private Stage primaryStage;
    private BorderPane mainWindowLayout;
    private PollyCredentials credentials;
    private ContentAreaController interfaceController;

    public static void main(String[] args) {
        System.out.println("working dir: " + System.getProperty("user.dir"));
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        loadCredentials();
        initMainWindow(primaryStage);
        initCenterInterface();
    }

    /**
     * Exit program
     */
    @Override
    public void stop() {
        Platform.exit();
    }

    /**
     * Initializes the root layout.
     */
    private void initMainWindow(Stage primaryStage) {

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Polly Talker app");
        this.primaryStage.setResizable(false);

        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(PollyTalkerApp.class.getResource("/view/MainWindow.fxml"));
            mainWindowLayout = loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(mainWindowLayout);
            primaryStage.setScene(scene);

            // Give the controller access to the main app.
            MainWindowController controller = loader.getController();
            controller.setMainApp(this);

            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setCredentials(PollyCredentials credentials) {
        this.credentials = credentials;
        credentials.setSystemProperties();
        AwsClientSingleton.getInstance().initAwsPollyClient(Region.getRegion(Constants.DEFAULT_AWS_REGION));
        interfaceController.initialize();

    }

    /**
     * Saves credentials and re-initializes AWS Client
     */
    public void saveCredentials() {
        saveCredentialsToFile(credentials, new File(Constants.CREDENTIALS_FILENAME));
        AwsClientSingleton.getInstance().initAwsPollyClient(Region.getRegion(Constants.DEFAULT_AWS_REGION));
        interfaceController.initialize();
    }

    public PollyCredentials getCredentials() {
        return credentials;
    }

    private void initCenterInterface() {
        try {
            // Load Content
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(PollyTalkerApp.class.getResource("/view/ContentArea.fxml"));
            AnchorPane centerContent = loader.load();

            mainWindowLayout.setCenter(centerContent);

            // Give the controller access to the main app.
            ContentAreaController controller = interfaceController = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Credentials dialog init
     */
    public void showCredentialsDialog() {

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(PollyTalkerApp.class.getResource("/view/CredentialsModal.fxml"));
            AnchorPane credentialsDialog = loader.load();

            // Create Credentials dialog
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Credentials");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);

            Scene scene = new Scene(credentialsDialog);
            dialogStage.setScene(scene);
            dialogStage.show();

            AwsCredentialsController controller = loader.getController();
            controller.setMainApp(this);
            controller.setStage(dialogStage);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * About dialog init
     */
    public void showAboutDialog() {

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(PollyTalkerApp.class.getResource("/view/AboutModal.fxml"));
            AnchorPane aboutDialog = loader.load();

            // Create Credentials dialog
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Polly Talker app info");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);

            AboutModalController controller = loader.getController();
            controller.setMainApp(this);
            controller.setStage(dialogStage);

            Scene scene = new Scene(aboutDialog);
            dialogStage.setScene(scene);
            dialogStage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load AWS Polly Api Credentials from XML file
     */
    private void loadCredentials() {
        this.credentials = FileHelper.loadCredentialsFromFile(Constants.CREDENTIALS_FILENAME);
    }

    /**
     * Saves AWS Polly credentials to a XML file
     *
     * @param credentials user credentials
     * @param file specified location of file to be saved
     */
    private void saveCredentialsToFile(PollyCredentials credentials, File file) {

        boolean fileSavedSuccessfully = FileHelper.saveCredentialsToFile(credentials, file);

        if (!fileSavedSuccessfully) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not save data");
            alert.setContentText("Could not save data to file:\n" + Constants.CREDENTIALS_FILENAME);

            alert.showAndWait();
        }
    }

    public void saveVoiceFileToUserSpecifiedLocation(Path location) {
        FileChooser fileChooser = new FileChooser();

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("MP3 files (*.mp3)", "*.mp3");
        fileChooser.getExtensionFilters().add(extFilter);

        //set init Dir upon opening Dialog
        Optional<File> initialPath = Optional.ofNullable(FileHelper.getLastSavedLocation());

        initialPath.ifPresent(fileChooser::setInitialDirectory);

        // Show save file dialog
        File file = fileChooser.showSaveDialog(this.primaryStage);

        if (file != null) {
            // Make sure it has the correct extension
            if (!file.getPath().endsWith(".mp3")) {
                file = new File(file.getPath() + ".mp3");
            }
            try {
                Files.copy(location,
                        file.toPath(),
                        StandardCopyOption.REPLACE_EXISTING);
                FileHelper.setLastSavedLocation(file);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
