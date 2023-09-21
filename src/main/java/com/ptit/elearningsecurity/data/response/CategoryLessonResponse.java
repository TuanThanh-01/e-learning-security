package com.ptit.elearningsecurity.data.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CategoryLessonResponse {
    private int id;
    @JsonProperty("category_name")
    private String categoryName;
    private String description;
}
