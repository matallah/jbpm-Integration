package com.jbpm.integration.web.rest;

import com.jbpm.integration.domain.FinishedPayment;
import com.jbpm.integration.repository.FinishedPaymentRepository;
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
 * REST controller for managing {@link com.jbpm.integration.domain.FinishedPayment}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class FinishedPaymentResource {

    private final Logger log = LoggerFactory.getLogger(FinishedPaymentResource.class);

    private static final String ENTITY_NAME = "finishedPayment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FinishedPaymentRepository finishedPaymentRepository;

    public FinishedPaymentResource(FinishedPaymentRepository finishedPaymentRepository) {
        this.finishedPaymentRepository = finishedPaymentRepository;
    }

    /**
     * {@code POST  /finished-payments} : Create a new finishedPayment.
     *
     * @param finishedPayment the finishedPayment to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new finishedPayment, or with status {@code 400 (Bad Request)} if the finishedPayment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/finished-payments")
    public ResponseEntity<FinishedPayment> createFinishedPayment(@RequestBody FinishedPayment finishedPayment) throws URISyntaxException {
        log.debug("REST request to save FinishedPayment : {}", finishedPayment);
        if (finishedPayment.getId() != null) {
            throw new BadRequestAlertException("A new finishedPayment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FinishedPayment result = finishedPaymentRepository.save(finishedPayment);
        return ResponseEntity
            .created(new URI("/api/finished-payments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /finished-payments/:id} : Updates an existing finishedPayment.
     *
     * @param id the id of the finishedPayment to save.
     * @param finishedPayment the finishedPayment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated finishedPayment,
     * or with status {@code 400 (Bad Request)} if the finishedPayment is not valid,
     * or with status {@code 500 (Internal Server Error)} if the finishedPayment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/finished-payments/{id}")
    public ResponseEntity<FinishedPayment> updateFinishedPayment(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FinishedPayment finishedPayment
    ) throws URISyntaxException {
        log.debug("REST request to update FinishedPayment : {}, {}", id, finishedPayment);
        if (finishedPayment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, finishedPayment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!finishedPaymentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FinishedPayment result = finishedPaymentRepository.save(finishedPayment);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, finishedPayment.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /finished-payments/:id} : Partial updates given fields of an existing finishedPayment, field will ignore if it is null
     *
     * @param id the id of the finishedPayment to save.
     * @param finishedPayment the finishedPayment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated finishedPayment,
     * or with status {@code 400 (Bad Request)} if the finishedPayment is not valid,
     * or with status {@code 404 (Not Found)} if the finishedPayment is not found,
     * or with status {@code 500 (Internal Server Error)} if the finishedPayment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/finished-payments/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FinishedPayment> partialUpdateFinishedPayment(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FinishedPayment finishedPayment
    ) throws URISyntaxException {
        log.debug("REST request to partial update FinishedPayment partially : {}, {}", id, finishedPayment);
        if (finishedPayment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, finishedPayment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!finishedPaymentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FinishedPayment> result = finishedPaymentRepository
            .findById(finishedPayment.getId())
            .map(existingFinishedPayment -> {
                if (finishedPayment.getPrice() != null) {
                    existingFinishedPayment.setPrice(finishedPayment.getPrice());
                }
                if (finishedPayment.getName() != null) {
                    existingFinishedPayment.setName(finishedPayment.getName());
                }
                if (finishedPayment.getApprove() != null) {
                    existingFinishedPayment.setApprove(finishedPayment.getApprove());
                }

                return existingFinishedPayment;
            })
            .map(finishedPaymentRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, finishedPayment.getId().toString())
        );
    }

    /**
     * {@code GET  /finished-payments} : get all the finishedPayments.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of finishedPayments in body.
     */
    @GetMapping("/finished-payments")
    public List<FinishedPayment> getAllFinishedPayments() {
        log.debug("REST request to get all FinishedPayments");
        return finishedPaymentRepository.findAll();
    }

    /**
     * {@code GET  /finished-payments/:id} : get the "id" finishedPayment.
     *
     * @param id the id of the finishedPayment to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the finishedPayment, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/finished-payments/{id}")
    public ResponseEntity<FinishedPayment> getFinishedPayment(@PathVariable Long id) {
        log.debug("REST request to get FinishedPayment : {}", id);
        Optional<FinishedPayment> finishedPayment = finishedPaymentRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(finishedPayment);
    }

    /**
     * {@code DELETE  /finished-payments/:id} : delete the "id" finishedPayment.
     *
     * @param id the id of the finishedPayment to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/finished-payments/{id}")
    public ResponseEntity<Void> deleteFinishedPayment(@PathVariable Long id) {
        log.debug("REST request to delete FinishedPayment : {}", id);
        finishedPaymentRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
