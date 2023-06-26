package com.jbpm.integration.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.jbpm.integration.IntegrationTest;
import com.jbpm.integration.domain.Tasks;
import com.jbpm.integration.repository.TasksRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TasksResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TasksResourceIT {

    private static final Integer DEFAULT_TASK_ID = 1;
    private static final Integer UPDATED_TASK_ID = 2;

    private static final String DEFAULT_TASK_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TASK_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TASK_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_TASK_STATUS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tasks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TasksRepository tasksRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTasksMockMvc;

    private Tasks tasks;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tasks createEntity(EntityManager em) {
        Tasks tasks = new Tasks().taskId(DEFAULT_TASK_ID).taskName(DEFAULT_TASK_NAME).taskStatus(DEFAULT_TASK_STATUS);
        return tasks;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tasks createUpdatedEntity(EntityManager em) {
        Tasks tasks = new Tasks().taskId(UPDATED_TASK_ID).taskName(UPDATED_TASK_NAME).taskStatus(UPDATED_TASK_STATUS);
        return tasks;
    }

    @BeforeEach
    public void initTest() {
        tasks = createEntity(em);
    }

    @Test
    @Transactional
    void createTasks() throws Exception {
        int databaseSizeBeforeCreate = tasksRepository.findAll().size();
        // Create the Tasks
        restTasksMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tasks)))
            .andExpect(status().isCreated());

        // Validate the Tasks in the database
        List<Tasks> tasksList = tasksRepository.findAll();
        assertThat(tasksList).hasSize(databaseSizeBeforeCreate + 1);
        Tasks testTasks = tasksList.get(tasksList.size() - 1);
        assertThat(testTasks.getTaskId()).isEqualTo(DEFAULT_TASK_ID);
        assertThat(testTasks.getTaskName()).isEqualTo(DEFAULT_TASK_NAME);
        assertThat(testTasks.getTaskStatus()).isEqualTo(DEFAULT_TASK_STATUS);
    }

    @Test
    @Transactional
    void createTasksWithExistingId() throws Exception {
        // Create the Tasks with an existing ID
        tasks.setId(1L);

        int databaseSizeBeforeCreate = tasksRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTasksMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tasks)))
            .andExpect(status().isBadRequest());

        // Validate the Tasks in the database
        List<Tasks> tasksList = tasksRepository.findAll();
        assertThat(tasksList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTasks() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList
        restTasksMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tasks.getId().intValue())))
            .andExpect(jsonPath("$.[*].taskId").value(hasItem(DEFAULT_TASK_ID)))
            .andExpect(jsonPath("$.[*].taskName").value(hasItem(DEFAULT_TASK_NAME)))
            .andExpect(jsonPath("$.[*].taskStatus").value(hasItem(DEFAULT_TASK_STATUS)));
    }

    @Test
    @Transactional
    void getTasks() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get the tasks
        restTasksMockMvc
            .perform(get(ENTITY_API_URL_ID, tasks.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tasks.getId().intValue()))
            .andExpect(jsonPath("$.taskId").value(DEFAULT_TASK_ID))
            .andExpect(jsonPath("$.taskName").value(DEFAULT_TASK_NAME))
            .andExpect(jsonPath("$.taskStatus").value(DEFAULT_TASK_STATUS));
    }

    @Test
    @Transactional
    void getNonExistingTasks() throws Exception {
        // Get the tasks
        restTasksMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTasks() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        int databaseSizeBeforeUpdate = tasksRepository.findAll().size();

        // Update the tasks
        Tasks updatedTasks = tasksRepository.findById(tasks.getId()).get();
        // Disconnect from session so that the updates on updatedTasks are not directly saved in db
        em.detach(updatedTasks);
        updatedTasks.taskId(UPDATED_TASK_ID).taskName(UPDATED_TASK_NAME).taskStatus(UPDATED_TASK_STATUS);

        restTasksMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTasks.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTasks))
            )
            .andExpect(status().isOk());

        // Validate the Tasks in the database
        List<Tasks> tasksList = tasksRepository.findAll();
        assertThat(tasksList).hasSize(databaseSizeBeforeUpdate);
        Tasks testTasks = tasksList.get(tasksList.size() - 1);
        assertThat(testTasks.getTaskId()).isEqualTo(UPDATED_TASK_ID);
        assertThat(testTasks.getTaskName()).isEqualTo(UPDATED_TASK_NAME);
        assertThat(testTasks.getTaskStatus()).isEqualTo(UPDATED_TASK_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingTasks() throws Exception {
        int databaseSizeBeforeUpdate = tasksRepository.findAll().size();
        tasks.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTasksMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tasks.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tasks))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tasks in the database
        List<Tasks> tasksList = tasksRepository.findAll();
        assertThat(tasksList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTasks() throws Exception {
        int databaseSizeBeforeUpdate = tasksRepository.findAll().size();
        tasks.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTasksMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tasks))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tasks in the database
        List<Tasks> tasksList = tasksRepository.findAll();
        assertThat(tasksList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTasks() throws Exception {
        int databaseSizeBeforeUpdate = tasksRepository.findAll().size();
        tasks.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTasksMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tasks)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tasks in the database
        List<Tasks> tasksList = tasksRepository.findAll();
        assertThat(tasksList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTasksWithPatch() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        int databaseSizeBeforeUpdate = tasksRepository.findAll().size();

        // Update the tasks using partial update
        Tasks partialUpdatedTasks = new Tasks();
        partialUpdatedTasks.setId(tasks.getId());

        partialUpdatedTasks.taskName(UPDATED_TASK_NAME).taskStatus(UPDATED_TASK_STATUS);

        restTasksMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTasks.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTasks))
            )
            .andExpect(status().isOk());

        // Validate the Tasks in the database
        List<Tasks> tasksList = tasksRepository.findAll();
        assertThat(tasksList).hasSize(databaseSizeBeforeUpdate);
        Tasks testTasks = tasksList.get(tasksList.size() - 1);
        assertThat(testTasks.getTaskId()).isEqualTo(DEFAULT_TASK_ID);
        assertThat(testTasks.getTaskName()).isEqualTo(UPDATED_TASK_NAME);
        assertThat(testTasks.getTaskStatus()).isEqualTo(UPDATED_TASK_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateTasksWithPatch() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        int databaseSizeBeforeUpdate = tasksRepository.findAll().size();

        // Update the tasks using partial update
        Tasks partialUpdatedTasks = new Tasks();
        partialUpdatedTasks.setId(tasks.getId());

        partialUpdatedTasks.taskId(UPDATED_TASK_ID).taskName(UPDATED_TASK_NAME).taskStatus(UPDATED_TASK_STATUS);

        restTasksMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTasks.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTasks))
            )
            .andExpect(status().isOk());

        // Validate the Tasks in the database
        List<Tasks> tasksList = tasksRepository.findAll();
        assertThat(tasksList).hasSize(databaseSizeBeforeUpdate);
        Tasks testTasks = tasksList.get(tasksList.size() - 1);
        assertThat(testTasks.getTaskId()).isEqualTo(UPDATED_TASK_ID);
        assertThat(testTasks.getTaskName()).isEqualTo(UPDATED_TASK_NAME);
        assertThat(testTasks.getTaskStatus()).isEqualTo(UPDATED_TASK_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingTasks() throws Exception {
        int databaseSizeBeforeUpdate = tasksRepository.findAll().size();
        tasks.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTasksMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tasks.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tasks))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tasks in the database
        List<Tasks> tasksList = tasksRepository.findAll();
        assertThat(tasksList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTasks() throws Exception {
        int databaseSizeBeforeUpdate = tasksRepository.findAll().size();
        tasks.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTasksMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tasks))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tasks in the database
        List<Tasks> tasksList = tasksRepository.findAll();
        assertThat(tasksList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTasks() throws Exception {
        int databaseSizeBeforeUpdate = tasksRepository.findAll().size();
        tasks.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTasksMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tasks)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tasks in the database
        List<Tasks> tasksList = tasksRepository.findAll();
        assertThat(tasksList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTasks() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        int databaseSizeBeforeDelete = tasksRepository.findAll().size();

        // Delete the tasks
        restTasksMockMvc
            .perform(delete(ENTITY_API_URL_ID, tasks.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Tasks> tasksList = tasksRepository.findAll();
        assertThat(tasksList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
