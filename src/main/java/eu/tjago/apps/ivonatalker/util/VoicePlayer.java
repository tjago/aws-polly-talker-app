package eu.tjago.apps.ivonatalker.util;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.nio.file.Paths;

/**
 * Created by tjago on 2016-02-24.
 */
public class VoicePlayer implements Runnable {

    private final static long interval = 300L;
    private volatile Thread blinker;

    public VoicePlayer() {
        this.blinker = new Thread();
        System.out.println("Thread initialized!");
    }

    public void halt() {
        System.out.println("Stopping Thread");
        blinker.interrupt();
    }

    public void run() {
        this.

//        while (true) {
//            try {
//                Thread.sleep(interval);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            System.out.println("Doing Voice Player thread job...");
//        }
    }


    private void playAudioFile(String file) {

        private MediaPlayer audioMediaPlayer;

        if(audioMediaPlayer != null) {
            audioMediaPlayer.dispose();
        }
        Media audioMedia = new Media(Paths.get(file).toUri().toString());
        audioMediaPlayer = new MediaPlayer(audioMedia);
        audioMediaPlayer.play();
    }
}
