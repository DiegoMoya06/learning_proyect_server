package com.dm.learning.dto.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//public abstract class BaseDto implements Dto{
public abstract class BaseDto {
    private Instant created;
    private Instant updated;
    private String createdBy;
    private String updatedBy;

//    @PrePersist
//    protected void beforeCreate() {
//        this.created = Instant.now();
//        this.updated = Instant.now();
//        this.createdBy = "";
//        this.updatedBy = "";
//    }
//
//    @PreUpdate
//    protected void beforeUpdate() {
//        this.updated = Instant.now();
//        this.updatedBy = "";
//    }
}
