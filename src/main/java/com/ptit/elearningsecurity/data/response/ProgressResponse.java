package com.ptit.elearningsecurity.data.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.util.List;

@Data
@Accessors(chain = true)
public class ProgressResponse {
    private Long id;
    @JsonProperty("total_completed_lessons")
    private Integer totalCompletedLessons;
    @JsonProperty("created_at")
    private Instant createdAt;
    @JsonProperty("updated_at")
    private Instant updatedAt;
    @JsonProperty("user")
    private UserResponse userResponse;
    @JsonProperty("lesson_finished")
    private List<String> lessonFinished;
}
