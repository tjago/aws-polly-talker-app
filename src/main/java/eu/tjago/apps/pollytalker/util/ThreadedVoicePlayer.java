package eu.tjago.apps.pollytalker.util;

import javafx.scene.media.MediaPlayer;

import java.util.Optional;

/**
 * Created by tjago
 */
public class ThreadedVoicePlayer implements Runnable, AutoCloseable {

    private volatile Thread voiceThread;
    private final Optional<MediaPlayer> mediaPlayer;

    public ThreadedVoicePlayer(MediaPlayer newMediaPlayer) {
        this.voiceThread = new Thread();
        this.mediaPlayer = Optional.of(newMediaPlayer);
    }

    public void halt() {
        this.close();
        voiceThread.interrupt();
    }

    public void run() {
        this.mediaPlayer.ifPresent(MediaPlayer::play);
    }

    @Override
    public void close() {
        this.mediaPlayer.ifPresent(MediaPlayer::stop);
        this.mediaPlayer.ifPresent(MediaPlayer::dispose);
    }
}
