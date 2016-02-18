package eu.tjago.apps.ivonatalker.api;

import com.amazonaws.AmazonClientException;
import com.amazonaws.internal.StaticCredentialsProvider;
import com.ivona.services.tts.IvonaSpeechCloudClient;
import com.ivona.services.tts.model.*;
import eu.tjago.apps.ivonatalker.util.Constants;

import java.io.*;
import java.util.List;

/**
 * Created by Tomasz on 2016-01-06.
 */
public class SpeechCloudSingleton {

    private static volatile SpeechCloudSingleton instance;
    private static volatile IvonaSpeechCloudClient speechCloud;
    public static final String TMP_SPEECH_FILE = "tmpSpeechFile.mp3";

    private SpeechCloudSingleton() {
    }

    public static SpeechCloudSingleton getInstance() {
        synchronized (SpeechCloudSingleton.class) {
            if (instance == null) {
                instance = new SpeechCloudSingleton();
            }
        }

        return instance;
    }

    /**
     * Initialization
     *
     * @param credentials
     */
    public static void initSpeechCloudClient(IvonaCredentials credentials) {
        speechCloud = new IvonaSpeechCloudClient(new StaticCredentialsProvider(credentials));

        speechCloud.setEndpoint(Constants.IVONA_SERVICE_ENDPOINT);
    }

    /**
     * Method returns list of Ivona Speech cloud voices
     * It's also serving the purpose of testing Credentials in this Singleton
     *
     * @return
     * @throws AmazonClientException
     */
    public static List<Voice> getAllVoicesList() throws AmazonClientException {
        ListVoicesRequest allVoicesRequest = new ListVoicesRequest();
        ListVoicesResult allVoicesResult = speechCloud.listVoices(allVoicesRequest);

        return allVoicesResult.getVoices();
    }

    public static void createSpeech(String voiceName, String textInput) {
        Input input = new Input();
        Voice voice = new Voice();

        voice.setName(voiceName);
        input.setData(textInput);

        CreateSpeechRequest createSpeechRequest = new CreateSpeechRequest();
        createSpeechRequest.setInput(input);
        createSpeechRequest.setVoice(voice);

        InputStream in = null;
        FileOutputStream outputStream = null;

        try {

            CreateSpeechResult createSpeechResult = speechCloud.createSpeech(createSpeechRequest);

            printConsoleInfo(createSpeechResult);

            in = createSpeechResult.getBody();
            outputStream = new FileOutputStream(new File(TMP_SPEECH_FILE));

            byte[] buffer = new byte[2 * 1024];
            int readBytes;

            while ((readBytes = in.read(buffer)) > 0) {
                // In the example we are only printing the bytes counter,
                // In real-life scenario we would operate on the buffer
                System.out.println(" received bytes: " + readBytes);
                outputStream.write(buffer, 0, readBytes);
            }

            System.out.println("\nFile saved: " + TMP_SPEECH_FILE);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void printConsoleInfo(CreateSpeechResult createSpeechResult) {
        System.out.println("\nSuccess sending request:");
        System.out.println(" content type:\t" + createSpeechResult.getContentType());
        System.out.println(" request id:\t" + createSpeechResult.getTtsRequestId());
        System.out.println(" request chars:\t" + createSpeechResult.getTtsRequestCharacters());
        System.out.println(" request units:\t" + createSpeechResult.getTtsRequestUnits());

        System.out.println("\nStarting to retrieve audio stream:");
    }

    public static String getTmpSpeechFilename() {
        return TMP_SPEECH_FILE;
    }

    public static File getTmpSpeechFile() {
        return new File(TMP_SPEECH_FILE);
    }

}
