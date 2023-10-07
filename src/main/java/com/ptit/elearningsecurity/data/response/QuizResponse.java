package com.ptit.elearningsecurity.data.response;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.Instant;

@Data
@Accessors(chain = true)
public class QuizResponse {
    private Integer id;
    private String name;
    private String description;
    private Instant createdAt;
    private String imageCover;
}
