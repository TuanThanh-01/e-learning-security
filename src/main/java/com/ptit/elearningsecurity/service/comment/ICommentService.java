package com.ptit.elearningsecurity.service.comment;

import com.ptit.elearningsecurity.data.request.CommentRequest;
import com.ptit.elearningsecurity.data.response.CommentResponse;
import com.ptit.elearningsecurity.exception.CommentCustomException;
import com.ptit.elearningsecurity.exception.PostCustomException;
import com.ptit.elearningsecurity.exception.UserCustomException;

import java.io.IOException;
import java.util.List;

public interface ICommentService {
    List<CommentResponse> getAllCommentByPostID(int postID) throws PostCustomException;
    List<CommentResponse> getAllAnswerComment(int cmtID);
    CommentResponse createComment(CommentRequest commentRequest) throws PostCustomException, UserCustomException, IOException;
    CommentResponse updateComment(int cmtID, CommentRequest commentRequest) throws CommentCustomException, IOException;
    void deleteComment(int cmtID) throws CommentCustomException;
}
