package bo.sterenborg.voicerecognition.util;

import bo.sterenborg.voicerecognition.util.exception.VoiceRecognitionException;
import bo.sterenborg.voicerecognition.util.properties.DialogFlowProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.dialogflow.v2.SessionsSettings;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.UUID;

/**
 * @author Bo Sterenborg
 * Provides some session utilities for this application.
 */
public class SessionUtils {
    public static final DialogFlowProperties DIALOG_FLOW_PROPERTIES = getDialogFlowProperties();
    private static final String RESOURCES_PATH = "src/main/resources";
    private static final String GOOGLE_AUTH_JSON_FILE = RESOURCES_PATH + "/authentication/dialogflow.json";

    /**
     * Creates a default {@link SessionsSettings} instance, using the {@link SessionUtils#getGoogleCredentials}.
     *
     * @return = a default {@link SessionsSettings} instance.
     */
    public static SessionsSettings getDefaultSessionSettings() {
        try {
            return SessionsSettings.newBuilder()
                    //use the default google authentication credentials for dialogflow
                    .setCredentialsProvider(FixedCredentialsProvider.create(getGoogleCredentials())).build();
        } catch (IOException e) {
            throw new VoiceRecognitionException(e);
        }
    }

    /**
     * Creates a new session {@link UUID}. Can be used to define a unique dialog flow session.
     *
     * @return = a random UUID.
     */
    public static String newSessionUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * Gets the Google credentials from the {@link GOOGLE_AUTH_JSON_FILE} file and converts it to a Java {@link GoogleCredentials}
     * instance.
     *
     * @return = {@link GoogleCredentials} from the default Google dialog flow credentials.
     */
    private static GoogleCredentials getGoogleCredentials() {
        try {
            return GoogleCredentials.fromStream(new FileInputStream(GOOGLE_AUTH_JSON_FILE));
        } catch (IOException e) {
            throw new VoiceRecognitionException(e);
        }
    }

    /**
     * Injects the {@link GOOGLE_AUTH_JSON_FILE} into a Java implementation.
     *
     * @return = {@link DialogFlowProperties}
     */
    private static DialogFlowProperties getDialogFlowProperties() {
        try {
            return new ObjectMapper().readValue(new FileInputStream(GOOGLE_AUTH_JSON_FILE), DialogFlowProperties.class);
        } catch (IOException e) {
            throw new VoiceRecognitionException(e);
        }
    }


}
