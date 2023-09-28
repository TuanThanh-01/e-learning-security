package com.ptit.elearningsecurity.data.response;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TopicResponse {
    private int id;
    private String name;
}
