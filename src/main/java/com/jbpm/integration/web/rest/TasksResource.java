package com.jbpm.integration.web.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jbpm.integration.domain.ProcessInstances;
import com.jbpm.integration.domain.Tasks;
import com.jbpm.integration.repository.TasksRepository;
import com.jbpm.integration.service.dto.TasksDTO;
import com.jbpm.integration.service.dto.TasksList;
import com.jbpm.integration.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.jbpm.integration.domain.Tasks}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TasksResource extends BaseController {

    private final Logger log = LoggerFactory.getLogger(TasksResource.class);

    private static final String ENTITY_NAME = "tasks";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TasksRepository tasksRepository;

    public TasksResource(RestTemplate restTemplate, TasksRepository tasksRepository) {
        super(restTemplate);
        this.tasksRepository = tasksRepository;
    }

    /**
     * {@code POST  /tasks} : Create a new tasks.
     *
     * @param tasks the tasks to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tasks, or with status {@code 400 (Bad Request)} if the tasks has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tasks")
    public ResponseEntity<Tasks> createTasks(@RequestBody Tasks tasks) throws URISyntaxException {
        log.debug("REST request to save Tasks : {}", tasks);
        if (tasks.getId() != null) {
            throw new BadRequestAlertException("A new tasks cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Tasks result = tasksRepository.save(tasks);
        return ResponseEntity
            .created(new URI("/api/tasks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tasks/:id} : Updates an existing tasks.
     *
     * @param id    the id of the tasks to save.
     * @param tasks the tasks to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tasks,
     * or with status {@code 400 (Bad Request)} if the tasks is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tasks couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tasks/{id}")
    public ResponseEntity<Tasks> updateTasks(@PathVariable(value = "id", required = false) final Long id, @RequestBody Tasks tasks)
        throws URISyntaxException {
        log.debug("REST request to update Tasks : {}, {}", id, tasks);
        ///server/containers/Payment_1.0.0-SNAPSHOT/tasks/states/claimed?taskId=4
        ///server/containers/Payment_1.0.0-SNAPSHOT/tasks/{taskInstanceId}/states/started
        String claim = baseJbpmURI + "/server/containers/" + containerID + "/tasks/states/claimed?taskId=" + tasks.getTaskId();
        String start = baseJbpmURI + "/server/containers/" + containerID + "/tasks/" + tasks.getTaskId() + "/states/started";
        String responseClaim = restTemplate.postForObject(claim, "", String.class);
        // Define the payload data to be sent in the request
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>("", headers);
        restTemplate.put(start, requestEntity);
        System.out.println(responseClaim);

        if (tasks.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tasks.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tasksRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Tasks result = tasksRepository.save(tasks);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tasks.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tasks/:id} : Partial updates given fields of an existing tasks, field will ignore if it is null
     *
     * @param id    the id of the tasks to save.
     * @param tasks the tasks to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tasks,
     * or with status {@code 400 (Bad Request)} if the tasks is not valid,
     * or with status {@code 404 (Not Found)} if the tasks is not found,
     * or with status {@code 500 (Internal Server Error)} if the tasks couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tasks/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Tasks> partialUpdateTasks(@PathVariable(value = "id", required = false) final Long id, @RequestBody Tasks tasks)
        throws URISyntaxException {
        log.debug("REST request to partial update Tasks partially : {}, {}", id, tasks);
        if (tasks.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tasks.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tasksRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Tasks> result = tasksRepository
            .findById(tasks.getId())
            .map(existingTasks -> {
                if (tasks.getTaskId() != null) {
                    existingTasks.setTaskId(tasks.getTaskId());
                }
                if (tasks.getTaskName() != null) {
                    existingTasks.setTaskName(tasks.getTaskName());
                }
                if (tasks.getTaskStatus() != null) {
                    existingTasks.setTaskStatus(tasks.getTaskStatus());
                }
                if (tasks.getPrice() != null) {
                    existingTasks.setPrice(tasks.getPrice());
                }
                if (tasks.getApprove() != null) {
                    existingTasks.setApprove(tasks.getApprove());
                }

                return existingTasks;
            })
            .map(tasksRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tasks.getId().toString())
        );
    }

    /**
     * {@code GET  /tasks} : get all the tasks.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tasks in body.
     */
    @GetMapping("/tasks")
    public List<Tasks> getAllTasks() throws JsonProcessingException {
        log.debug("REST request to get all Tasks");
        List<ProcessInstances> allProcess = getAllProcess();
        List<TasksDTO> allTasks = new ArrayList<>();

        for (ProcessInstances process : allProcess) {
            String url = baseJbpmURI + "/server/queries/tasks/instances/process/" + process.getProcessInstanceId();
            ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.GET, API(), String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            TasksList tasksList = objectMapper.readValue(exchange.getBody(), TasksList.class);
            List<TasksDTO> tasksDTOS = tasksList.getTasksDTOS();
            allTasks.addAll(tasksDTOS);
        }

        tasksRepository.deleteAll();
        for (TasksDTO tasksDTO : allTasks) {
            Tasks tasks = new Tasks();
            tasks.setTaskId(tasksDTO.getTaskId());
            tasks.setTaskName(tasksDTO.getTaskName());
            tasks.setTaskStatus(tasksDTO.getTaskStatus());
            tasksRepository.save(tasks);
        }
        return tasksRepository.findAll();
    }

    /**
     * {@code GET  /tasks/:id} : get the "id" tasks.
     *
     * @param id the id of the tasks to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tasks, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tasks/{id}")
    public ResponseEntity<Tasks> getTasks(@PathVariable Long id) {
        log.debug("REST request to get Tasks : {}", id);
        Optional<Tasks> tasks = tasksRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(tasks);
    }

    /**
     * {@code DELETE  /tasks/:id} : delete the "id" tasks.
     *
     * @param id the id of the tasks to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Void> deleteTasks(@PathVariable Long id) {
        log.debug("REST request to delete Tasks : {}", id);
        tasksRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
