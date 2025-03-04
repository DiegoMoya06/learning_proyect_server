package com.dm.learning.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DSResponse {
    @JsonProperty("id")
    private UUID id;
    @JsonProperty("object")
    private String object;
    @JsonProperty("created")
    private Timestamp created;
    @JsonProperty("model")
    private String model;
    @JsonProperty("choices")
    private List<Choice> choices;
    @JsonProperty("usage")
    private DSUsage usage;
    @JsonProperty("system_fingerprint")
    private String systemFingerprint;
}

