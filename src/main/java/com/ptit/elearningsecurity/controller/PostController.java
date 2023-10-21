package com.ptit.elearningsecurity.controller;

import com.ptit.elearningsecurity.data.request.PostRequest;
import com.ptit.elearningsecurity.data.response.PostPageableResponse;
import com.ptit.elearningsecurity.data.response.PostResponse;
import com.ptit.elearningsecurity.exception.PostCustomException;
import com.ptit.elearningsecurity.exception.TopicCustomException;
import com.ptit.elearningsecurity.exception.UserCustomException;
import com.ptit.elearningsecurity.service.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/post")
public class PostController {

    private final PostService postService;

    @GetMapping("/all")
    public ResponseEntity<PostPageableResponse> getAllPost(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable paging = PageRequest.of(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(postService.findAllPost(paging));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getSinglePost(@PathVariable("id") int postID) throws PostCustomException {
        return ResponseEntity.status(HttpStatus.OK).body(postService.findPostById(postID));
    }

    @PostMapping(value = "/create", consumes = "multipart/form-data")
    public ResponseEntity<PostResponse> createPost(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("listTopicID")List<Integer> listTopicID,
            @RequestParam("userID") Integer userID,
            @RequestParam(value = "postImages", required = false) MultipartFile postImages
            ) throws TopicCustomException, UserCustomException, IOException {
        PostRequest postRequest = new PostRequest();
        postRequest.setTitle(title)
                .setContent(content)
                .setListTopicID(listTopicID)
                .setUserID(userID)
                .setPostImages(postImages);
        return ResponseEntity.status(HttpStatus.OK).body(postService.createPost(postRequest));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<PostResponse> updatePost(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "content", required = false) String content,
            @RequestParam(value = "listTopicID", required = false)List<Integer> listTopicID,
            @RequestParam(value = "userID", required = false) Integer userID,
            @RequestParam(value = "postImages", required = false) MultipartFile postImages,
            @PathVariable("id") int postID
    ) throws PostCustomException, TopicCustomException, IOException {
        PostRequest postRequest = new PostRequest();
        postRequest.setTitle(title)
                .setContent(content)
                .setListTopicID(listTopicID)
                .setUserID(userID)
                .setPostImages(postImages);
        return ResponseEntity.status(HttpStatus.OK).body(postService.updatePost(postID, postRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable("id") int postID) throws PostCustomException {
        postService.deletePost(postID);
        return ResponseEntity.status(HttpStatus.OK).body("Delete Post Successfully");
    }
}
