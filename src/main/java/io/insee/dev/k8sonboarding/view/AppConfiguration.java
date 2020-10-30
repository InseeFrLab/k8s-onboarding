package io.insee.dev.k8sonboarding.view;

public class AppConfiguration {
    private String authUrl;
    private String realm;
    private String resource;
    private String clientId;

    public AppConfiguration() {
	super();
    }

    public String getAuthUrl() {
	return authUrl;
    }

    public void setAuthUrl(String authUrl) {
	this.authUrl = authUrl;
    }

    public String getRealm() {
	return realm;
    }

    public void setRealm(String realm) {
	this.realm = realm;
    }

    public String getResource() {
	return resource;
    }

    public void setResource(String resource) {
	this.resource = resource;
    }

    public String getClientId() {
	return clientId;
    }

    public void setClientId(String clientId) {
	this.clientId = clientId;
    }

}