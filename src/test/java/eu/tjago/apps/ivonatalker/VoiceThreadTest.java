package eu.tjago.apps.ivonatalker;

import eu.tjago.apps.ivonatalker.api.SpeechCloudSingleton;
import eu.tjago.apps.ivonatalker.util.ThreadedVoicePlayer;
import javafx.application.Application;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.junit.BeforeClass;
import org.junit.Test;

import java.nio.file.Paths;

import static org.junit.Assert.assertNotEquals;

/**
 * Created by tjago on 2016-03-07.
 */


public class VoiceThreadTest {

    @BeforeClass
    public static void setUpClass() throws InterruptedException {
        // Initialise Java FX

        System.out.printf("About to launch FX App\n");
        Thread t = new Thread("JavaFX Init Thread") {
            public void run() {
                Application.launch(IvonaTalkerApp.class, new String[0]);
            }
        };
        t.setDaemon(true);
        t.start();
        System.out.printf("FX App thread started\n");
        Thread.sleep(500);
    }

    @Test
    public void RunAndKillThread() throws InterruptedException {
        Media audioMedia = new Media(Paths.get(SpeechCloudSingleton.getTmpSpeechFilename())
                .toUri()
                .toString());


        ThreadedVoicePlayer vp = new ThreadedVoicePlayer(new MediaPlayer(audioMedia));

        vp.run();
        Thread.sleep(4000);
        vp.halt();

        assertNotEquals(vp, null);
    }
}
