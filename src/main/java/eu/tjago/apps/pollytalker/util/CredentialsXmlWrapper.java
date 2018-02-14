package eu.tjago.apps.pollytalker.util;

import eu.tjago.apps.pollytalker.api.IvonaCredentials;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Tomasz on 2016-02-15.
 */

@XmlRootElement(name = "credentialsRoot")
public class CredentialsXmlWrapper {

    private IvonaCredentials credentials;

    public CredentialsXmlWrapper() {
    }

    public CredentialsXmlWrapper(IvonaCredentials credentials) {
        this.credentials = credentials;
    }

    @XmlElement(name = "credentials")
    public IvonaCredentials getCredentials() {
        return credentials;
    }

    public void setCredentials(IvonaCredentials credentials) {
        this.credentials = credentials;
    }
}
