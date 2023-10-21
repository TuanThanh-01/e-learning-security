package com.ptit.elearningsecurity.controller;

import com.ptit.elearningsecurity.data.request.CommentRequest;
import com.ptit.elearningsecurity.data.response.CommentResponse;
import com.ptit.elearningsecurity.exception.CommentCustomException;
import com.ptit.elearningsecurity.exception.PostCustomException;
import com.ptit.elearningsecurity.exception.UserCustomException;
import com.ptit.elearningsecurity.service.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comment")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/get-all-by-post/{postId}")
    public ResponseEntity<List<CommentResponse>> getAllCommentInPost(@PathVariable("postId") int postId) throws PostCustomException {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getAllCommentByPostID(postId));
    }

    @GetMapping("/get-all-answer-comment/{cmtId}")
    public ResponseEntity<List<CommentResponse>> getAllAnswerComment(@PathVariable("cmtId") int cmtId) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getAllAnswerComment(cmtId));
    }

    @PostMapping("/create")
    public ResponseEntity<CommentResponse> createComment(
            @RequestParam("context") String context,
            @RequestParam(value = "parentId", required = false) Integer parentId,
            @RequestParam("userId") Integer userId,
            @RequestParam("postId") Integer postId,
            @RequestParam(value = "commentImages", required = false) MultipartFile commentImages)
            throws PostCustomException, UserCustomException, IOException {
        CommentRequest commentRequest = new CommentRequest();
        commentRequest.setContext(context)
                .setParentId(parentId)
                .setUserID(userId)
                .setPostID(postId)
                .setCommentImages(commentImages);
        return ResponseEntity.status(HttpStatus.OK).body(commentService.createComment(commentRequest));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CommentResponse> updateComment(
            @RequestParam(value = "context", required = false) String context,
            @RequestParam(value = "parentId", required = false) Integer parentId,
            @RequestParam(value = "userId", required = false) Integer userId,
            @RequestParam(value = "postId", required = false) Integer postId,
            @RequestParam(value = "commentImages", required = false) MultipartFile commentImages,
            @PathVariable(value = "id", required = false) int cmtId
    ) throws CommentCustomException, IOException {
        CommentRequest commentRequest = new CommentRequest();
        commentRequest.setContext(context)
                .setParentId(parentId)
                .setUserID(userId)
                .setPostID(postId)
                .setCommentImages(commentImages);
        return ResponseEntity.status(HttpStatus.OK).body(commentService.updateComment(cmtId, commentRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable("id") int id) throws CommentCustomException {
        commentService.deleteComment(id);
        return ResponseEntity.status(HttpStatus.OK).body("Delete Comment Successfully");
    }
}
