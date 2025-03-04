package com.dm.learning.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Choice {
    @JsonProperty("index")
    private Integer index;
    @JsonProperty("message")
    private ResponseMessage message;
    @JsonProperty("logprobs")
    private String logprobs;
    @JsonProperty("finish_reason")
    private String finishReason;
}

