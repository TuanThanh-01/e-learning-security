package com.ptit.elearningsecurity.data.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class LessonResponse {
    private int id;
    private String title;
    private String description;
    private String content;
    @JsonProperty("cover_image_url")
    private String coverImageUrl;
    @JsonProperty("list_content_image_url")
    private List<String> contentsImagesUrl;
    @JsonProperty("category_lesson_name")
    private String categoryLessonName;
}
