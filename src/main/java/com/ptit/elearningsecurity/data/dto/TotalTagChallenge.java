package com.ptit.elearningsecurity.data.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class TotalTagChallenge {
    private String tag;
    private Long totalChallenge;

    public TotalTagChallenge(String tag, Long totalChallenge) {
        this.tag = tag;
        this.totalChallenge = totalChallenge;
    }
}
