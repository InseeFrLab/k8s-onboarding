package io.insee.dev.k8sonboarding.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConfigurationProperties(prefix="io.insee.dev.k8sonboarding")
public class ClusterProperty {
  
  private String namespacePrefix = "prefix";
  private String nameClusterAdmin = "cluster-admin";
  private String nameNamespaceAdmin = "namespace_admin";
  private String apiserverUrl;
  
  public void setNamespacePrefix(String namespacePrefix) {
    this.namespacePrefix = namespacePrefix;
  }
  public String getNameClusterAdmin() {
    return nameClusterAdmin;
  }
  public void setNameClusterAdmin(String nameClusterAdmin) {
    this.nameClusterAdmin = nameClusterAdmin;
  }
  public String getNameNamespaceAdmin() {
    return nameNamespaceAdmin;
  }
  public void setNameNamespaceAdmin(String nameNamespaceAdmin) {
    this.nameNamespaceAdmin = nameNamespaceAdmin;
  }
  
  public String getNameSpaceId(String userId) {
    return String.join("-",namespacePrefix, userId);
  }
  public String getApiserverUrl() {
    return apiserverUrl;
  }
  public void setApiserverUrl(String apiserverUrl) {
    this.apiserverUrl = apiserverUrl;
  }
  
}
