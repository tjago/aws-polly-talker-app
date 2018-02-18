package eu.tjago.apps.pollytalker.model;

import com.amazonaws.auth.AWSCredentials;

/**
 * Substitution class
 * Can't use existing BasicAWSCredentials class here
 * as JAXB requires default constructor and all setters and getters
 *
 * Created by tjago
 */
public class PollyCredentials implements AWSCredentials{
    private String AWSAccessKeyId;
    private String AWSSecretKey;

    public PollyCredentials() {}

    public PollyCredentials(String AWSAccessKeyId, String AWSSecretKey) {
        this.AWSAccessKeyId = AWSAccessKeyId;
        this.AWSSecretKey = AWSSecretKey;
    }

    @Override
    public String getAWSAccessKeyId() {
        return this.AWSAccessKeyId;
    }

    @Override
    public String getAWSSecretKey() {
        return this.AWSSecretKey;
    }

    public void setAWSAccessKeyId(String AWSAccessKeyId) {
        this.AWSAccessKeyId = AWSAccessKeyId;
        System.setProperty("aws.accessKeyId", AWSAccessKeyId);
    }

    public void setAWSSecretKey(String AWSSecretKey) {
        this.AWSSecretKey = AWSSecretKey;
        System.setProperty("aws.secretKey", AWSSecretKey);
    }

    public boolean initialized() {
        return !getAWSAccessKeyId().isEmpty() && !getAWSSecretKey().isEmpty();
    }
}
