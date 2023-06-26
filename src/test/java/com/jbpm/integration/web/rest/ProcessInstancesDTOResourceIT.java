package com.jbpm.integration.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.jbpm.integration.IntegrationTest;
import com.jbpm.integration.domain.ProcessInstances;
import com.jbpm.integration.repository.ProcessInstancesRepository;
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
 * Integration tests for the {@link ProcessInstancesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProcessInstancesDTOResourceIT {

    private static final Integer DEFAULT_PROCESS_INSTANCE_ID = 1;
    private static final Integer UPDATED_PROCESS_INSTANCE_ID = 2;

    private static final String DEFAULT_PROCESS_ID = "AAAAAAAAAA";
    private static final String UPDATED_PROCESS_ID = "BBBBBBBBBB";

    private static final String DEFAULT_PROCESS_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PROCESS_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/process-instances";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProcessInstancesRepository processInstancesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProcessInstancesMockMvc;

    private ProcessInstances processInstances;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProcessInstances createEntity(EntityManager em) {
        ProcessInstances processInstances = new ProcessInstances()
            .processInstanceId(DEFAULT_PROCESS_INSTANCE_ID)
            .processId(DEFAULT_PROCESS_ID)
            .processName(DEFAULT_PROCESS_NAME);
        return processInstances;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProcessInstances createUpdatedEntity(EntityManager em) {
        ProcessInstances processInstances = new ProcessInstances()
            .processInstanceId(UPDATED_PROCESS_INSTANCE_ID)
            .processId(UPDATED_PROCESS_ID)
            .processName(UPDATED_PROCESS_NAME);
        return processInstances;
    }

    @BeforeEach
    public void initTest() {
        processInstances = createEntity(em);
    }

    @Test
    @Transactional
    void createProcessInstances() throws Exception {
        int databaseSizeBeforeCreate = processInstancesRepository.findAll().size();
        // Create the ProcessInstances
        restProcessInstancesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(processInstances))
            )
            .andExpect(status().isCreated());

        // Validate the ProcessInstances in the database
        List<ProcessInstances> processInstancesList = processInstancesRepository.findAll();
        assertThat(processInstancesList).hasSize(databaseSizeBeforeCreate + 1);
        ProcessInstances testProcessInstances = processInstancesList.get(processInstancesList.size() - 1);
        assertThat(testProcessInstances.getProcessInstanceId()).isEqualTo(DEFAULT_PROCESS_INSTANCE_ID);
        assertThat(testProcessInstances.getProcessId()).isEqualTo(DEFAULT_PROCESS_ID);
        assertThat(testProcessInstances.getProcessName()).isEqualTo(DEFAULT_PROCESS_NAME);
    }

    @Test
    @Transactional
    void createProcessInstancesWithExistingId() throws Exception {
        // Create the ProcessInstances with an existing ID
        processInstances.setId(1L);

        int databaseSizeBeforeCreate = processInstancesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProcessInstancesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(processInstances))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProcessInstances in the database
        List<ProcessInstances> processInstancesList = processInstancesRepository.findAll();
        assertThat(processInstancesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProcessInstances() throws Exception {
        // Initialize the database
        processInstancesRepository.saveAndFlush(processInstances);

        // Get all the processInstancesList
        restProcessInstancesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(processInstances.getId().intValue())))
            .andExpect(jsonPath("$.[*].processInstanceId").value(hasItem(DEFAULT_PROCESS_INSTANCE_ID)))
            .andExpect(jsonPath("$.[*].processId").value(hasItem(DEFAULT_PROCESS_ID)))
            .andExpect(jsonPath("$.[*].processName").value(hasItem(DEFAULT_PROCESS_NAME)));
    }

    @Test
    @Transactional
    void getProcessInstances() throws Exception {
        // Initialize the database
        processInstancesRepository.saveAndFlush(processInstances);

        // Get the processInstances
        restProcessInstancesMockMvc
            .perform(get(ENTITY_API_URL_ID, processInstances.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(processInstances.getId().intValue()))
            .andExpect(jsonPath("$.processInstanceId").value(DEFAULT_PROCESS_INSTANCE_ID))
            .andExpect(jsonPath("$.processId").value(DEFAULT_PROCESS_ID))
            .andExpect(jsonPath("$.processName").value(DEFAULT_PROCESS_NAME));
    }

    @Test
    @Transactional
    void getNonExistingProcessInstances() throws Exception {
        // Get the processInstances
        restProcessInstancesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProcessInstances() throws Exception {
        // Initialize the database
        processInstancesRepository.saveAndFlush(processInstances);

        int databaseSizeBeforeUpdate = processInstancesRepository.findAll().size();

        // Update the processInstances
        ProcessInstances updatedProcessInstances = processInstancesRepository.findById(processInstances.getId()).get();
        // Disconnect from session so that the updates on updatedProcessInstances are not directly saved in db
        em.detach(updatedProcessInstances);
        updatedProcessInstances
            .processInstanceId(UPDATED_PROCESS_INSTANCE_ID)
            .processId(UPDATED_PROCESS_ID)
            .processName(UPDATED_PROCESS_NAME);

        restProcessInstancesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProcessInstances.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedProcessInstances))
            )
            .andExpect(status().isOk());

        // Validate the ProcessInstances in the database
        List<ProcessInstances> processInstancesList = processInstancesRepository.findAll();
        assertThat(processInstancesList).hasSize(databaseSizeBeforeUpdate);
        ProcessInstances testProcessInstances = processInstancesList.get(processInstancesList.size() - 1);
        assertThat(testProcessInstances.getProcessInstanceId()).isEqualTo(UPDATED_PROCESS_INSTANCE_ID);
        assertThat(testProcessInstances.getProcessId()).isEqualTo(UPDATED_PROCESS_ID);
        assertThat(testProcessInstances.getProcessName()).isEqualTo(UPDATED_PROCESS_NAME);
    }

    @Test
    @Transactional
    void putNonExistingProcessInstances() throws Exception {
        int databaseSizeBeforeUpdate = processInstancesRepository.findAll().size();
        processInstances.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProcessInstancesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, processInstances.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(processInstances))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProcessInstances in the database
        List<ProcessInstances> processInstancesList = processInstancesRepository.findAll();
        assertThat(processInstancesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProcessInstances() throws Exception {
        int databaseSizeBeforeUpdate = processInstancesRepository.findAll().size();
        processInstances.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcessInstancesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(processInstances))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProcessInstances in the database
        List<ProcessInstances> processInstancesList = processInstancesRepository.findAll();
        assertThat(processInstancesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProcessInstances() throws Exception {
        int databaseSizeBeforeUpdate = processInstancesRepository.findAll().size();
        processInstances.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcessInstancesMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(processInstances))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProcessInstances in the database
        List<ProcessInstances> processInstancesList = processInstancesRepository.findAll();
        assertThat(processInstancesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProcessInstancesWithPatch() throws Exception {
        // Initialize the database
        processInstancesRepository.saveAndFlush(processInstances);

        int databaseSizeBeforeUpdate = processInstancesRepository.findAll().size();

        // Update the processInstances using partial update
        ProcessInstances partialUpdatedProcessInstances = new ProcessInstances();
        partialUpdatedProcessInstances.setId(processInstances.getId());

        partialUpdatedProcessInstances.processInstanceId(UPDATED_PROCESS_INSTANCE_ID).processId(UPDATED_PROCESS_ID);

        restProcessInstancesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProcessInstances.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProcessInstances))
            )
            .andExpect(status().isOk());

        // Validate the ProcessInstances in the database
        List<ProcessInstances> processInstancesList = processInstancesRepository.findAll();
        assertThat(processInstancesList).hasSize(databaseSizeBeforeUpdate);
        ProcessInstances testProcessInstances = processInstancesList.get(processInstancesList.size() - 1);
        assertThat(testProcessInstances.getProcessInstanceId()).isEqualTo(UPDATED_PROCESS_INSTANCE_ID);
        assertThat(testProcessInstances.getProcessId()).isEqualTo(UPDATED_PROCESS_ID);
        assertThat(testProcessInstances.getProcessName()).isEqualTo(DEFAULT_PROCESS_NAME);
    }

    @Test
    @Transactional
    void fullUpdateProcessInstancesWithPatch() throws Exception {
        // Initialize the database
        processInstancesRepository.saveAndFlush(processInstances);

        int databaseSizeBeforeUpdate = processInstancesRepository.findAll().size();

        // Update the processInstances using partial update
        ProcessInstances partialUpdatedProcessInstances = new ProcessInstances();
        partialUpdatedProcessInstances.setId(processInstances.getId());

        partialUpdatedProcessInstances
            .processInstanceId(UPDATED_PROCESS_INSTANCE_ID)
            .processId(UPDATED_PROCESS_ID)
            .processName(UPDATED_PROCESS_NAME);

        restProcessInstancesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProcessInstances.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProcessInstances))
            )
            .andExpect(status().isOk());

        // Validate the ProcessInstances in the database
        List<ProcessInstances> processInstancesList = processInstancesRepository.findAll();
        assertThat(processInstancesList).hasSize(databaseSizeBeforeUpdate);
        ProcessInstances testProcessInstances = processInstancesList.get(processInstancesList.size() - 1);
        assertThat(testProcessInstances.getProcessInstanceId()).isEqualTo(UPDATED_PROCESS_INSTANCE_ID);
        assertThat(testProcessInstances.getProcessId()).isEqualTo(UPDATED_PROCESS_ID);
        assertThat(testProcessInstances.getProcessName()).isEqualTo(UPDATED_PROCESS_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingProcessInstances() throws Exception {
        int databaseSizeBeforeUpdate = processInstancesRepository.findAll().size();
        processInstances.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProcessInstancesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, processInstances.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(processInstances))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProcessInstances in the database
        List<ProcessInstances> processInstancesList = processInstancesRepository.findAll();
        assertThat(processInstancesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProcessInstances() throws Exception {
        int databaseSizeBeforeUpdate = processInstancesRepository.findAll().size();
        processInstances.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcessInstancesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(processInstances))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProcessInstances in the database
        List<ProcessInstances> processInstancesList = processInstancesRepository.findAll();
        assertThat(processInstancesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProcessInstances() throws Exception {
        int databaseSizeBeforeUpdate = processInstancesRepository.findAll().size();
        processInstances.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcessInstancesMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(processInstances))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProcessInstances in the database
        List<ProcessInstances> processInstancesList = processInstancesRepository.findAll();
        assertThat(processInstancesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProcessInstances() throws Exception {
        // Initialize the database
        processInstancesRepository.saveAndFlush(processInstances);

        int databaseSizeBeforeDelete = processInstancesRepository.findAll().size();

        // Delete the processInstances
        restProcessInstancesMockMvc
            .perform(delete(ENTITY_API_URL_ID, processInstances.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProcessInstances> processInstancesList = processInstancesRepository.findAll();
        assertThat(processInstancesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
