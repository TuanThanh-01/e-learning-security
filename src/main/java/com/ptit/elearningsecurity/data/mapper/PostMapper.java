package com.ptit.elearningsecurity.data.mapper;

import com.ptit.elearningsecurity.data.request.PostRequest;
import com.ptit.elearningsecurity.data.response.PostResponse;
import com.ptit.elearningsecurity.entity.discuss.Post;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class PostMapper {

    @Autowired
    protected TopicMapper topicMapper;

    @Autowired
    protected UserMapper userMapper;

    public abstract Post toPojo(PostRequest postRequest);

    @Named("toRs")
    public abstract PostResponse toResponse(Post post);

    @IterableMapping(qualifiedByName = "toRs")
    public abstract List<PostResponse> toPostResponses(List<Post> posts);

    @AfterMapping
    protected void after(@MappingTarget Post post, PostRequest postRequest) {
        post.setCreatedAt(Instant.now());
        post.setUpdatedAt(null);
    }

    @AfterMapping
    protected void after(@MappingTarget PostResponse postResponse, Post post) {
        postResponse.setUserCreate(userMapper.toResponse(post.getUser()));
        postResponse.setTopic(topicMapper.topicResponseList(post.getTopics()));
    }

}
