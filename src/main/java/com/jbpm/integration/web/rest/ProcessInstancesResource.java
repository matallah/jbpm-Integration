package com.jbpm.integration.web.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jbpm.integration.domain.ProcessInstances;
import com.jbpm.integration.repository.ProcessInstancesRepository;
import com.jbpm.integration.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.jbpm.integration.domain.ProcessInstances}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ProcessInstancesResource extends BaseController {

    private final Logger log = LoggerFactory.getLogger(ProcessInstancesResource.class);

    private static final String ENTITY_NAME = "processInstances";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProcessInstancesRepository processInstancesRepository;

    public ProcessInstancesResource(RestTemplate restTemplate, ProcessInstancesRepository processInstancesRepository) {
        super(restTemplate);
        this.processInstancesRepository = processInstancesRepository;
    }

    /**
     * {@code POST  /process-instances} : Create a new processInstances.
     *
     * @param processInstances the processInstances to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new processInstances, or with status {@code 400 (Bad Request)} if the processInstances has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/process-instances")
    public ResponseEntity<ProcessInstances> createProcessInstances(@RequestBody ProcessInstances processInstances)
        throws URISyntaxException {
        log.debug("REST request to save ProcessInstances : {}", processInstances);
        if (processInstances.getId() != null) {
            throw new BadRequestAlertException("A new processInstances cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProcessInstances result = processInstancesRepository.save(processInstances);
        return ResponseEntity
            .created(new URI("/api/process-instances/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /process-instances/:id} : Updates an existing processInstances.
     *
     * @param id               the id of the processInstances to save.
     * @param processInstances the processInstances to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated processInstances,
     * or with status {@code 400 (Bad Request)} if the processInstances is not valid,
     * or with status {@code 500 (Internal Server Error)} if the processInstances couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/process-instances/{id}")
    public ResponseEntity<ProcessInstances> updateProcessInstances(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProcessInstances processInstances
    ) throws URISyntaxException {
        log.debug("REST request to update ProcessInstances : {}, {}", id, processInstances);
        if (processInstances.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, processInstances.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!processInstancesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProcessInstances result = processInstancesRepository.save(processInstances);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, processInstances.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /process-instances/:id} : Partial updates given fields of an existing processInstances, field will ignore if it is null
     *
     * @param id               the id of the processInstances to save.
     * @param processInstances the processInstances to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated processInstances,
     * or with status {@code 400 (Bad Request)} if the processInstances is not valid,
     * or with status {@code 404 (Not Found)} if the processInstances is not found,
     * or with status {@code 500 (Internal Server Error)} if the processInstances couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/process-instances/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProcessInstances> partialUpdateProcessInstances(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProcessInstances processInstances
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProcessInstances partially : {}, {}", id, processInstances);
        if (processInstances.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, processInstances.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!processInstancesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProcessInstances> result = processInstancesRepository
            .findById(processInstances.getId())
            .map(existingProcessInstances -> {
                if (processInstances.getProcessInstanceId() != null) {
                    existingProcessInstances.setProcessInstanceId(processInstances.getProcessInstanceId());
                }
                if (processInstances.getProcessId() != null) {
                    existingProcessInstances.setProcessId(processInstances.getProcessId());
                }
                if (processInstances.getProcessName() != null) {
                    existingProcessInstances.setProcessName(processInstances.getProcessName());
                }

                return existingProcessInstances;
            })
            .map(processInstancesRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, processInstances.getId().toString())
        );
    }

    /**
     * {@code GET  /process-instances} : get all the processInstances.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of processInstances in body.
     */
    @GetMapping("/process-instances")
    public List<ProcessInstances> getAllProcessInstances() throws JsonProcessingException, JsonProcessingException {
        log.debug("REST request to get all ProcessInstances");
        processInstancesRepository.deleteAll();
        getAllProcess().forEach(processInstancesRepository::save);
        //return processInstancesList;
        return processInstancesRepository.findAll();
    }

    /**
     * {@code GET  /process-instances/:id} : get the "id" processInstances.
     *
     * @param id the id of the processInstances to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the processInstances, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/process-instances/{id}")
    public ResponseEntity<ProcessInstances> getProcessInstances(@PathVariable Long id) {
        log.debug("REST request to get ProcessInstances : {}", id);
        Optional<ProcessInstances> processInstances = processInstancesRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(processInstances);
    }

    /**
     * {@code DELETE  /process-instances/:id} : delete the "id" processInstances.
     *
     * @param id the id of the processInstances to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/process-instances/{id}")
    public ResponseEntity<Void> deleteProcessInstances(@PathVariable Long id) {
        log.debug("REST request to delete ProcessInstances : {}", id);
        processInstancesRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
