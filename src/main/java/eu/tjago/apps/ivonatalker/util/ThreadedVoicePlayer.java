package eu.tjago.apps.ivonatalker.util;

import javafx.scene.media.MediaPlayer;

/**
 * Created by tjago on 2016-02-24.
 */
public class ThreadedVoicePlayer implements Runnable {

    private final static long interval = 300L;
    private volatile Thread voiceThread;
    private final MediaPlayer mediaPlayer;

    public ThreadedVoicePlayer(MediaPlayer newMediaPlayer) {
        this.voiceThread = new Thread();
        this.mediaPlayer = newMediaPlayer;
        System.out.println("Thread initialized!");
    }

    public void halt() {
        System.out.println("Stopping Thread");
        mediaPlayer.stop();
        voiceThread.interrupt();
    }

    public void run() {
        this.mediaPlayer.play();
    }
}
