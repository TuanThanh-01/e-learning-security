package com.ptit.elearningsecurity.data.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class TagTotalCompleteChallengeCTF {
    private String tag;
    private Long totalCompleted;

    public TagTotalCompleteChallengeCTF(String tag, Long totalCompleted) {
        this.tag = tag;
        this.totalCompleted = totalCompleted;
    }
}
