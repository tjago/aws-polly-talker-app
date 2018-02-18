package eu.tjago.apps.pollytalker.util;

import javafx.scene.media.MediaPlayer;

/**
 * Created by tjago
 */
public class ThreadedVoicePlayer implements Runnable, AutoCloseable {

    private volatile Thread voiceThread;
    private final MediaPlayer mediaPlayer;

    public ThreadedVoicePlayer(MediaPlayer newMediaPlayer) {
        this.voiceThread = new Thread();
        this.mediaPlayer = newMediaPlayer;
    }

    public void halt() {
        this.close();
        voiceThread.interrupt();
    }

    public void run() {
        this.mediaPlayer.play();
    }

    @Override
    public void close() {
        this.mediaPlayer.stop();
        this.mediaPlayer.dispose();
    }
}
