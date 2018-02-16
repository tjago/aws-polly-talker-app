package eu.tjago.apps.pollytalker.model;

import com.amazonaws.auth.AWSCredentials;

/**
 * Created by Tomasz on 2016-01-06.
 */
public class PollyCredentials implements AWSCredentials{
    private String accessKey;
    private String secretKey;

    public PollyCredentials() {}

    public PollyCredentials(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public PollyCredentials setAccessKey(String accessKey) {
        this.accessKey = accessKey;
        return this;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public PollyCredentials setSecretKey(String secretKey) {
        this.secretKey = secretKey;
        return this;
    }

    @Override
    public String getAWSAccessKeyId() {
        return this.getAccessKey();
    }

    @Override
    public String getAWSSecretKey() {
        return this.getSecretKey();
    }
}