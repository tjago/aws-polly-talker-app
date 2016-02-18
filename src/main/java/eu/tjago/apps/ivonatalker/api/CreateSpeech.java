package eu.tjago.apps.ivonatalker.api;

import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.ivona.services.tts.IvonaSpeechCloudClient;
import com.ivona.services.tts.model.CreateSpeechRequest;
import com.ivona.services.tts.model.CreateSpeechResult;
import com.ivona.services.tts.model.Input;
import com.ivona.services.tts.model.Voice;
import eu.tjago.apps.ivonatalker.util.Constants;

import java.io.*;

/**
 * Created by Tomasz on 2015-12-24.
 */
@Deprecated
public class CreateSpeech {

    private IvonaSpeechCloudClient speechCloud;

    private void init() {
        speechCloud = new IvonaSpeechCloudClient(
                new ClasspathPropertiesFileCredentialsProvider(Constants.IVONA_CREDENTIALS_PROPERTIES));
        speechCloud.setEndpoint(Constants.IVONA_SERVICE_ENDPOINT);
    }

    public CreateSpeech() {
        this("Salli", "This is a sample text to be synthesized.");
    }

    public CreateSpeech(String voiceName, String textInput) {

        init();

        String outputFileName = "/tmp/speech.mp3"; //System.getProperty("java.io.tmpdir") - save to tmp folder if possible

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

            System.out.println("\nSuccess sending request:");
            System.out.println(" content type:\t" + createSpeechResult.getContentType());
            System.out.println(" request id:\t" + createSpeechResult.getTtsRequestId());
            System.out.println(" request chars:\t" + createSpeechResult.getTtsRequestCharacters());
            System.out.println(" request units:\t" + createSpeechResult.getTtsRequestUnits());

            System.out.println("\nStarting to retrieve audio stream:");

            in = createSpeechResult.getBody();
            outputStream = new FileOutputStream(new File(outputFileName));

            byte[] buffer = new byte[2 * 1024];
            int readBytes;

            while ((readBytes = in.read(buffer)) > 0) {
                // In the example we are only printing the bytes counter,
                // In real-life scenario we would operate on the buffer
                System.out.println(" received bytes: " + readBytes);
                outputStream.write(buffer, 0, readBytes);
            }

            System.out.println("\nFile saved: " + outputFileName);

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
}
