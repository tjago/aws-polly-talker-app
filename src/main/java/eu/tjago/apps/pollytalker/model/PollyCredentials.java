package eu.tjago.apps.pollytalker.model;

import com.amazonaws.auth.AWSCredentials;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Credentials class
 * For use in application and storing into a file using JAXB
 *
 * Credentials are later loaded using System property values.
 *
 * Created by tjago
 */

@XmlRootElement(name = "credentialsRoot")
public class PollyCredentials implements AWSCredentials{
    private String AWSAccessKeyId;
    private String AWSSecretKey;

    public PollyCredentials() {}

    public PollyCredentials(String AWSAccessKeyId, String AWSSecretKey) {
        this.AWSAccessKeyId = AWSAccessKeyId;
        this.AWSSecretKey = AWSSecretKey;
    }

    @XmlElement(name = "accessKey")
    @Override
    public String getAWSAccessKeyId() {
        return this.AWSAccessKeyId;
    }

    @XmlElement(name = "secretKey")
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
