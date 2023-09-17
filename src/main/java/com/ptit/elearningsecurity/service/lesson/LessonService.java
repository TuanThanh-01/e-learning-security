package com.ptit.elearningsecurity.service.lesson;

import com.ptit.elearningsecurity.common.DataUtils;
import com.ptit.elearningsecurity.data.mapper.LessonMapper;
import com.ptit.elearningsecurity.data.request.LessonRequest;
import com.ptit.elearningsecurity.data.response.LessonResponse;
import com.ptit.elearningsecurity.entity.CategoryLesson;
import com.ptit.elearningsecurity.entity.Lesson;
import com.ptit.elearningsecurity.entity.image.ImageData;
import com.ptit.elearningsecurity.exception.CategoryLessonCustomException;
import com.ptit.elearningsecurity.exception.LessonCustomException;
import com.ptit.elearningsecurity.repository.CategoryLessonRepository;
import com.ptit.elearningsecurity.repository.LessonRepository;
import com.ptit.elearningsecurity.service.imageData.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LessonService implements ILessonService {

    private final LessonRepository lessonRepository;
    private final CategoryLessonRepository categoryLessonRepository;
    private final ImageService imageService;
    private final LessonMapper lessonMapper;

    @Override
    public List<LessonResponse> getAllLesson() {
        return lessonMapper.toListLessonResponse(lessonRepository.findAll());
    }

    @Override
    public LessonResponse findById(int lessonID) throws LessonCustomException {
        Optional<Lesson> lessonOptional = lessonRepository.findById(lessonID);
        if (lessonOptional.isEmpty()) {
            throw new LessonCustomException(
                    "Lesson Not Found With ID: " + lessonID,
                    DataUtils.ERROR_LESSON_NOT_FOUND
            );
        }
        return lessonMapper.toResponse(lessonOptional.get());
    }

    @Override
    public LessonResponse createLesson(LessonRequest lessonRequest) throws CategoryLessonCustomException, IOException {
        Lesson lesson = lessonMapper.toPojo(lessonRequest);
        Optional<CategoryLesson> categoryLessonOptional =
                categoryLessonRepository.findById(lessonRequest.getCategoryLessonID());
        if (categoryLessonOptional.isEmpty()) {
            throw new CategoryLessonCustomException(
                    "Category Lesson Not Found With ID: " + lessonRequest.getCategoryLessonID(),
                    DataUtils.ERROR_CATEGORY_LESSON_NOT_FOUND
            );
        }
        ImageData imageCover = imageService.uploadImage(lessonRequest.getCoverImage());
        List<ImageData> imagesContents = imageService.uploadListImage(lessonRequest.getContentsImages());

        lesson.setCategoryLesson(categoryLessonOptional.get());
        lesson.setCoverImage(imageCover);
        lesson.setContentsImages(imagesContents);

        return lessonMapper.toResponse(
                lessonRepository.save(lesson));
    }

    @Override
    public LessonResponse updateLesson(LessonRequest lessonRequest, int lessonID) throws CategoryLessonCustomException {
        Optional<Lesson> lessonOptional = lessonRepository.findById(lessonID);
        if(lessonOptional.isEmpty()) {
            throw new CategoryLessonCustomException(
                    "Category Lesson Not Found With ID: " + lessonRequest.getCategoryLessonID(),
                    DataUtils.ERROR_CATEGORY_LESSON_NOT_FOUND
            );
        }
        Lesson lesson = lessonOptional.get();
        if(Objects.nonNull(lessonRequest.getTitle()) && !"".equalsIgnoreCase(lessonRequest.getTitle())) {
            lesson.setTitle(lessonRequest.getTitle());
        }

        if(Objects.nonNull(lessonRequest.getDescription()) && !"".equalsIgnoreCase(lessonRequest.getDescription())) {
            lesson.setDescription(lessonRequest.getDescription());
        }

        if(Objects.nonNull(lessonRequest.getContent()) && !"".equalsIgnoreCase(lessonRequest.getContent())) {
            lesson.setContent(lessonRequest.getContent());
        }
        lesson.setUpdatedAt(Instant.now());
        lessonRepository.save(lesson);
        return lessonMapper.toResponse(lesson);
    }

    @Override
    public void deleteLesson(int lessonID) throws LessonCustomException {
        Optional<Lesson> lessonOptional = lessonRepository.findById(lessonID);
        if (lessonOptional.isEmpty()) {
            throw new LessonCustomException(
                    "Lesson Not Found With ID: " + lessonID,
                    DataUtils.ERROR_LESSON_NOT_FOUND
            );
        }
        lessonRepository.delete(lessonOptional.get());
    }
}
