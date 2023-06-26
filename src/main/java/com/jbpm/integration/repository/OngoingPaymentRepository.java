package com.jbpm.integration.repository;

import com.jbpm.integration.domain.OngoingPayment;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the OngoingPayment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OngoingPaymentRepository extends JpaRepository<OngoingPayment, Long> {}
