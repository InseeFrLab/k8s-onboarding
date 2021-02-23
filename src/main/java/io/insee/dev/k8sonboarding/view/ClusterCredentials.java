package io.insee.dev.k8sonboarding.view;

public class ClusterCredentials {
    private String apiserverUrl;
    private String token = null;
    private String namespace;
    private boolean onboarded;
    private String user;
    private String clusterName;
    private boolean insecure;


    public String getToken() {
	return token;
    }

    public void setToken(String token) {
	this.token = token;
    }

    public String getApiserverUrl() {
	return apiserverUrl;
    }

    public void setApiserverUrl(String apiserverUrl) {
	this.apiserverUrl = apiserverUrl;
    }

    public String getNamespace() {
	return namespace;
    }

    public void setNamespace(String namespace) {
	this.namespace = namespace;
    }

    public String getUser() {
	return user;
    }

    public void setUser(String user) {
	this.user = user;
    }

    public boolean isOnboarded() {
        return onboarded;
    }

    public void setOnboarded(boolean onboarded) {
        this.onboarded = onboarded;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public boolean isInsecure() {
        return insecure;
    }

    public void setInsecure(boolean insecure) {
        this.insecure = insecure;
    }
}