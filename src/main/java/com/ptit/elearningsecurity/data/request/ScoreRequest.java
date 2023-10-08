package com.ptit.elearningsecurity.data.request;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ScoreRequest {
    private Integer score;
    private Integer userId;
    private Integer quizId;
}
