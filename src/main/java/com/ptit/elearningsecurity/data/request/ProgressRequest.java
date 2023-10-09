package com.ptit.elearningsecurity.data.request;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ProgressRequest {
    private Integer userId;
    private Integer lessonId;
}
