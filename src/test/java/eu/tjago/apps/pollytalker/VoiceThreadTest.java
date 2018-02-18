package eu.tjago.apps.pollytalker;

import javafx.application.Application;

/**
 * Created by tjago on 2016-03-07.
 */


public class VoiceThreadTest {

//    @BeforeClass
    public static void setUpClass() throws InterruptedException {
        // Initialise Java FX

        System.out.printf("About to launch FX App\n");
        Thread t = new Thread("JavaFX Init Thread") {
            public void run() {
                Application.launch(PollyTalkerApp.class, new String[0]);
            }
        };
        t.setDaemon(true);
        t.start();
        System.out.printf("FX App thread started\n");
        Thread.sleep(500);
    }

    //TODO refactor so it doesn't execute this test if .mp3 file is not found

//    @Test
    public void RunAndKillThread() throws InterruptedException {
//        Media audioMedia = new Media(Paths.get(AwsClientSingleton.getTmpSpeechFilename())
//                .toUri()
//                .toString());


//        ThreadedVoicePlayer vp = new ThreadedVoicePlayer(new MediaPlayer(audioMedia));
//
//        vp.run();
//        Thread.sleep(2000);
//        vp.halt();
//
//        assertNotEquals(vp, null);
    }
}
