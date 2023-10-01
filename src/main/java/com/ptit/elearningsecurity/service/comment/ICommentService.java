package com.ptit.elearningsecurity.service.comment;

import com.ptit.elearningsecurity.data.request.CommentRequest;
import com.ptit.elearningsecurity.data.response.CommentResponse;

import java.util.List;

public interface ICommentService {
    List<CommentResponse> getAllCommentByPostID(int postID);
    List<CommentResponse> getAllAnswerComment(int cmtID);
    CommentResponse createComment(CommentRequest commentRequest);
    CommentResponse updateComment(int cmtID, CommentRequest commentRequest);
    void deleteComment(int cmtID);
}
