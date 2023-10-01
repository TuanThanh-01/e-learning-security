package com.ptit.elearningsecurity.service.comment;

import com.ptit.elearningsecurity.data.request.CommentRequest;
import com.ptit.elearningsecurity.data.response.CommentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService implements ICommentService{
    @Override
    public List<CommentResponse> getAllCommentByPostID(int postID) {
        return null;
    }

    @Override
    public List<CommentResponse> getAllAnswerComment(int cmtID) {
        return null;
    }

    @Override
    public CommentResponse createComment(CommentRequest commentRequest) {
        return null;
    }

    @Override
    public CommentResponse updateComment(int cmtID, CommentRequest commentRequest) {
        return null;
    }

    @Override
    public void deleteComment(int cmtID) {

    }
}
