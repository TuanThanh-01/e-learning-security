package com.ptit.elearningsecurity.data.request;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Accessors(chain = true)
public class PostRequest {
    private String title;
    private String content;
    private List<Integer> listTopicID;
    private int userID;
    private MultipartFile postImages;
}
