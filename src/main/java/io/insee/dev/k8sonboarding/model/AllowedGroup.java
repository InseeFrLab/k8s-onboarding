package io.insee.dev.k8sonboarding.model;

public class AllowedGroup {
    private String namespace;
    private String group;

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public AllowedGroup(String namespace, String group) {
        this.namespace = namespace;
        this.group = group;
    }
}
