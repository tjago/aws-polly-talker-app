package eu.tjago.apps.pollytalker.util;

import eu.tjago.apps.pollytalker.PollyTalkerApp;
import eu.tjago.apps.pollytalker.model.PollyCredentials;
import javafx.scene.control.Alert;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileReader;
import java.util.prefs.Preferences;

/**
 * Created by Tomasz on 2016-02-18.
 */
public class FileHelper {

    /**
     * Load user and pass from xml file
     */
    public static PollyCredentials loadCredentialsFromFile(String filename) {

        File file = new File(filename);
        try {
            JAXBContext context = JAXBContext.newInstance(CredentialsXmlWrapper.class);
            Unmarshaller um     = context.createUnmarshaller();

            FileReader fileReader = new FileReader(file);
            CredentialsXmlWrapper credentialsXmlWrapper = (CredentialsXmlWrapper) um.unmarshal(fileReader);

            return credentialsXmlWrapper.getCredentials();

        } catch (Exception e) { // catches ANY exception
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not load data from file:\n" + file.getPath());
            alert.setContentText("Error Message:\n" + e.getMessage());

            alert.showAndWait();
            return null;
        }
    }



    /**
     * Saves the file path to the registry.
     * @param file
     */
    public static void setLastSavedLocation(File file) {
        Preferences prefs = Preferences.userNodeForPackage(PollyTalkerApp.class);

        if (file != null) {
            prefs.put("filePath", file.getPath());
        }
    }

    /**
     * Returns file path from OS registry
     * The preference is read from the OS specific registry. If no such
     * preference can be found, null is returned.
     *
     * @return
     */
    public static File getLastSavedLocation() {
        Preferences prefs = Preferences.userNodeForPackage(PollyTalkerApp.class);
        String filePath = prefs.get("filePath", null);
        if (filePath != null) {
            return new File(filePath).getParentFile();
        } else {
            return null;
        }
    }

}
