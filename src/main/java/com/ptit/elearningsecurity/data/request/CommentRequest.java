package com.ptit.elearningsecurity.data.request;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;


@Data
@Accessors(chain = true)
public class CommentRequest {
    private String context;
    private int parentId;
    private int userID;
    private int postID;
    private MultipartFile commentImages;
}
