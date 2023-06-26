package com.jbpm.integration.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.jbpm.integration.IntegrationTest;
import com.jbpm.integration.domain.FinishedPayment;
import com.jbpm.integration.repository.FinishedPaymentRepository;
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
 * Integration tests for the {@link FinishedPaymentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FinishedPaymentResourceIT {

    private static final Float DEFAULT_PRICE = 1F;
    private static final Float UPDATED_PRICE = 2F;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_APPROVE = false;
    private static final Boolean UPDATED_APPROVE = true;

    private static final String ENTITY_API_URL = "/api/finished-payments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FinishedPaymentRepository finishedPaymentRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFinishedPaymentMockMvc;

    private FinishedPayment finishedPayment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FinishedPayment createEntity(EntityManager em) {
        FinishedPayment finishedPayment = new FinishedPayment().price(DEFAULT_PRICE).name(DEFAULT_NAME).approve(DEFAULT_APPROVE);
        return finishedPayment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FinishedPayment createUpdatedEntity(EntityManager em) {
        FinishedPayment finishedPayment = new FinishedPayment().price(UPDATED_PRICE).name(UPDATED_NAME).approve(UPDATED_APPROVE);
        return finishedPayment;
    }

    @BeforeEach
    public void initTest() {
        finishedPayment = createEntity(em);
    }

    @Test
    @Transactional
    void createFinishedPayment() throws Exception {
        int databaseSizeBeforeCreate = finishedPaymentRepository.findAll().size();
        // Create the FinishedPayment
        restFinishedPaymentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(finishedPayment))
            )
            .andExpect(status().isCreated());

        // Validate the FinishedPayment in the database
        List<FinishedPayment> finishedPaymentList = finishedPaymentRepository.findAll();
        assertThat(finishedPaymentList).hasSize(databaseSizeBeforeCreate + 1);
        FinishedPayment testFinishedPayment = finishedPaymentList.get(finishedPaymentList.size() - 1);
        assertThat(testFinishedPayment.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testFinishedPayment.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFinishedPayment.getApprove()).isEqualTo(DEFAULT_APPROVE);
    }

    @Test
    @Transactional
    void createFinishedPaymentWithExistingId() throws Exception {
        // Create the FinishedPayment with an existing ID
        finishedPayment.setId(1L);

        int databaseSizeBeforeCreate = finishedPaymentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFinishedPaymentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(finishedPayment))
            )
            .andExpect(status().isBadRequest());

        // Validate the FinishedPayment in the database
        List<FinishedPayment> finishedPaymentList = finishedPaymentRepository.findAll();
        assertThat(finishedPaymentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFinishedPayments() throws Exception {
        // Initialize the database
        finishedPaymentRepository.saveAndFlush(finishedPayment);

        // Get all the finishedPaymentList
        restFinishedPaymentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(finishedPayment.getId().intValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].approve").value(hasItem(DEFAULT_APPROVE.booleanValue())));
    }

    @Test
    @Transactional
    void getFinishedPayment() throws Exception {
        // Initialize the database
        finishedPaymentRepository.saveAndFlush(finishedPayment);

        // Get the finishedPayment
        restFinishedPaymentMockMvc
            .perform(get(ENTITY_API_URL_ID, finishedPayment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(finishedPayment.getId().intValue()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.approve").value(DEFAULT_APPROVE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingFinishedPayment() throws Exception {
        // Get the finishedPayment
        restFinishedPaymentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFinishedPayment() throws Exception {
        // Initialize the database
        finishedPaymentRepository.saveAndFlush(finishedPayment);

        int databaseSizeBeforeUpdate = finishedPaymentRepository.findAll().size();

        // Update the finishedPayment
        FinishedPayment updatedFinishedPayment = finishedPaymentRepository.findById(finishedPayment.getId()).get();
        // Disconnect from session so that the updates on updatedFinishedPayment are not directly saved in db
        em.detach(updatedFinishedPayment);
        updatedFinishedPayment.price(UPDATED_PRICE).name(UPDATED_NAME).approve(UPDATED_APPROVE);

        restFinishedPaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFinishedPayment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFinishedPayment))
            )
            .andExpect(status().isOk());

        // Validate the FinishedPayment in the database
        List<FinishedPayment> finishedPaymentList = finishedPaymentRepository.findAll();
        assertThat(finishedPaymentList).hasSize(databaseSizeBeforeUpdate);
        FinishedPayment testFinishedPayment = finishedPaymentList.get(finishedPaymentList.size() - 1);
        assertThat(testFinishedPayment.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testFinishedPayment.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFinishedPayment.getApprove()).isEqualTo(UPDATED_APPROVE);
    }

    @Test
    @Transactional
    void putNonExistingFinishedPayment() throws Exception {
        int databaseSizeBeforeUpdate = finishedPaymentRepository.findAll().size();
        finishedPayment.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFinishedPaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, finishedPayment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(finishedPayment))
            )
            .andExpect(status().isBadRequest());

        // Validate the FinishedPayment in the database
        List<FinishedPayment> finishedPaymentList = finishedPaymentRepository.findAll();
        assertThat(finishedPaymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFinishedPayment() throws Exception {
        int databaseSizeBeforeUpdate = finishedPaymentRepository.findAll().size();
        finishedPayment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFinishedPaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(finishedPayment))
            )
            .andExpect(status().isBadRequest());

        // Validate the FinishedPayment in the database
        List<FinishedPayment> finishedPaymentList = finishedPaymentRepository.findAll();
        assertThat(finishedPaymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFinishedPayment() throws Exception {
        int databaseSizeBeforeUpdate = finishedPaymentRepository.findAll().size();
        finishedPayment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFinishedPaymentMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(finishedPayment))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FinishedPayment in the database
        List<FinishedPayment> finishedPaymentList = finishedPaymentRepository.findAll();
        assertThat(finishedPaymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFinishedPaymentWithPatch() throws Exception {
        // Initialize the database
        finishedPaymentRepository.saveAndFlush(finishedPayment);

        int databaseSizeBeforeUpdate = finishedPaymentRepository.findAll().size();

        // Update the finishedPayment using partial update
        FinishedPayment partialUpdatedFinishedPayment = new FinishedPayment();
        partialUpdatedFinishedPayment.setId(finishedPayment.getId());

        partialUpdatedFinishedPayment.price(UPDATED_PRICE).approve(UPDATED_APPROVE);

        restFinishedPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFinishedPayment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFinishedPayment))
            )
            .andExpect(status().isOk());

        // Validate the FinishedPayment in the database
        List<FinishedPayment> finishedPaymentList = finishedPaymentRepository.findAll();
        assertThat(finishedPaymentList).hasSize(databaseSizeBeforeUpdate);
        FinishedPayment testFinishedPayment = finishedPaymentList.get(finishedPaymentList.size() - 1);
        assertThat(testFinishedPayment.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testFinishedPayment.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFinishedPayment.getApprove()).isEqualTo(UPDATED_APPROVE);
    }

    @Test
    @Transactional
    void fullUpdateFinishedPaymentWithPatch() throws Exception {
        // Initialize the database
        finishedPaymentRepository.saveAndFlush(finishedPayment);

        int databaseSizeBeforeUpdate = finishedPaymentRepository.findAll().size();

        // Update the finishedPayment using partial update
        FinishedPayment partialUpdatedFinishedPayment = new FinishedPayment();
        partialUpdatedFinishedPayment.setId(finishedPayment.getId());

        partialUpdatedFinishedPayment.price(UPDATED_PRICE).name(UPDATED_NAME).approve(UPDATED_APPROVE);

        restFinishedPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFinishedPayment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFinishedPayment))
            )
            .andExpect(status().isOk());

        // Validate the FinishedPayment in the database
        List<FinishedPayment> finishedPaymentList = finishedPaymentRepository.findAll();
        assertThat(finishedPaymentList).hasSize(databaseSizeBeforeUpdate);
        FinishedPayment testFinishedPayment = finishedPaymentList.get(finishedPaymentList.size() - 1);
        assertThat(testFinishedPayment.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testFinishedPayment.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFinishedPayment.getApprove()).isEqualTo(UPDATED_APPROVE);
    }

    @Test
    @Transactional
    void patchNonExistingFinishedPayment() throws Exception {
        int databaseSizeBeforeUpdate = finishedPaymentRepository.findAll().size();
        finishedPayment.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFinishedPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, finishedPayment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(finishedPayment))
            )
            .andExpect(status().isBadRequest());

        // Validate the FinishedPayment in the database
        List<FinishedPayment> finishedPaymentList = finishedPaymentRepository.findAll();
        assertThat(finishedPaymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFinishedPayment() throws Exception {
        int databaseSizeBeforeUpdate = finishedPaymentRepository.findAll().size();
        finishedPayment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFinishedPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(finishedPayment))
            )
            .andExpect(status().isBadRequest());

        // Validate the FinishedPayment in the database
        List<FinishedPayment> finishedPaymentList = finishedPaymentRepository.findAll();
        assertThat(finishedPaymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFinishedPayment() throws Exception {
        int databaseSizeBeforeUpdate = finishedPaymentRepository.findAll().size();
        finishedPayment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFinishedPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(finishedPayment))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FinishedPayment in the database
        List<FinishedPayment> finishedPaymentList = finishedPaymentRepository.findAll();
        assertThat(finishedPaymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFinishedPayment() throws Exception {
        // Initialize the database
        finishedPaymentRepository.saveAndFlush(finishedPayment);

        int databaseSizeBeforeDelete = finishedPaymentRepository.findAll().size();

        // Delete the finishedPayment
        restFinishedPaymentMockMvc
            .perform(delete(ENTITY_API_URL_ID, finishedPayment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FinishedPayment> finishedPaymentList = finishedPaymentRepository.findAll();
        assertThat(finishedPaymentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
