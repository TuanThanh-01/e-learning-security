package com.ptit.elearningsecurity.data.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class CommentResponse {
    private int id;
    private String context;
    private int parentID;
    @JsonProperty("user_comment")
    private UserResponse userResponse;
    @JsonProperty("post_id")
    private int postID;
    @JsonProperty("image_url")
    private List<String> imageUrl;
}
