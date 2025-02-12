package com.dm.learning.entities.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    /**
     * Point in time when the entry was created.
     */
    @CreationTimestamp
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Column(nullable = false, updatable = false)
    protected Instant created;

    /**
     * Point in time when the entry was updated.
     */
    @UpdateTimestamp
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Column(nullable = false, updatable = false)
    protected Instant updated;

    /**
     * ID of the user which created the entry.
     */
    @CreatedBy
    @Column(nullable = false, updatable = false)
    private String createdBy;

    /**
     * ID of the user which updated the entry.
     */
    @LastModifiedBy
    @Column(nullable = false, updatable = false)
    private String updatedBy;
}
