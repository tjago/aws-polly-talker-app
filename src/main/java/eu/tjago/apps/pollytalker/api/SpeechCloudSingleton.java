package eu.tjago.apps.pollytalker.api;

import com.amazonaws.AmazonClientException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.polly.AmazonPollyClient;
import com.amazonaws.services.polly.model.*;

import java.io.*;
import java.nio.file.StandardCopyOption;
import java.util.List;

/**
 * Created by Tomasz on 2016-01-06.
 * Edited on 2018-02-14
 */
public class SpeechCloudSingleton {

    private static volatile SpeechCloudSingleton instance;
    private static volatile AmazonPollyClient pollyClient;
    public static final String TMP_SPEECH_FILE = "tmpSpeechFile.mp3";

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


    public static InputStream synthesize(String text, OutputFormat format, Voice voice) throws IOException {
        SynthesizeSpeechRequest synthReq =
                new SynthesizeSpeechRequest().withText(text).withVoiceId(voice.getId())
                        .withOutputFormat(format);
        SynthesizeSpeechResult synthRes = pollyClient.synthesizeSpeech(synthReq);

        return synthRes.getAudioStream();
    }

    public static String getTmpSpeechFilename() {
        return TMP_SPEECH_FILE;
    }

    public static File getTmpSpeechFile() {
        return new File(TMP_SPEECH_FILE);
    }

    public static void saveToFile(InputStream speechStream) {

        try {
            java.nio.file.Files.copy(
                    speechStream,
                    getTmpSpeechFile().toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.out.println("encountered error when saving speech stream to file: " + e.getMessage());
//            e.printStackTrace();
        }
    }
}
