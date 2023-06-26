package com.jbpm.integration.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Process implements Serializable {

    @JsonProperty("process-instance")
    private List<ProcessInstancesDTO> processInstance;

    public List<ProcessInstancesDTO> getProcessInstance() {
        return processInstance;
    }

    public void setProcessInstance(List<ProcessInstancesDTO> processInstance) {
        this.processInstance = processInstance;
    }
}
