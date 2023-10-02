package com.ptit.elearningsecurity.service.post;

import com.ptit.elearningsecurity.data.request.PostRequest;
import com.ptit.elearningsecurity.data.response.PostPageableResponse;
import com.ptit.elearningsecurity.data.response.PostResponse;
import com.ptit.elearningsecurity.exception.PostCustomException;
import com.ptit.elearningsecurity.exception.TopicCustomException;
import com.ptit.elearningsecurity.exception.UserCustomException;
import org.springframework.data.domain.Pageable;

import java.io.IOException;

public interface IPostService {
    PostPageableResponse findAllPost(Pageable pageable);
    PostResponse findPostById(int postId) throws PostCustomException;
    PostResponse createPost(PostRequest postRequest) throws UserCustomException, IOException, TopicCustomException;
    PostResponse updatePost(int postID, PostRequest postRequest) throws PostCustomException, TopicCustomException, UserCustomException, IOException;

    void deletePost(int postID) throws PostCustomException;
}
