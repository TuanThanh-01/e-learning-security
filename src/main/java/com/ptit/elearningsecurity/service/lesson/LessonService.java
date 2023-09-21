package com.ptit.elearningsecurity.service.lesson;

import com.ptit.elearningsecurity.common.DataUtils;
import com.ptit.elearningsecurity.data.mapper.LessonMapper;
import com.ptit.elearningsecurity.data.request.LessonRequest;
import com.ptit.elearningsecurity.data.response.LessonResponse;
import com.ptit.elearningsecurity.entity.CategoryLesson;
import com.ptit.elearningsecurity.entity.ImageData;
import com.ptit.elearningsecurity.entity.Lesson;
import com.ptit.elearningsecurity.exception.CategoryLessonCustomException;
import com.ptit.elearningsecurity.exception.LessonCustomException;
import com.ptit.elearningsecurity.repository.CategoryLessonRepository;
import com.ptit.elearningsecurity.repository.LessonRepository;
import com.ptit.elearningsecurity.service.imageData.ImageService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LessonService implements ILessonService {

    private final LessonRepository lessonRepository;
    private final CategoryLessonRepository categoryLessonRepository;
    private final ImageService imageService;
    private final LessonMapper lessonMapper;

    @Override
    public List<LessonResponse> getAllLesson() {
        List<Lesson> lessons = lessonRepository.findAll();
        List<LessonResponse> lessonResponses = new ArrayList<>();
        lessons.forEach(lesson -> {
            lessonResponses.add(getLessonResponse(lesson));
        });
        return lessonResponses;
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
        return getLessonResponse(lessonOptional.get());
    }

    private LessonResponse getLessonResponse(Lesson lesson) {
        ImageData coverImage = lesson.getCoverImage();
        List<ImageData> contentsImages = lesson.getContentsImages();
        List<String> listContentImageUrl = contentsImages.stream().map(ImageData::getImageUrl).toList();
        LessonResponse lessonResponse = lessonMapper.toResponse(lesson);
        lessonResponse.setCoverImageUrl(coverImage.getImageUrl());
        lessonResponse.setContentsImagesUrl(listContentImageUrl);
        return lessonResponse;
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
        ImageData coverImage = imageService.saveImage(lesson.getTitle(), lessonRequest.getCoverImage());
        List<ImageData> contentImageData = imageService.saveAllImages(lesson.getTitle(), lessonRequest.getContentsImages());
        lesson.setCoverImage(coverImage);
        lesson.setContentsImages(contentImageData);
        lesson.setCategoryLesson(categoryLessonOptional.get());
        return getLessonResponse(lesson);
    }

    @Override
    public LessonResponse updateLesson(LessonRequest lessonRequest, int lessonID) throws CategoryLessonCustomException, IOException {
        Optional<Lesson> lessonOptional = lessonRepository.findById(lessonID);
        if (lessonOptional.isEmpty()) {
            throw new CategoryLessonCustomException(
                    "Category Lesson Not Found With ID: " + lessonRequest.getCategoryLessonID(),
                    DataUtils.ERROR_CATEGORY_LESSON_NOT_FOUND
            );
        }
        Lesson lesson = lessonOptional.get();
        if (Objects.nonNull(lessonRequest.getTitle()) && !"".equalsIgnoreCase(lessonRequest.getTitle())) {
            lesson.setTitle(lessonRequest.getTitle());
        }

        if (Objects.nonNull(lessonRequest.getDescription()) && !"".equalsIgnoreCase(lessonRequest.getDescription())) {
            lesson.setDescription(lessonRequest.getDescription());
        }

        if (Objects.nonNull(lessonRequest.getContent()) && !"".equalsIgnoreCase(lessonRequest.getContent())) {
            lesson.setContent(lessonRequest.getContent());
        }
        if (Objects.nonNull(lessonRequest.getCoverImage())) {
            if(!lesson.getCoverImage().getImageName().equals(lessonRequest.getCoverImage().getName())) {
                imageService.deleteImageByID(lesson.getId());
                imageService.deleteImageResource(lesson.getCoverImage().getImageUrl());
                imageService.saveImage(lesson.getTitle(), lessonRequest.getCoverImage());
            }
        }
        if (Objects.nonNull(lessonRequest.getContentsImages()) && lessonRequest.getContentsImages().size() > 0) {
            for (int i = 0;i < lessonRequest.getContentsImages().size();i++) {
                if(!lesson.getContentsImages().get(i).getImageName()
                        .equals(lessonRequest.getContentsImages().get(i).getName())) {
                    imageService.deleteImageByID(lesson.getContentsImages().get(i).getId());
                    imageService.deleteImageResource(lesson.getContentsImages().get(i).getImageUrl());
                    imageService.saveImage(lesson.getTitle(), lessonRequest.getContentsImages().get(i));
                }
            }
        }
        lesson.setUpdatedAt(Instant.now());
        lessonRepository.save(lesson);
        return lessonMapper.toResponse(lesson);
    }

    @Override
    public void deleteLesson(int lessonID) throws LessonCustomException, IOException {
        Optional<Lesson> lessonOptional = lessonRepository.findById(lessonID);
        if (lessonOptional.isEmpty()) {
            throw new LessonCustomException(
                    "Lesson Not Found With ID: " + lessonID,
                    DataUtils.ERROR_LESSON_NOT_FOUND
            );
        }
        FileUtils.deleteDirectory(new File(DataUtils.IMAGE_DIRECTORY + "/" + lessonOptional.get().getTitle()));
        lessonRepository.delete(lessonOptional.get());
    }
}
