package com.jbpm.integration.domain;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * A ProcessInstances.
 */
@Entity
@Table(name = "process_instances")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProcessInstances implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "process_instance_id")
    private Integer processInstanceId;

    @Column(name = "process_id")
    private String processId;

    @Column(name = "process_name")
    private String processName;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProcessInstances id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getProcessInstanceId() {
        return this.processInstanceId;
    }

    public ProcessInstances processInstanceId(Integer processInstanceId) {
        this.setProcessInstanceId(processInstanceId);
        return this;
    }

    public void setProcessInstanceId(Integer processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getProcessId() {
        return this.processId;
    }

    public ProcessInstances processId(String processId) {
        this.setProcessId(processId);
        return this;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getProcessName() {
        return this.processName;
    }

    public ProcessInstances processName(String processName) {
        this.setProcessName(processName);
        return this;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProcessInstances)) {
            return false;
        }
        return id != null && id.equals(((ProcessInstances) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProcessInstances{" +
            "id=" + getId() +
            ", processInstanceId=" + getProcessInstanceId() +
            ", processId='" + getProcessId() + "'" +
            ", processName='" + getProcessName() + "'" +
            "}";
    }
}
