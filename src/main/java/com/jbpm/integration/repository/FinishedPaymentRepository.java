package com.jbpm.integration.repository;

import com.jbpm.integration.domain.FinishedPayment;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FinishedPayment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FinishedPaymentRepository extends JpaRepository<FinishedPayment, Long> {}
