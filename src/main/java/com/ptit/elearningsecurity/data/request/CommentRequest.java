package com.ptit.elearningsecurity.data.request;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Accessors(chain = true)
public class CommentRequest {
    private String context;
    private int parentID;
    private int userID;
    private int postID;
    private List<MultipartFile> commentImages;
}
