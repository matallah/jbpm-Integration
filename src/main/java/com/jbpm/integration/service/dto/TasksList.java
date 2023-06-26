package com.jbpm.integration.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TasksList {

    @JsonProperty("task-summary")
    private List<TasksDTO> tasksDTOS;

    public List<TasksDTO> getTasksDTOS() {
        return tasksDTOS;
    }

    public void setTasksDTOS(List<TasksDTO> tasksDTOS) {
        this.tasksDTOS = tasksDTOS;
    }
}
