package io.insee.dev.k8sonboarding.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "io.insee.dev.k8sonboarding.ui")
public class UiProperties {

    private String clientId;
    private String authority;
    private String groupFilter;
    private boolean userNamespaceEnabled;
    private boolean namespaceCreationAllowed;
    public UiProperties() {
        super();
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

    public boolean isUserNamespaceEnabled() {
        return userNamespaceEnabled;
    }

    public void setUserNamespaceEnabled(boolean userNamespaceEnabled) {
        this.userNamespaceEnabled = userNamespaceEnabled;
    }

    public boolean isNamespaceCreationAllowed() {
        return namespaceCreationAllowed;
    }

    public void setNamespaceCreationAllowed(boolean namespaceCreationAllowed) {
        this.namespaceCreationAllowed = namespaceCreationAllowed;
    }
}
