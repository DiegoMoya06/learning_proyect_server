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

    String testBody = "{" +
            "\"id\":\"640034b0-14de-47bc-aad1-f806305ad062\"," +
            "\"object\":\"chat.completion\"," +
            "\"created\":1741086926," +
            "\"model\":\"deepseek-chat\"," +
            "\"choices\":[" +
            "{\"index\":0," +
            "\"message\":{" +
            "\"role\":\"assistant\"," +
            "\"content\":\"Here is the structured and organized output based on your request:" +
            "\\n\\n```json\\n{\\n  \\\"generalDescription\\\": \\\"This text is a vocabulary guide for learning German, focusing on basic words and phrases related to describing people and clothing. It includes translations from German to Spanish, organized into categories such as physical appearance, clothing, and accessories. The text is designed to help learners expand their vocabulary and understand how to use these words in context.\\\"," +
            "\\n  \\\"cardsList\\\": [\\n    {\\n      \\\"title\\\": \\\"scheinen*\\\",\\n      \\\"description\\\": \\\"parecer, tener aspecto\\\",\\n      \\\"rate\\\": 1.0,\\n      \\\"probability\\\": 0.0\\n    },\\n    {\\n      \\\"title\\\": \\\"wiegen*\\\",\\n      \\\"description\\\": \\\"pesar\\\",\\n      \\\"rate\\\": 1.0,\\n      \\\"probability\\\": 0.0\\n    },\\n   {\\n      \\\"title\\\": \\\"die Latzhose (-n)\\\",\\n      \\\"description\\\": \\\"pantalón de peto\\\",\\n      \\\"rate\\\": 1.0,\\n      \\\"probability\\\": 0\"}],\"logprobs\":null,\"finish_reason\":\"length\"}" +
            "],\"usage\":{\"prompt_tokens\":5641,\"completion_tokens\":4096,\"total_tokens\":9737,\"prompt_tokens_details\":{\"cached_tokens\":128},\"prompt_cache_hit_tokens\":128,\"prompt_cache_miss_tokens\":5513},\"system_fingerprint\":\"fp_3a5770e1b4_prod0225\"}";
}

