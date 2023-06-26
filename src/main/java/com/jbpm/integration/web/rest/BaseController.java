package com.jbpm.integration.web.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jbpm.integration.domain.ProcessInstances;
import com.jbpm.integration.service.dto.ProcessInstancesDTO;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class BaseController {

    protected String containerID = "Payment_1.0.0-SNAPSHOT"; // Get it from container DAO
    protected final String baseJbpmURI = "http://localhost:9090/kie-server/services/rest/";
    protected String userName = "wbadmin", password = "wbadmin";
    protected final String jbpmEndPoint = baseJbpmURI + "server/containers/";

    @Autowired
    protected final RestTemplate restTemplate;

    public BaseController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    protected HttpEntity<String> API() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        String authStr = userName + ":" + password;
        String base64AuthStr = Base64.getEncoder().encodeToString(authStr.getBytes());
        httpHeaders.set("Authorization", "Basic " + base64AuthStr);
        return new HttpEntity<>(httpHeaders);
    }

    protected List<ProcessInstances> getAllProcess() {
        String url = jbpmEndPoint + containerID + "/processes/instances/";
        ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.GET, API(), String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        com.jbpm.integration.service.dto.Process process = null;
        try {
            process = objectMapper.readValue(exchange.getBody(), com.jbpm.integration.service.dto.Process.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        List<ProcessInstancesDTO> processInstance = process.getProcessInstance();
        List<ProcessInstances> processInstancesList = new ArrayList<>();
        for (int i = 0, processInstanceSize = processInstance.size(); i < processInstanceSize; i++) {
            ProcessInstances processInstances = new ProcessInstances();
            ProcessInstancesDTO instances = processInstance.get(i);
            processInstances.setProcessId(instances.getProcessId());
            processInstances.setProcessName(instances.getProcessName());
            processInstances.setProcessInstanceId(instances.getProcessInstanceId());
            processInstancesList.add(processInstances);
        }
        return processInstancesList;
    }
}
