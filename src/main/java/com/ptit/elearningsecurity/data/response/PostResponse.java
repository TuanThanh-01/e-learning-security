package com.ptit.elearningsecurity.data.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.util.List;

@Data
@Accessors(chain = true)
public class PostResponse {
    private int id;
    private String title;
    private String content;
    private List<TopicResponse> topic;
    @JsonProperty("user_create")
    private UserResponse userCreate;
    @JsonProperty("image_url")
    private String imageUrl;
    @JsonProperty("created_at")
    private Instant createdAt;
    @JsonProperty("updated_at")
    private Instant updatedAt;
}
