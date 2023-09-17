package com.ptit.elearningsecurity.data.mapper;

import com.ptit.elearningsecurity.data.request.LessonRequest;
import com.ptit.elearningsecurity.data.response.LessonResponse;
import com.ptit.elearningsecurity.entity.CategoryLesson;
import com.ptit.elearningsecurity.entity.Lesson;
import com.ptit.elearningsecurity.exception.CategoryLessonCustomException;
import com.ptit.elearningsecurity.repository.CategoryLessonRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.List;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class LessonMapper {

    public abstract Lesson toPojo(LessonRequest lessonRequest);

    @Named("toRs")
    public abstract LessonResponse toResponse(Lesson lesson);

    @IterableMapping(qualifiedByName = "toRs")
    public abstract List<LessonResponse> toListLessonResponse(List<Lesson> lesson);

    @AfterMapping
    protected void after(@MappingTarget Lesson lesson, LessonRequest lessonRequest) {
        lesson.setCreatedAt(Instant.now());
        lesson.setUpdatedAt(null);
    }

    @AfterMapping
    protected void after(@MappingTarget LessonResponse lessonResponse, Lesson lesson) {
        lessonResponse.setCategoryLessonName(lesson.getCategoryLesson().getCategoryName());
    }
}
