package bo.sterenborg.voicerecognition.util.properties;

import com.fasterxml.jackson.annotation.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DialogFlowProperties {
    @JsonProperty("type")
    private String type;
    @JsonProperty("project_id")
    private String projectId;
    @JsonProperty("private_key_id")
    private String privateKeyId;
    @JsonProperty("private_key")
    private String privateKey;
    @JsonProperty("client_email")
    private String clientEmail;
    @JsonProperty("client_id")
    private String clientId;
    @JsonProperty("auth_uri")
    private String authUri;
    @JsonProperty("token_uri")
    private String tokenUri;
    @JsonProperty("auth_provider_x509_cert_url")
    private String authProviderX509CertUrl;
    @JsonProperty("client_x509_cert_url")
    private String clientX509CertUrl;

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("project_id")
    public String getProjectId() {
        return projectId;
    }

    @JsonProperty("project_id")
    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    @JsonProperty("private_key_id")
    public String getPrivateKeyId() {
        return privateKeyId;
    }

    @JsonProperty("private_key_id")
    public void setPrivateKeyId(String privateKeyId) {
        this.privateKeyId = privateKeyId;
    }

    @JsonProperty("private_key")
    public String getPrivateKey() {
        return privateKey;
    }

    @JsonProperty("private_key")
    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    @JsonProperty("client_email")
    public String getClientEmail() {
        return clientEmail;
    }

    @JsonProperty("client_email")
    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    @JsonProperty("client_id")
    public String getClientId() {
        return clientId;
    }

    @JsonProperty("client_id")
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @JsonProperty("auth_uri")
    public String getAuthUri() {
        return authUri;
    }

    @JsonProperty("auth_uri")
    public void setAuthUri(String authUri) {
        this.authUri = authUri;
    }

    @JsonProperty("token_uri")
    public String getTokenUri() {
        return tokenUri;
    }

    @JsonProperty("token_uri")
    public void setTokenUri(String tokenUri) {
        this.tokenUri = tokenUri;
    }

    @JsonProperty("auth_provider_x509_cert_url")
    public String getAuthProviderX509CertUrl() {
        return authProviderX509CertUrl;
    }

    @JsonProperty("auth_provider_x509_cert_url")
    public void setAuthProviderX509CertUrl(String authProviderX509CertUrl) {
        this.authProviderX509CertUrl = authProviderX509CertUrl;
    }

    @JsonProperty("client_x509_cert_url")
    public String getClientX509CertUrl() {
        return clientX509CertUrl;
    }

    @JsonProperty("client_x509_cert_url")
    public void setClientX509CertUrl(String clientX509CertUrl) {
        this.clientX509CertUrl = clientX509CertUrl;
    }
}
