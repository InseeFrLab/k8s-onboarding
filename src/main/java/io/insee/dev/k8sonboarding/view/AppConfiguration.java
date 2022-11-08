package io.insee.dev.k8sonboarding.view;

public class AppConfiguration {
    private String authUrl;
    private String resource;
    private String clientId;
    private String authority;
    private String groupFilter;

    private String userNamespaceEnabled;

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

    public String getGroupFilter() {
        return groupFilter;
    }

    public void setGroupFilter(String groupFilter) {
        this.groupFilter = groupFilter;
    }

    public String getUserNamespaceEnabled() {
        return userNamespaceEnabled;
    }

    public void setUserNamespaceEnabled(String userNamespaceEnabled) {
        this.userNamespaceEnabled = userNamespaceEnabled;
    }
}