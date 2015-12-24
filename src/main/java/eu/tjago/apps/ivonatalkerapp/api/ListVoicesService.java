package eu.tjago.apps.ivonatalkerapp.api;

import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.ivona.services.tts.IvonaSpeechCloudClient;
import com.ivona.services.tts.model.ListVoicesRequest;
import com.ivona.services.tts.model.ListVoicesResult;
import com.ivona.services.tts.model.Voice;

import java.util.List;

/**
 * Created by Tomasz on 2015-12-24.
 */

public class ListVoicesService {

    public static final String IVONA_ENDPOINT_URL = "https://tts.eu-west-1.ivonacloud.com";
    public static final String LANGUAGE_EN_US = "en-US";
    public static final String IVONA_CREDENTIALS_PROPERTIES = "IvonaCredentials.properties";

    private IvonaSpeechCloudClient speechCloud;

    private void init() {
        this.speechCloud = new IvonaSpeechCloudClient(
                new ClasspathPropertiesFileCredentialsProvider(IVONA_CREDENTIALS_PROPERTIES));
        this.speechCloud.setEndpoint(IVONA_ENDPOINT_URL);
    }

    public ListVoicesService() {
        init();
//        listAllVoices();
        printVoices(listVoicesFromLanguage(LANGUAGE_EN_US));
    }

    private List<Voice> listAllVoices() {
        ListVoicesRequest allVoicesRequest = new ListVoicesRequest();
        ListVoicesResult allVoicesResult = speechCloud.listVoices(allVoicesRequest);

        return allVoicesResult.getVoices();
    }

    private List<Voice> listVoicesFromLanguage(String lang) {
        ListVoicesRequest voicesRequest = new ListVoicesRequest();
        Voice voice = new Voice();
        voice.setLanguage(lang);
        voicesRequest.setVoice(voice);
        ListVoicesResult listVoicesResult = speechCloud.listVoices(voicesRequest);
        System.out.println(lang + " voices: ");

        return listVoicesResult.getVoices();
    }

    public void printVoices(List<Voice> voicesList) {
        for (Voice voice : voicesList) {
            System.out.println(voice);
        }
    }
}
