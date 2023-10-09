package com.ptit.elearningsecurity.data.mapper;

import com.ptit.elearningsecurity.data.request.CategoryLessonRequest;
import com.ptit.elearningsecurity.data.request.ProgressRequest;
import com.ptit.elearningsecurity.data.response.CategoryLessonResponse;
import com.ptit.elearningsecurity.data.response.ProgressResponse;
import com.ptit.elearningsecurity.entity.lecture.CategoryLesson;
import com.ptit.elearningsecurity.entity.lecture.Progress;
import org.mapstruct.*;

import java.time.Instant;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class ProgressMapper {

    public abstract Progress toPojo(ProgressRequest progressRequest);

    @Named("toResponse")
    public abstract ProgressResponse toProgressResponse(Progress progress);

    @IterableMapping(qualifiedByName = "toResponse")
    public abstract List<ProgressResponse> toProgressResponses(List<Progress> progresses);

    @AfterMapping
    protected void after(@MappingTarget Progress progress, ProgressRequest progressRequest) {
        progress.setCreatedAt(Instant.now());
        progress.setUpdatedAt(null);
    }
}
