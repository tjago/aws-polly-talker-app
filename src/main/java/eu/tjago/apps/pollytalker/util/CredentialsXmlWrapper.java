package eu.tjago.apps.pollytalker.util;

import eu.tjago.apps.pollytalker.model.PollyCredentials;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Tomasz on 2016-02-15.
 */

@XmlRootElement(name = "credentialsRoot")
public class CredentialsXmlWrapper {

    private PollyCredentials credentials;

    public CredentialsXmlWrapper() {
    }

    public CredentialsXmlWrapper(PollyCredentials credentials) {
        this.credentials = credentials;
    }

    @XmlElement(name = "credentials")
    public PollyCredentials getCredentials() {
        return credentials;
    }

    public void setCredentials(PollyCredentials credentials) {
        this.credentials = credentials;
    }
}
