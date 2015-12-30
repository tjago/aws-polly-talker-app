package eu.tjago.apps.ivonatalker;

import eu.tjago.apps.ivonatalker.api.CreateSpeech;
import eu.tjago.apps.ivonatalker.api.ListVoicesService;
import eu.tjago.apps.ivonatalker.controller.ContentAreaController;
import eu.tjago.apps.ivonatalker.controller.MainWindowController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Tomasz on 2015-12-24.
 */
public class IvonaTalkerApp extends Application {

    private Stage primaryStage;
    private BorderPane mainWindowLayout;

    public static void main(String[] args) {
        launch(args);

//        WorkingIvonaApiDemo();
    }

    private static void WorkingIvonaApiDemo() {
        System.out.println("Hello, I'm Ivona, we will have a talk, when you plug the API");
        System.out.println("Listing voices:");
        new ListVoicesService();
        new CreateSpeech();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Ivona Talker Application");
        this.primaryStage.setResizable(false);

        initMainWindow();
        setCenterContent();
    }

    /**
     * Initializes the root layout.
     */
    public void initMainWindow() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(IvonaTalkerApp.class.getResource("/view/MainWindow.fxml"));
            mainWindowLayout = (BorderPane) loader.load();

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

    public void setCenterContent() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(IvonaTalkerApp.class.getResource("/view/ContentArea.fxml"));
            AnchorPane centerContent = (AnchorPane) loader.load();

            // Set person overview into the center of root layout.
            mainWindowLayout.setCenter(centerContent);

            // Give the controller access to the main app.
            ContentAreaController controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
