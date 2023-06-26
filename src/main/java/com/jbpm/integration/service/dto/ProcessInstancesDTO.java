package com.jbpm.integration.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

/**
 * A ProcessInstances.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProcessInstancesDTO implements Serializable {

    @JsonProperty("process-instance-id")
    private Integer processInstanceId;

    @JsonProperty("container-id")
    private String processId;

    @JsonProperty("process-name")
    private String processName;

    public Integer getProcessInstanceId() {
        return this.processInstanceId;
    }

    public ProcessInstancesDTO processInstanceId(Integer processInstanceId) {
        this.setProcessInstanceId(processInstanceId);
        return this;
    }

    public void setProcessInstanceId(Integer processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getProcessId() {
        return this.processId;
    }

    public ProcessInstancesDTO processId(String processId) {
        this.setProcessId(processId);
        return this;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getProcessName() {
        return this.processName;
    }

    public ProcessInstancesDTO processName(String processName) {
        this.setProcessName(processName);
        return this;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }
}
