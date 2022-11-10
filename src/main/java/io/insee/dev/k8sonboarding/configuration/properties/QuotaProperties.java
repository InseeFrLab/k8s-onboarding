package io.insee.dev.k8sonboarding.configuration.properties;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "io.insee.dev.k8sonboarding.namespace-quota")
public class QuotaProperties {

    private String memoryRequests;
    private String cpuRequests;
    private String memoryLimits;
    private String cpuLimits;
    private String storageRequests;
    private Integer podsCount;
    
    public QuotaProperties() {
        super();
    }

    
    public Map<String, String> asMap() {
        Map<String, String> quotas = new HashMap<>();
        quotas.put("requests.memory",getMemoryRequests());
        quotas.put("requests.cpu", getCpuRequests());
        quotas.put("limits.memory", getMemoryLimits());
        quotas.put("limits.cpu", getCpuLimits());
        quotas.put("requests.storage", getStorageRequests());
        quotas.put("count/pods", getPodsCount() == null ? null : String.valueOf(getPodsCount()));
        return quotas;
    }
    
    public String getMemoryRequests() {
        return memoryRequests;
    }

    public void setMemoryRequests(String memoryRequests) {
        this.memoryRequests = memoryRequests;
    }

    public String getCpuRequests() {
        return cpuRequests;
    }

    public void setCpuRequests(String cpuRequests) {
        this.cpuRequests = cpuRequests;
    }

    public String getMemoryLimits() {
        return memoryLimits;
    }

    public void setMemoryLimits(String memoryLimits) {
        this.memoryLimits = memoryLimits;
    }

    public String getCpuLimits() {
        return cpuLimits;
    }

    public void setCpuLimits(String cpuLimits) {
        this.cpuLimits = cpuLimits;
    }

    public String getStorageRequests() {
        return storageRequests;
    }

    public void setStorageRequests(String storageRequests) {
        this.storageRequests = storageRequests;
    }

    public Integer getPodsCount() {
        return podsCount;
    }

    public void setPodsCount(Integer podsCount) {
        this.podsCount = podsCount;
    }
    
}
