package com.jbpm.integration.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.jbpm.integration.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OngoingPaymentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OngoingPayment.class);
        OngoingPayment ongoingPayment1 = new OngoingPayment();
        ongoingPayment1.setId(1L);
        OngoingPayment ongoingPayment2 = new OngoingPayment();
        ongoingPayment2.setId(ongoingPayment1.getId());
        assertThat(ongoingPayment1).isEqualTo(ongoingPayment2);
        ongoingPayment2.setId(2L);
        assertThat(ongoingPayment1).isNotEqualTo(ongoingPayment2);
        ongoingPayment1.setId(null);
        assertThat(ongoingPayment1).isNotEqualTo(ongoingPayment2);
    }
}
