package eu.tjago.apps.pollytalker.api;

import com.amazonaws.AmazonClientException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Region;
import com.amazonaws.services.polly.AmazonPollyClient;
import com.amazonaws.services.polly.model.*;
import eu.tjago.apps.pollytalker.util.Constants;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by tjago
 */
public class AwsClientSingleton {

    private static volatile AwsClientSingleton instance;
    private static volatile AmazonPollyClient pollyClient;
    private static volatile Path lastSavedFileLoc;

    private AwsClientSingleton() {
    }

    /**
     * method initializes
     * @return
     */
    public static AwsClientSingleton getInstance() {
        synchronized (AwsClientSingleton.class) {
            if (instance == null) {
                instance = new AwsClientSingleton();
                instance.initAwsPollyClient(Region.getRegion(Constants.DEFAULT_AWS_REGION));
            }
        }
        return instance;
    }

    /**
     * default Initialization of Polly client from Provider chain
     *
     * @param region
     */
    public void initAwsPollyClient(Region region) {
        pollyClient = new AmazonPollyClient(new DefaultAWSCredentialsProviderChain(),
                new ClientConfiguration());

        pollyClient.setRegion(region);
    }

    public void initAwsPollyClientFromCredentials(Region region) {
        pollyClient = new AmazonPollyClient();
        pollyClient.setRegion(region);
    }

    /**
     * Method returns list of all Polly voices
     * It's also serving the purpose of testing Credentials in this Singleton
     *
     * @return
     * @throws AmazonClientException
     */
    public static List<Voice> getAllVoicesList() {
        try {
            return pollyClient.describeVoices(new DescribeVoicesRequest()).getVoices();
        } catch (SdkClientException e) {
            return Collections.emptyList();
        }
    }

    public static Optional<Voice> getVoiceByName(String voiceName) {
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
