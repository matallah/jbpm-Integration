package com.jbpm.integration.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A Tasks.
 */
@Entity
@Table(name = "tasks")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Tasks implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "task_id")
    private Integer taskId;

    @Column(name = "task_name")
    private String taskName;

    @Column(name = "task_status")
    private String taskStatus;

    @Column(name = "price", precision = 21, scale = 2)
    private BigDecimal price;

    @Column(name = "approve")
    private Boolean approve;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Tasks id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTaskId() {
        return this.taskId;
    }

    public Tasks taskId(Integer taskId) {
        this.setTaskId(taskId);
        return this;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return this.taskName;
    }

    public Tasks taskName(String taskName) {
        this.setTaskName(taskName);
        return this;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskStatus() {
        return this.taskStatus;
    }

    public Tasks taskStatus(String taskStatus) {
        this.setTaskStatus(taskStatus);
        return this;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public Tasks price(BigDecimal price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Boolean getApprove() {
        return this.approve;
    }

    public Tasks approve(Boolean approve) {
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
        if (!(o instanceof Tasks)) {
            return false;
        }
        return id != null && id.equals(((Tasks) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Tasks{" +
            "id=" + getId() +
            ", taskId=" + getTaskId() +
            ", taskName='" + getTaskName() + "'" +
            ", taskStatus='" + getTaskStatus() + "'" +
            ", price=" + getPrice() +
            ", approve='" + getApprove() + "'" +
            "}";
    }
}
