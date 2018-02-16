package eu.tjago.apps.pollytalker.util;

import eu.tjago.apps.pollytalker.PollyTalkerApp;
import eu.tjago.apps.pollytalker.model.PollyCredentials;
import javafx.scene.control.Alert;
import org.joda.time.LocalDate;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.Locale;
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

        } catch (Exception e) {
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
     * @return file
     */
    public static File getLastSavedLocation() { //TODO: convert to Optional
        Preferences prefs = Preferences.userNodeForPackage(PollyTalkerApp.class);
        String filePath = prefs.get("filePath", null);
        if (filePath != null) {
            return new File(filePath).getParentFile();
        } else {
            return null;
        }
    }

    /**
     * generates a filename based on local date time
     * ex. recordings/rec-15_Feb-14_15_14.mp3
     * @return
     */
    public static String generateUniqueRecordingFilename() {
        LocalDateTime localDateTime = LocalDateTime.now();
        return  "recordings\\rec-"
                + localDateTime.getDayOfMonth()
                + "_" + localDateTime.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
                + "-" + localDateTime.getHour()
                + "_" + localDateTime.getMinute()
                + "_" + localDateTime.getSecond()
                + ".mp3";
    }

}
