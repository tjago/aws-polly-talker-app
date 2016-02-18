package eu.tjago.apps.ivonatalker.api;

import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.internal.StaticCredentialsProvider;
import com.ivona.services.tts.IvonaSpeechCloudClient;
import com.ivona.services.tts.model.ListVoicesRequest;
import com.ivona.services.tts.model.ListVoicesResult;
import com.ivona.services.tts.model.Voice;
import eu.tjago.apps.ivonatalker.util.Constants;

import java.util.List;

/**
 * Created by Tomasz on 2015-12-24.
 */

@Deprecated
public class ListVoicesService {

    private IvonaSpeechCloudClient speechCloud;

    /**
     * Init Speech cloud with credentials provide from properties file
     */
    private void init() {
        this.speechCloud = new IvonaSpeechCloudClient(
                new ClasspathPropertiesFileCredentialsProvider(Constants.IVONA_CREDENTIALS_PROPERTIES));
        this.speechCloud.setEndpoint(Constants.IVONA_SERVICE_ENDPOINT);
    }

    /**
     * Init Speech cloud by credentials model
     *
     * @param credentials
     */
    private void init(IvonaCredentials credentials) {
        this.speechCloud = new IvonaSpeechCloudClient(
                new StaticCredentialsProvider(credentials));
        this.speechCloud.setEndpoint(Constants.IVONA_SERVICE_ENDPOINT);
    }


    public ListVoicesService() {
        init();
//        printVoices(listVoicesFromLanguage(Constants.PREFERED_LANGUAGE));
    }

    public List<Voice> getAllVoicesList() {
        ListVoicesRequest allVoicesRequest = new ListVoicesRequest();
        ListVoicesResult allVoicesResult = speechCloud.listVoices(allVoicesRequest);

        return allVoicesResult.getVoices();
    }

    public List<Voice> listVoicesFromLanguage(String lang) {
        ListVoicesRequest voicesRequest = new ListVoicesRequest();
          Voice voice = new Voice();
          voice.setLanguage(lang);
          voicesRequest.setVoice(voice);

        return speechCloud.listVoices(voicesRequest).getVoices();
    }

    public void printVoices(List<Voice> voicesList) {
        for (Voice voice : voicesList) {
            System.out.println(voice);
        }
    }
}
