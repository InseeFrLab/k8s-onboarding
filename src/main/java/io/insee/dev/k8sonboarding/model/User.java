package io.insee.dev.k8sonboarding.model;

import java.util.List;

public class User {

    private String id;
    // TODO : move it somewhere else ?
    private String authToken;

    private List<String> groups;

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

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }
}
