package com.ptit.elearningsecurity.data.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class PostPageableResponse {
    @JsonProperty("total_items")
    private long totalItems;
    private List<PostResponse> data;
    @JsonProperty("total_pages")
    private long totalPages;
    @JsonProperty("current_page")
    private int currentPage;
}
