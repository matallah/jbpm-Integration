package com.jbpm.integration.repository;

import com.jbpm.integration.domain.ProcessInstances;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProcessInstances entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProcessInstancesRepository extends JpaRepository<ProcessInstances, Long> {}
