package com.jbpm.integration.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.jbpm.integration.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProcessInstancesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProcessInstances.class);
        ProcessInstances processInstances1 = new ProcessInstances();
        processInstances1.setId(1L);
        ProcessInstances processInstances2 = new ProcessInstances();
        processInstances2.setId(processInstances1.getId());
        assertThat(processInstances1).isEqualTo(processInstances2);
        processInstances2.setId(2L);
        assertThat(processInstances1).isNotEqualTo(processInstances2);
        processInstances1.setId(null);
        assertThat(processInstances1).isNotEqualTo(processInstances2);
    }
}
