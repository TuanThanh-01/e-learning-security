package com.ptit.elearningsecurity.data.response;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CategoryLessonResponse {
    private int id;
    private String categoryName;
    private String description;
}
