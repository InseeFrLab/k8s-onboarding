package io.insee.dev.k8sonboarding.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;


@Configuration
@ConfigurationProperties(prefix="io.insee.dev.k8sonboarding")
public class ClusterProperties {
  
  private String namespacePrefix;
  private String namespaceGroupPrefix;
  private String userPrefix;
  private String groupPrefix;
  private String nameNamespaceAdmin;
  private String apiserverUrl;
  private String clusterName;
  private boolean insecure;

  public void setNamespacePrefix(String namespacePrefix) {
    this.namespacePrefix = namespacePrefix;
  }
 
  public String getNameNamespaceAdmin() {
    return nameNamespaceAdmin;
  }
  public void setNameNamespaceAdmin(String nameNamespaceAdmin) {
    this.nameNamespaceAdmin = nameNamespaceAdmin;
  }
  
  public String getNamespacePrefix() {
    return namespacePrefix;
  }

  public String getApiserverUrl() {
    return apiserverUrl;
  }
  public void setApiserverUrl(String apiserverUrl) {
    this.apiserverUrl = apiserverUrl;
  }

  public String getUserPrefix() {
    return userPrefix;
  }

  public void setUserPrefix(String userPrefix) {
    this.userPrefix = userPrefix;
  }

  public String getClusterName() {
    return clusterName;
  }

  public void setClusterName(String clusterName) {
    this.clusterName = clusterName;
  }

  public String getGroupPrefix() {
    return groupPrefix;
  }

  public void setGroupPrefix(String groupPrefix) {
    this.groupPrefix = groupPrefix;
  }

  public void setNamespaceGroupPrefix(String namespaceGroupPrefix) {
    this.namespaceGroupPrefix = namespaceGroupPrefix;
  }

  public String getNamespaceGroupPrefix() {
    return namespaceGroupPrefix;
  }

  public void setInsecure(boolean insecure) {
    this.insecure = insecure;
  }

  public boolean getInsecure() {
    return insecure;
  }
}
