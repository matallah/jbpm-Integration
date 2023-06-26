package com.jbpm.integration.domain;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * A FinishedPayment.
 */
@Entity
@Table(name = "finished_payment")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FinishedPayment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "price")
    private Float price;

    @Column(name = "name")
    private String name;

    @Column(name = "approve")
    private Boolean approve;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FinishedPayment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getPrice() {
        return this.price;
    }

    public FinishedPayment price(Float price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getName() {
        return this.name;
    }

    public FinishedPayment name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getApprove() {
        return this.approve;
    }

    public FinishedPayment approve(Boolean approve) {
        this.setApprove(approve);
        return this;
    }

    public void setApprove(Boolean approve) {
        this.approve = approve;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FinishedPayment)) {
            return false;
        }
        return id != null && id.equals(((FinishedPayment) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FinishedPayment{" +
            "id=" + getId() +
            ", price=" + getPrice() +
            ", name='" + getName() + "'" +
            ", approve='" + getApprove() + "'" +
            "}";
    }
}
