package com.jbpm.integration.repository;

import com.jbpm.integration.domain.Tasks;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Tasks entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TasksRepository extends JpaRepository<Tasks, Long> {}
