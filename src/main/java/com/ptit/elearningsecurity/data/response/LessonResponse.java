package com.ptit.elearningsecurity.data.response;

import com.ptit.elearningsecurity.entity.image.ImageData;
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
    private ImageData coverImage;
    private List<ImageData> contentsImages;
    private String categoryLessonName;
}
