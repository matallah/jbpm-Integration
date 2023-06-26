package com.jbpm.integration.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
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
}
