package com.ptit.elearningsecurity.data.request;

import com.ptit.elearningsecurity.entity.image.ImageData;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class LessonRequest {
    private String title;
    private String description;
    private String content;
    private ImageData coverImage;
    private List<ImageData> contentsImages;
    private int categoryLessonID;
}
