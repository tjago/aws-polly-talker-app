package eu.tjago.apps.pollytalker.util;

import eu.tjago.apps.pollytalker.PollyTalkerApp;
import eu.tjago.apps.pollytalker.model.PollyCredentials;
import javafx.scene.control.Alert;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.prefs.Preferences;

/**
 * Created by tjago
 */
public class FileHelper {


    /**
     * Using JAXB from JDK saving the credentials to file in convenient xml format
     * @param credentials
     * @param file
     * @return
     */
    public static boolean saveCredentialsToFile(PollyCredentials credentials, File file) {
        try {
            JAXBContext context = JAXBContext.newInstance(PollyCredentials.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            m.marshal(new PollyCredentials(credentials.getAWSAccessKeyId(), credentials.getAWSSecretKey()), file);
            return true;

        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return false;
        }
    }
    /**
     * Load credentials from xml file
     */
    public static PollyCredentials loadCredentialsFromFile(String filename) {

        File file = new File(filename);
        try {
            JAXBContext context = JAXBContext.newInstance(PollyCredentials.class);
            Unmarshaller um     = context.createUnmarshaller();

            FileReader fileReader = new FileReader(file);
            PollyCredentials pollyCredentials = (PollyCredentials) um.unmarshal(fileReader);

            return pollyCredentials;

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not load credentials from a File:\n" + file.getPath());
            alert.setContentText("For first time use please go to Settings and set your AWS access key and secret"
            + " for Polly service. These can be obtained from your AWS account console.");

            alert.showAndWait();
            return null;
        }
    }



    /**
     * Saves the file path to the registry.
     * @param file last file location
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
    public static File getLastSavedLocation() {
        Preferences prefs = Preferences.userNodeForPackage(PollyTalkerApp.class);
        String filePath = prefs.get("filePath", null);
        if (filePath != null) {
            return new File(filePath).getParentFile();
        } else {
            return null;
        }
    }

    /**
     * generates a filepath based on local date time
     * ex. recordings/rec-15_Feb-14_15_14.mp3
     * @return generated filepath
     */
    public static Path generateUniqueRecordingFilename() {
        LocalDateTime localDateTime = LocalDateTime.now();
        return  Paths.get("recordings\\rec-"
                + localDateTime.getDayOfMonth()
                + "_" + localDateTime.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
                + "-" + localDateTime.getHour()
                + "_" + localDateTime.getMinute()
                + "_" + localDateTime.getSecond()
                + ".mp3");
    }
}
