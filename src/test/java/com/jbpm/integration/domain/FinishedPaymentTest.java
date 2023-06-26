package com.jbpm.integration.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.jbpm.integration.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FinishedPaymentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FinishedPayment.class);
        FinishedPayment finishedPayment1 = new FinishedPayment();
        finishedPayment1.setId(1L);
        FinishedPayment finishedPayment2 = new FinishedPayment();
        finishedPayment2.setId(finishedPayment1.getId());
        assertThat(finishedPayment1).isEqualTo(finishedPayment2);
        finishedPayment2.setId(2L);
        assertThat(finishedPayment1).isNotEqualTo(finishedPayment2);
        finishedPayment1.setId(null);
        assertThat(finishedPayment1).isNotEqualTo(finishedPayment2);
    }
}
