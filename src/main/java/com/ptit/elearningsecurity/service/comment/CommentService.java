package com.ptit.elearningsecurity.service.comment;

import com.ptit.elearningsecurity.common.DataUtils;
import com.ptit.elearningsecurity.data.mapper.CommentMapper;
import com.ptit.elearningsecurity.data.mapper.UserMapper;
import com.ptit.elearningsecurity.data.request.CommentRequest;
import com.ptit.elearningsecurity.data.response.CommentResponse;
import com.ptit.elearningsecurity.entity.User;
import com.ptit.elearningsecurity.entity.discuss.Comment;
import com.ptit.elearningsecurity.entity.discuss.Post;
import com.ptit.elearningsecurity.exception.CommentCustomException;
import com.ptit.elearningsecurity.exception.PostCustomException;
import com.ptit.elearningsecurity.exception.UserCustomException;
import com.ptit.elearningsecurity.repository.CommentRepository;
import com.ptit.elearningsecurity.repository.PostRepository;
import com.ptit.elearningsecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
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

@Service
@RequiredArgsConstructor
public class CommentService implements ICommentService{

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;
    private static final Path CURRENT_FOLDER = Paths.get(System.getProperty("user.dir"));

    @Override
    public List<CommentResponse> getAllCommentByPostID(int postID) throws PostCustomException {
        Optional<Post> postOptional = postRepository.findById(postID);
        if(postOptional.isEmpty()) {
            throw new PostCustomException("Post Not Found", DataUtils.ERROR_POST_NOT_FOUND);
        }
        Post post = postOptional.get();
        List<Comment> comments = commentRepository.findAllByPost(post);
        List<CommentResponse> commentResponses = new ArrayList<>();
        comments.forEach(comment -> {
            CommentResponse commentResponse = commentMapper.toResponse(comment);
            commentResponse.setPostID(comment.getPost().getId());
            commentResponses.add(commentResponse);
        });
        return commentResponses;
    }

    @Override
    public List<CommentResponse> getAllAnswerComment(int cmtID) {
        List<Comment> comments = commentRepository.findAllByParentId(cmtID);
        List<CommentResponse> commentResponses = new ArrayList<>();
        comments.forEach(comment -> {
            CommentResponse commentResponse = commentMapper.toResponse(comment);
            commentResponse.setPostID(comment.getPost().getId());
            commentResponses.add(commentResponse);
        });
        return commentResponses;
    }

    private String uploadImage(MultipartFile image) throws IOException {
        Path staticPath = Paths.get("static");
        Path imagePath = Paths.get("images");
        Path commentPath = Paths.get("comment");

        if (!Files.exists(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath).resolve(commentPath))) {
            Files.createDirectories(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath).resolve(commentPath));
        }

        String timeStamp = new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());
        String imageName = timeStamp.concat(Objects.requireNonNull(image.getOriginalFilename()));
        Path imageFilePath = CURRENT_FOLDER.resolve(staticPath)
                .resolve(imagePath).resolve(commentPath)
                .resolve(imageName);
        try(OutputStream os = Files.newOutputStream(imageFilePath)){
            os.write(image.getBytes());
        }
        return "/images/comment/" + imageName;
    }

    @Override
    public CommentResponse createComment(CommentRequest commentRequest)
            throws PostCustomException, UserCustomException, IOException {
        Comment comment = commentMapper.toPojo(commentRequest);
        Optional<Post> postOptional = postRepository.findById(commentRequest.getPostID());
        if(postOptional.isEmpty()) {
            throw new PostCustomException("Post Not Found", DataUtils.ERROR_POST_NOT_FOUND);
        }
        Post post = postOptional.get();
        comment.setPost(post);
        Optional<User> userOptional = userRepository.findById(commentRequest.getUserID());
        if(userOptional.isEmpty()) {
            throw new UserCustomException("User Not Found", DataUtils.ERROR_POST_NOT_FOUND);
        }
        User user = userOptional.get();
        comment.setPost(post);
        comment.setUser(user);
        if(Objects.nonNull(commentRequest.getCommentImages())){
            comment.setImageUrl(uploadImage(commentRequest.getCommentImages()));
        }
        Comment commentSaved = commentRepository.save(comment);

        CommentResponse commentResponse = commentMapper.toResponse(commentSaved);
        commentResponse.setPostID(commentSaved.getPost().getId());
        return commentResponse;
    }

    @Override
    public CommentResponse updateComment(int cmtID, CommentRequest commentRequest) throws CommentCustomException, IOException {
        Optional<Comment> commentOptional = commentRepository.findById(cmtID);
        if(commentOptional.isEmpty()) {
            throw new CommentCustomException("Comment Not Found", DataUtils.ERROR_COMMENT_NOT_FOUND);
        }
        Comment comment = commentOptional.get();
        if(Objects.nonNull(commentRequest.getContext()) && !"".equalsIgnoreCase(commentRequest.getContext())) {
            comment.setContext(commentRequest.getContext());
        }
        if(Objects.nonNull(commentRequest.getCommentImages())) {
            if(Objects.nonNull(comment.getImageUrl())) {
                deleteImageResource(comment.getImageUrl());
            }
            comment.setImageUrl(uploadImage(commentRequest.getCommentImages()));
        }
        comment.setUpdatedAt(Instant.now());
        commentRepository.save(comment);
        CommentResponse commentResponse = commentMapper.toResponse(comment);
        commentResponse.setPostID(comment.getPost().getId());
        return commentResponse;
    }

    private void deleteImageResource(String imageUrl) throws IOException {
        Path staticPath = Paths.get("static");
        String imageUrlPath = CURRENT_FOLDER.resolve(staticPath) + imageUrl;
        FileUtils.delete(new File(imageUrlPath));
    }

    @Override
    public void deleteComment(int cmtID) throws CommentCustomException {
        Optional<Comment> commentOptional = commentRepository.findById(cmtID);
        if(commentOptional.isEmpty()) {
            throw new CommentCustomException("Comment Not Found", DataUtils.ERROR_COMMENT_NOT_FOUND);
        }
        commentRepository.delete(commentOptional.get());
    }
}
