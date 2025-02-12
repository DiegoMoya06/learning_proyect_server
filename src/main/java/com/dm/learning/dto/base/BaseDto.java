package com.dm.learning.dto.base;

import com.dm.learning.entities.base.BaseEntity;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseDto {

    private Instant created;
    private Instant updated;
    private String createdBy;
    private String updatedBy;

    protected BaseDto(@NonNull BaseEntity entity) {
        this.created = entity.getCreated();
        this.updated = entity.getUpdated();
        this.createdBy = entity.getCreatedBy();
        this.updatedBy = entity.getUpdatedBy();
    }
}
