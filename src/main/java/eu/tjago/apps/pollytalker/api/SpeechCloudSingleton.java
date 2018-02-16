package eu.tjago.apps.pollytalker.api;

import com.amazonaws.AmazonClientException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.polly.AmazonPollyClient;
import com.amazonaws.services.polly.model.*;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

/**
 * Created by Tomasz on 2016-01-06.
 * Edited on 2018-02-14
 */
public class SpeechCloudSingleton {

    private static volatile SpeechCloudSingleton instance;
    private static volatile AmazonPollyClient pollyClient;
    private static volatile Path lastSavedFileLoc;

    private SpeechCloudSingleton() {
    }

    public static SpeechCloudSingleton getInstance() {
        synchronized (SpeechCloudSingleton.class) {
            if (instance == null) {
                instance = new SpeechCloudSingleton();
                instance.initSpeechCloudClient(Region.getRegion(Regions.EU_WEST_1));
            }
        }
        return instance;
    }

    /**
     * Initialization
     *
     * @param region
     */
    public void initSpeechCloudClient(Region region) {
        pollyClient = new AmazonPollyClient(new DefaultAWSCredentialsProviderChain(),
                new ClientConfiguration());

        pollyClient.setRegion(region);
    }

    /**
     * Method returns list of all Polly voices
     * It's also serving the purpose of testing Credentials in this Singleton
     *
     * @return
     * @throws AmazonClientException
     */
    public static List<Voice> getAllVoicesList() throws AmazonClientException {
        return pollyClient.describeVoices(new DescribeVoicesRequest()).getVoices();
    }

    public static Optional<Voice> getVoicebyName(String voiceName) {
        List<Voice> voices = pollyClient.describeVoices(new DescribeVoicesRequest()).getVoices();
        return voices.stream()
                .filter(voice -> voice.getName().equals(voiceName))
                .findFirst();
    }

    public static InputStream synthesize(String text, OutputFormat format, Voice voice) throws IOException {
        SynthesizeSpeechRequest synthReq =
                new SynthesizeSpeechRequest().withText(text).withVoiceId(voice.getId())
                        .withOutputFormat(format);
        SynthesizeSpeechResult synthRes = pollyClient.synthesizeSpeech(synthReq);

        return synthRes.getAudioStream();
    }

    public static Optional<Path> getLastSavedFileLoc() {
        if (lastSavedFileLoc != null) {
            return Optional.of(lastSavedFileLoc);
        }
        return Optional.empty();
    }

    public static void setLastSavedFileLoc(Path location) {
        lastSavedFileLoc = location;
    }
}
