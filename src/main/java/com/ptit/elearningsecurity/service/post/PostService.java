package com.ptit.elearningsecurity.service.post;

import com.ptit.elearningsecurity.common.DataUtils;
import com.ptit.elearningsecurity.data.mapper.PostMapper;
import com.ptit.elearningsecurity.data.request.PostRequest;
import com.ptit.elearningsecurity.data.response.PostPageableResponse;
import com.ptit.elearningsecurity.data.response.PostResponse;
import com.ptit.elearningsecurity.entity.User;
import com.ptit.elearningsecurity.entity.discuss.Post;
import com.ptit.elearningsecurity.entity.discuss.Topic;
import com.ptit.elearningsecurity.exception.PostCustomException;
import com.ptit.elearningsecurity.exception.TopicCustomException;
import com.ptit.elearningsecurity.exception.UserCustomException;
import com.ptit.elearningsecurity.repository.PostRepository;
import com.ptit.elearningsecurity.repository.TopicRepository;
import com.ptit.elearningsecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class PostService implements IPostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final TopicRepository topicRepository;
    private final PostMapper postMapper;
    private static final Path CURRENT_FOLDER = Paths.get(System.getProperty("user.dir"));


    @Override
    public PostPageableResponse findAllPost(Pageable pageable) {
        Page<Post> postPage = postRepository.findAll(pageable);
        List<Post> posts = postPage.getContent();
        List<PostResponse> postResponseList = postMapper.toPostResponses(posts);
        PostPageableResponse postPageableResponse = new PostPageableResponse();
        postPageableResponse.setData(postResponseList)
                .setTotalItems(postPage.getTotalElements())
                .setCurrentPage(postPage.getNumber())
                .setTotalPages(postPage.getTotalPages());
        return postPageableResponse;
    }

    @Override
    public PostResponse findPostById(int postId) throws PostCustomException {
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isEmpty()) {
            throw new PostCustomException("Post Not Found", DataUtils.ERROR_POST_NOT_FOUND);
        }
        Post post = postOptional.get();
        return postMapper.toResponse(post);
    }

    private String uploadImage(MultipartFile image) throws IOException {
        Path staticPath = Paths.get("static");
        Path imagePath = Paths.get("images");
        Path postPath = Paths.get("post");

        if (!Files.exists(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath).resolve(postPath))) {
            Files.createDirectories(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath).resolve(postPath));
        }

        String timeStamp = new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());
        String imageName = timeStamp.concat(Objects.requireNonNull(image.getOriginalFilename()));
        Path imageFilePath = CURRENT_FOLDER.resolve(staticPath)
                .resolve(imagePath).resolve(postPath)
                .resolve(imageName);
        try(OutputStream os = Files.newOutputStream(imageFilePath)){
            os.write(image.getBytes());
        }
        return "/images/post/" + imageName;
    }

    @Override
    public PostResponse createPost(PostRequest postRequest) throws UserCustomException, IOException, TopicCustomException {
        Post post = postMapper.toPojo(postRequest);
        List<Topic> topics = topicRepository.findAllById(postRequest.getListTopicID());
        post.setTopics(topics);
        if (topics.size() < postRequest.getListTopicID().size()) {
            List<Integer> foundIDs = topics.stream().map(Topic::getId).toList();
            List<Integer> notFoundIDs = new ArrayList<>(postRequest.getListTopicID());
            notFoundIDs.removeAll(foundIDs);
            throw new TopicCustomException(
                    "Topic Not Found With ID: " + notFoundIDs.stream().map(String::valueOf)
                            .collect(Collectors.joining(", ")).trim(),
                    DataUtils.ERROR_TOPIC_NOT_FOUND
            );
        }
        post.setTopics(topicRepository.findAllById(postRequest.getListTopicID()));
        Optional<User> userOptional = userRepository.findById(postRequest.getUserID());
        if (userOptional.isEmpty()) {
            throw new UserCustomException("User Not Found", DataUtils.ERROR_USER_NOT_FOUND);
        }
        post.setUser(userOptional.get());
        if(Objects.nonNull(postRequest.getPostImages())) {
            post.setImageUrl(uploadImage(postRequest.getPostImages()));
        }
        return postMapper.toResponse(postRepository.save(post));
    }

    @Override
    public PostResponse updatePost(int postID, PostRequest postRequest) throws PostCustomException, TopicCustomException, IOException {
        Optional<Post> postOptional = postRepository.findById(postID);
        if (postOptional.isEmpty()) {
            throw new PostCustomException("Post Not Found", DataUtils.ERROR_POST_NOT_FOUND);
        }
        Post post = postOptional.get();
        if (Objects.nonNull(postRequest.getTitle()) && !"".equalsIgnoreCase(postRequest.getTitle())) {
            post.setTitle(postRequest.getTitle());
        }
        if (Objects.nonNull(postRequest.getContent()) && !"".equalsIgnoreCase(postRequest.getContent())) {
            post.setContent(postRequest.getContent());
        }
        if (Objects.nonNull(postRequest.getListTopicID()) && postRequest.getListTopicID().size() > 0) {
            List<Topic> topics = topicRepository.findAllById(postRequest.getListTopicID());
            post.setTopics(topics);
            if (topics.size() < postRequest.getListTopicID().size()) {
                List<Integer> foundIDs = topics.stream().map(Topic::getId).toList();
                List<Integer> notFoundIDs = new ArrayList<>(postRequest.getListTopicID());
                notFoundIDs.removeAll(foundIDs);
                throw new TopicCustomException(
                        "Topic Not Found With ID: " + notFoundIDs.stream().map(String::valueOf)
                        .collect(Collectors.joining(", ")).trim(),
                        DataUtils.ERROR_TOPIC_NOT_FOUND
                );
            }
        }
        if(Objects.nonNull(postRequest.getPostImages())) {
            if(Objects.nonNull(post.getImageUrl())) {
                deleteImageResource(post.getImageUrl());
            }
            post.setImageUrl(uploadImage(postRequest.getPostImages()));
        }
        post.setUpdatedAt(Instant.now());
        return postMapper.toResponse(postRepository.save(post));
    }

    private void deleteImageResource(String imageUrl) throws IOException {
        Path staticPath = Paths.get("static");
        String imageUrlPath = CURRENT_FOLDER.resolve(staticPath) + imageUrl;
        FileUtils.delete(new File(imageUrlPath));
    }

    @Override
    public void deletePost(int postID) throws PostCustomException {
        Optional<Post> postOptional = postRepository.findById(postID);
        if (postOptional.isEmpty()) {
            throw new PostCustomException("Post Not Found", DataUtils.ERROR_POST_NOT_FOUND);
        }
        postRepository.delete(postOptional.get());
    }
}
