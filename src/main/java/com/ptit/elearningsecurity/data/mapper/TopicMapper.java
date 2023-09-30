package com.ptit.elearningsecurity.data.mapper;

import com.ptit.elearningsecurity.data.request.TopicRequest;
import com.ptit.elearningsecurity.data.response.TopicResponse;
import com.ptit.elearningsecurity.entity.discuss.Topic;
import org.mapstruct.*;

import java.time.Instant;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class TopicMapper {

    public abstract Topic toPojo(TopicRequest topicRequest);

    @Named("toRs")
    public abstract TopicResponse toResponse(Topic topic);

    @IterableMapping(qualifiedByName = "toRs")
    public abstract List<TopicResponse> topicResponseList(List<Topic> topicList);

    @AfterMapping
    protected void after(@MappingTarget Topic topic, TopicRequest topicRequest) {
        topic.setCreatedAt(Instant.now());
        topic.setUpdatedAt(null);
    }
}
