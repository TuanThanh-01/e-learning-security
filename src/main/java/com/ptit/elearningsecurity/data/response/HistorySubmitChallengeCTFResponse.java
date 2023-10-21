package com.ptit.elearningsecurity.data.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class HistorySubmitChallengeCTFResponse {
    private Integer id;
    private String username;
    @JsonProperty("challenge_ctf_title")
    private String challengeCTFTitle;
    private String result;
}
