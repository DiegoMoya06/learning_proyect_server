package com.dm.learning.entities.base;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BaseEntity {

    /**
     * Point in time when the entry was created.
     */
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    protected Instant created;

    /**
     * Point in time when the entry was updated.
     */
    @UpdateTimestamp
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
    @CreatedBy
    @Column(nullable = false, updatable = false)
    private String updatedBy;
}
