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
public abstract class BaseDto {

    private Instant created;
    private Instant updated;
    private String createdBy;
    private String updatedBy;
}
