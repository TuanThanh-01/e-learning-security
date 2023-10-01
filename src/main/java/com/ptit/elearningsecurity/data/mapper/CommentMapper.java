package com.ptit.elearningsecurity.data.mapper;

import com.ptit.elearningsecurity.data.request.CommentRequest;
import com.ptit.elearningsecurity.data.response.CommentResponse;
import com.ptit.elearningsecurity.entity.discuss.Comment;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class CommentMapper {

    @Autowired
    private UserMapper userMapper;

    public abstract Comment toPojo(CommentRequest commentRequest);

    @Named("toRs")
    public abstract CommentResponse toResponse(Comment comment);

    @IterableMapping(qualifiedByName = "toRs")
    public abstract List<CommentResponse> toCommentResponses(List<Comment> comments);

    @AfterMapping
    protected void after(@MappingTarget Comment comment, CommentRequest commentRequest) {
        comment.setCreatedAt(Instant.now());
        comment.setUpdatedAt(null);
    }

    @AfterMapping
    protected void after(@MappingTarget CommentResponse commentResponse, Comment comment) {
        commentResponse.setUserResponse(userMapper.toResponse(comment.getUser()));
    }
}
