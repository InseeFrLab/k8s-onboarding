package io.insee.dev.k8sonboarding.view;

public class AppConfiguration {
    private String authUrl;
    private String resource;
    private String clientId;
    private String authority;

    public AppConfiguration() {
	super();
    }

    public String getAuthUrl() {
	return authUrl;
    }

    public void setAuthUrl(String authUrl) {
	this.authUrl = authUrl;
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

    public String getAuthority() {
	return authority;
    }

    public void setAuthority(String authority) {
	this.authority = authority;
    }

}