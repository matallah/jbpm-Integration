package com.jbpm.integration.web.rest;

import com.jbpm.integration.domain.OngoingPayment;
import com.jbpm.integration.repository.OngoingPaymentRepository;
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
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.jbpm.integration.domain.OngoingPayment}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class OngoingPaymentResource {

    private final Logger log = LoggerFactory.getLogger(OngoingPaymentResource.class);

    private static final String ENTITY_NAME = "ongoingPayment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OngoingPaymentRepository ongoingPaymentRepository;

    public OngoingPaymentResource(OngoingPaymentRepository ongoingPaymentRepository) {
        this.ongoingPaymentRepository = ongoingPaymentRepository;
    }

    /**
     * {@code POST  /ongoing-payments} : Create a new ongoingPayment.
     *
     * @param ongoingPayment the ongoingPayment to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ongoingPayment, or with status {@code 400 (Bad Request)} if the ongoingPayment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ongoing-payments")
    public ResponseEntity<OngoingPayment> createOngoingPayment(@RequestBody OngoingPayment ongoingPayment) throws URISyntaxException {
        log.debug("REST request to save OngoingPayment : {}", ongoingPayment);
        if (ongoingPayment.getId() != null) {
            throw new BadRequestAlertException("A new ongoingPayment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OngoingPayment result = ongoingPaymentRepository.save(ongoingPayment);
        return ResponseEntity
            .created(new URI("/api/ongoing-payments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ongoing-payments/:id} : Updates an existing ongoingPayment.
     *
     * @param id the id of the ongoingPayment to save.
     * @param ongoingPayment the ongoingPayment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ongoingPayment,
     * or with status {@code 400 (Bad Request)} if the ongoingPayment is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ongoingPayment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ongoing-payments/{id}")
    public ResponseEntity<OngoingPayment> updateOngoingPayment(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OngoingPayment ongoingPayment
    ) throws URISyntaxException {
        log.debug("REST request to update OngoingPayment : {}, {}", id, ongoingPayment);
        if (ongoingPayment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ongoingPayment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ongoingPaymentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OngoingPayment result = ongoingPaymentRepository.save(ongoingPayment);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ongoingPayment.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ongoing-payments/:id} : Partial updates given fields of an existing ongoingPayment, field will ignore if it is null
     *
     * @param id the id of the ongoingPayment to save.
     * @param ongoingPayment the ongoingPayment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ongoingPayment,
     * or with status {@code 400 (Bad Request)} if the ongoingPayment is not valid,
     * or with status {@code 404 (Not Found)} if the ongoingPayment is not found,
     * or with status {@code 500 (Internal Server Error)} if the ongoingPayment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ongoing-payments/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OngoingPayment> partialUpdateOngoingPayment(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OngoingPayment ongoingPayment
    ) throws URISyntaxException {
        log.debug("REST request to partial update OngoingPayment partially : {}, {}", id, ongoingPayment);
        if (ongoingPayment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ongoingPayment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ongoingPaymentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OngoingPayment> result = ongoingPaymentRepository
            .findById(ongoingPayment.getId())
            .map(existingOngoingPayment -> {
                if (ongoingPayment.getPrice() != null) {
                    existingOngoingPayment.setPrice(ongoingPayment.getPrice());
                }
                if (ongoingPayment.getName() != null) {
                    existingOngoingPayment.setName(ongoingPayment.getName());
                }

                return existingOngoingPayment;
            })
            .map(ongoingPaymentRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ongoingPayment.getId().toString())
        );
    }

    /**
     * {@code GET  /ongoing-payments} : get all the ongoingPayments.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ongoingPayments in body.
     */
    @GetMapping("/ongoing-payments")
    public List<OngoingPayment> getAllOngoingPayments() {
        log.debug("REST request to get all OngoingPayments");
        return ongoingPaymentRepository.findAll();
    }

    /**
     * {@code GET  /ongoing-payments/:id} : get the "id" ongoingPayment.
     *
     * @param id the id of the ongoingPayment to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ongoingPayment, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ongoing-payments/{id}")
    public ResponseEntity<OngoingPayment> getOngoingPayment(@PathVariable Long id) {
        log.debug("REST request to get OngoingPayment : {}", id);
        Optional<OngoingPayment> ongoingPayment = ongoingPaymentRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(ongoingPayment);
    }

    /**
     * {@code DELETE  /ongoing-payments/:id} : delete the "id" ongoingPayment.
     *
     * @param id the id of the ongoingPayment to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ongoing-payments/{id}")
    public ResponseEntity<Void> deleteOngoingPayment(@PathVariable Long id) {
        log.debug("REST request to delete OngoingPayment : {}", id);
        ongoingPaymentRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
