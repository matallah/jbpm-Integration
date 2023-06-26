package com.jbpm.integration.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.jbpm.integration.IntegrationTest;
import com.jbpm.integration.domain.OngoingPayment;
import com.jbpm.integration.repository.OngoingPaymentRepository;
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
 * Integration tests for the {@link OngoingPaymentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OngoingPaymentResourceIT {

    private static final Float DEFAULT_PRICE = 1F;
    private static final Float UPDATED_PRICE = 2F;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ongoing-payments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OngoingPaymentRepository ongoingPaymentRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOngoingPaymentMockMvc;

    private OngoingPayment ongoingPayment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OngoingPayment createEntity(EntityManager em) {
        OngoingPayment ongoingPayment = new OngoingPayment().price(DEFAULT_PRICE).name(DEFAULT_NAME);
        return ongoingPayment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OngoingPayment createUpdatedEntity(EntityManager em) {
        OngoingPayment ongoingPayment = new OngoingPayment().price(UPDATED_PRICE).name(UPDATED_NAME);
        return ongoingPayment;
    }

    @BeforeEach
    public void initTest() {
        ongoingPayment = createEntity(em);
    }

    @Test
    @Transactional
    void createOngoingPayment() throws Exception {
        int databaseSizeBeforeCreate = ongoingPaymentRepository.findAll().size();
        // Create the OngoingPayment
        restOngoingPaymentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ongoingPayment))
            )
            .andExpect(status().isCreated());

        // Validate the OngoingPayment in the database
        List<OngoingPayment> ongoingPaymentList = ongoingPaymentRepository.findAll();
        assertThat(ongoingPaymentList).hasSize(databaseSizeBeforeCreate + 1);
        OngoingPayment testOngoingPayment = ongoingPaymentList.get(ongoingPaymentList.size() - 1);
        assertThat(testOngoingPayment.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testOngoingPayment.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createOngoingPaymentWithExistingId() throws Exception {
        // Create the OngoingPayment with an existing ID
        ongoingPayment.setId(1L);

        int databaseSizeBeforeCreate = ongoingPaymentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOngoingPaymentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ongoingPayment))
            )
            .andExpect(status().isBadRequest());

        // Validate the OngoingPayment in the database
        List<OngoingPayment> ongoingPaymentList = ongoingPaymentRepository.findAll();
        assertThat(ongoingPaymentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOngoingPayments() throws Exception {
        // Initialize the database
        ongoingPaymentRepository.saveAndFlush(ongoingPayment);

        // Get all the ongoingPaymentList
        restOngoingPaymentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ongoingPayment.getId().intValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getOngoingPayment() throws Exception {
        // Initialize the database
        ongoingPaymentRepository.saveAndFlush(ongoingPayment);

        // Get the ongoingPayment
        restOngoingPaymentMockMvc
            .perform(get(ENTITY_API_URL_ID, ongoingPayment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ongoingPayment.getId().intValue()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingOngoingPayment() throws Exception {
        // Get the ongoingPayment
        restOngoingPaymentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOngoingPayment() throws Exception {
        // Initialize the database
        ongoingPaymentRepository.saveAndFlush(ongoingPayment);

        int databaseSizeBeforeUpdate = ongoingPaymentRepository.findAll().size();

        // Update the ongoingPayment
        OngoingPayment updatedOngoingPayment = ongoingPaymentRepository.findById(ongoingPayment.getId()).get();
        // Disconnect from session so that the updates on updatedOngoingPayment are not directly saved in db
        em.detach(updatedOngoingPayment);
        updatedOngoingPayment.price(UPDATED_PRICE).name(UPDATED_NAME);

        restOngoingPaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOngoingPayment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOngoingPayment))
            )
            .andExpect(status().isOk());

        // Validate the OngoingPayment in the database
        List<OngoingPayment> ongoingPaymentList = ongoingPaymentRepository.findAll();
        assertThat(ongoingPaymentList).hasSize(databaseSizeBeforeUpdate);
        OngoingPayment testOngoingPayment = ongoingPaymentList.get(ongoingPaymentList.size() - 1);
        assertThat(testOngoingPayment.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testOngoingPayment.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingOngoingPayment() throws Exception {
        int databaseSizeBeforeUpdate = ongoingPaymentRepository.findAll().size();
        ongoingPayment.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOngoingPaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ongoingPayment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ongoingPayment))
            )
            .andExpect(status().isBadRequest());

        // Validate the OngoingPayment in the database
        List<OngoingPayment> ongoingPaymentList = ongoingPaymentRepository.findAll();
        assertThat(ongoingPaymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOngoingPayment() throws Exception {
        int databaseSizeBeforeUpdate = ongoingPaymentRepository.findAll().size();
        ongoingPayment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOngoingPaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ongoingPayment))
            )
            .andExpect(status().isBadRequest());

        // Validate the OngoingPayment in the database
        List<OngoingPayment> ongoingPaymentList = ongoingPaymentRepository.findAll();
        assertThat(ongoingPaymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOngoingPayment() throws Exception {
        int databaseSizeBeforeUpdate = ongoingPaymentRepository.findAll().size();
        ongoingPayment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOngoingPaymentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ongoingPayment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OngoingPayment in the database
        List<OngoingPayment> ongoingPaymentList = ongoingPaymentRepository.findAll();
        assertThat(ongoingPaymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOngoingPaymentWithPatch() throws Exception {
        // Initialize the database
        ongoingPaymentRepository.saveAndFlush(ongoingPayment);

        int databaseSizeBeforeUpdate = ongoingPaymentRepository.findAll().size();

        // Update the ongoingPayment using partial update
        OngoingPayment partialUpdatedOngoingPayment = new OngoingPayment();
        partialUpdatedOngoingPayment.setId(ongoingPayment.getId());

        partialUpdatedOngoingPayment.name(UPDATED_NAME);

        restOngoingPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOngoingPayment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOngoingPayment))
            )
            .andExpect(status().isOk());

        // Validate the OngoingPayment in the database
        List<OngoingPayment> ongoingPaymentList = ongoingPaymentRepository.findAll();
        assertThat(ongoingPaymentList).hasSize(databaseSizeBeforeUpdate);
        OngoingPayment testOngoingPayment = ongoingPaymentList.get(ongoingPaymentList.size() - 1);
        assertThat(testOngoingPayment.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testOngoingPayment.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateOngoingPaymentWithPatch() throws Exception {
        // Initialize the database
        ongoingPaymentRepository.saveAndFlush(ongoingPayment);

        int databaseSizeBeforeUpdate = ongoingPaymentRepository.findAll().size();

        // Update the ongoingPayment using partial update
        OngoingPayment partialUpdatedOngoingPayment = new OngoingPayment();
        partialUpdatedOngoingPayment.setId(ongoingPayment.getId());

        partialUpdatedOngoingPayment.price(UPDATED_PRICE).name(UPDATED_NAME);

        restOngoingPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOngoingPayment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOngoingPayment))
            )
            .andExpect(status().isOk());

        // Validate the OngoingPayment in the database
        List<OngoingPayment> ongoingPaymentList = ongoingPaymentRepository.findAll();
        assertThat(ongoingPaymentList).hasSize(databaseSizeBeforeUpdate);
        OngoingPayment testOngoingPayment = ongoingPaymentList.get(ongoingPaymentList.size() - 1);
        assertThat(testOngoingPayment.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testOngoingPayment.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingOngoingPayment() throws Exception {
        int databaseSizeBeforeUpdate = ongoingPaymentRepository.findAll().size();
        ongoingPayment.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOngoingPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ongoingPayment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ongoingPayment))
            )
            .andExpect(status().isBadRequest());

        // Validate the OngoingPayment in the database
        List<OngoingPayment> ongoingPaymentList = ongoingPaymentRepository.findAll();
        assertThat(ongoingPaymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOngoingPayment() throws Exception {
        int databaseSizeBeforeUpdate = ongoingPaymentRepository.findAll().size();
        ongoingPayment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOngoingPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ongoingPayment))
            )
            .andExpect(status().isBadRequest());

        // Validate the OngoingPayment in the database
        List<OngoingPayment> ongoingPaymentList = ongoingPaymentRepository.findAll();
        assertThat(ongoingPaymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOngoingPayment() throws Exception {
        int databaseSizeBeforeUpdate = ongoingPaymentRepository.findAll().size();
        ongoingPayment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOngoingPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(ongoingPayment))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OngoingPayment in the database
        List<OngoingPayment> ongoingPaymentList = ongoingPaymentRepository.findAll();
        assertThat(ongoingPaymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOngoingPayment() throws Exception {
        // Initialize the database
        ongoingPaymentRepository.saveAndFlush(ongoingPayment);

        int databaseSizeBeforeDelete = ongoingPaymentRepository.findAll().size();

        // Delete the ongoingPayment
        restOngoingPaymentMockMvc
            .perform(delete(ENTITY_API_URL_ID, ongoingPayment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OngoingPayment> ongoingPaymentList = ongoingPaymentRepository.findAll();
        assertThat(ongoingPaymentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
