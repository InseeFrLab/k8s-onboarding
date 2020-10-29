package io.insee.dev.k8sonboarding.model;

public class User {

    private String id;
    // TODO : move it somewhere else ?
    private String authToken;

    public String getId() {
	return id;
    }

    public void setId(String id) {
	this.id = id;
    }

    public String getAuthToken() {
	return authToken;
    }

    public void setAuthToken(String authToken) {
	this.authToken = authToken;
    }
}
