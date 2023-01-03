package io.insee.dev.k8sonboarding.model;

public class AllowedGroup {
    String namespace;
    String group;

    public AllowedGroup(String namespace, String group) {
        this.namespace = namespace;
        this.group = group;
    }
}
