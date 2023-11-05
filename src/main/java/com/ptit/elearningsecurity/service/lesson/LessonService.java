package com.ptit.elearningsecurity.service.lesson;

import com.ptit.elearningsecurity.common.DataUtils;
import com.ptit.elearningsecurity.data.mapper.LessonMapper;
import com.ptit.elearningsecurity.data.request.LessonRequest;
import com.ptit.elearningsecurity.data.response.LessonResponse;
import com.ptit.elearningsecurity.entity.lecture.CategoryLesson;
import com.ptit.elearningsecurity.entity.lecture.ImageLesson;
import com.ptit.elearningsecurity.entity.lecture.Lesson;
import com.ptit.elearningsecurity.exception.CategoryLessonCustomException;
import com.ptit.elearningsecurity.exception.ImageDataCustomException;
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
        lessons.forEach(lesson -> lessonResponses.add(mapImageDataToLessonResponse(lesson)));
        return lessonResponses;
    }

    @Override
    public LessonResponse findById(int lessonID) throws LessonCustomException {
        Optional<Lesson> lessonOptional = lessonRepository.findById(lessonID);
        if (lessonOptional.isEmpty()) {
            throw new LessonCustomException("Lesson Not Found", DataUtils.ERROR_LESSON_NOT_FOUND);
        }
        return mapImageDataToLessonResponse(lessonOptional.get());
    }


    private LessonResponse mapImageDataToLessonResponse(Lesson lesson) {
        String coverImage = lesson.getCoverImage();
        List<ImageLesson> contentsImages = lesson.getContentsImages();
        List<String> listContentImageUrl = contentsImages.stream().map(ImageLesson::getImageUrl).toList();
        LessonResponse lessonResponse = lessonMapper.toResponse(lesson);
        lessonResponse.setCoverImage(coverImage);
        lessonResponse.setContentsImagesUrl(listContentImageUrl);
        lessonResponse.setCategoryLessonList(
                lesson.getCategoryLessons()
                        .stream()
                        .map(CategoryLesson::getCategoryName)
                        .collect(Collectors.toList())
        );
        return lessonResponse;
    }


    @Override
    public LessonResponse createLesson(LessonRequest lessonRequest) throws CategoryLessonCustomException, IOException, LessonCustomException {
        Lesson lesson = lessonMapper.toPojo(lessonRequest);
        List<CategoryLesson> categoryLessons = categoryLessonRepository.findAllById(lessonRequest.getCategoryLessonID());
        lesson.setCategoryLessons(categoryLessons);
        if (categoryLessons.size() < lessonRequest.getCategoryLessonID().size()) {
            List<Integer> foundIDs = categoryLessons.stream().map(CategoryLesson::getId).toList();
            List<Integer> notFoundIDs = new ArrayList<>(lessonRequest.getCategoryLessonID());
            notFoundIDs.removeAll(foundIDs);
            throw new CategoryLessonCustomException(
                    "Category Lesson Not Found: " + notFoundIDs.stream().map(String::valueOf)
                            .collect(Collectors.joining(", ")).trim(),
                    DataUtils.ERROR_TOPIC_NOT_FOUND
            );
        }
        if(lessonRepository.existsByTitle(lessonRequest.getTitle())) {
            throw new LessonCustomException(
                    "Lesson Exists By Title: " + lessonRequest.getTitle(),
                    DataUtils.ERROR_LESSON_EXISTS
            );
        }
        if(lessonRequest.getCoverImage() == null) {
            lesson.setCoverImage("/images/lessonImage/default.png");
        }
        else {
            String coverImage = imageService.uploadImage(lessonRequest.getCoverImage());
            lesson.setCoverImage(coverImage);
        }
        List<ImageLesson> contentImageData = imageService.saveAllImages(lessonRequest.getContentsImages());
        lesson.setContentsImages(contentImageData);
        return mapImageDataToLessonResponse(lessonRepository.save(lesson));
    }

    @Override
    public LessonResponse updateLesson(LessonRequest lessonRequest, int lessonID) throws CategoryLessonCustomException, IOException, ImageDataCustomException, LessonCustomException {
        Optional<Lesson> lessonOptional = lessonRepository.findById(lessonID);
        if (lessonOptional.isEmpty()) {
            throw new LessonCustomException("Lesson Not Found", DataUtils.ERROR_LESSON_NOT_FOUND);
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
            imageService.deleteImage(lesson.getCoverImage());
            lesson.setCoverImage(imageService.uploadImage(lessonRequest.getCoverImage()));
        }
        if (Objects.nonNull(lessonRequest.getContentsImages()) && lessonRequest.getContentsImages().size() > 0) {
            for (int i = 0; i < lessonRequest.getContentsImages().size(); i++) {
                if (!lesson.getContentsImages().get(i).getImageName()
                        .equals(lessonRequest.getContentsImages().get(i).getOriginalFilename())) {
                    imageService.deleteImageByID(lesson.getContentsImages().get(i).getId());
                    imageService.deleteImage(lesson.getContentsImages().get(i).getImageUrl());
                    imageService.updateImage(lesson.getContentsImages().get(i).getId(), lessonRequest.getContentsImages().get(i));
                }
            }
        }
        if (Objects.nonNull(lessonRequest.getCategoryLessonID()) && lessonRequest.getCategoryLessonID().size() > 0) {
            List<CategoryLesson> categoryLessons = categoryLessonRepository.findAllById(lessonRequest.getCategoryLessonID());
            lesson.setCategoryLessons(categoryLessons);
            if (categoryLessons.size() < lessonRequest.getCategoryLessonID().size()) {
                List<Integer> foundIDs = categoryLessons.stream().map(CategoryLesson::getId).toList();
                List<Integer> notFoundIDs = new ArrayList<>(lessonRequest.getCategoryLessonID());
                notFoundIDs.removeAll(foundIDs);
                throw new CategoryLessonCustomException(
                        "Category Lesson Not Found: " + notFoundIDs.stream().map(String::valueOf)
                                .collect(Collectors.joining(", ")).trim(),
                        DataUtils.ERROR_TOPIC_NOT_FOUND
                );
            }
        }
        lesson.setUpdatedAt(Instant.now());
        lessonRepository.save(lesson);
        return mapImageDataToLessonResponse(lesson);
    }

    @Override
    public void deleteLesson(int lessonID) throws LessonCustomException, IOException {
        Optional<Lesson> lessonOptional = lessonRepository.findById(lessonID);
        if (lessonOptional.isEmpty()) {
            throw new LessonCustomException("Lesson Not Found", DataUtils.ERROR_LESSON_NOT_FOUND);
        }
        Lesson lesson = lessonOptional.get();
        for (ImageLesson imageLesson : lesson.getContentsImages()) {
            imageService.deleteImage(imageLesson.getImageUrl());
        }
        imageService.deleteImage(lesson.getCoverImage());
        lesson.getContentsImages().forEach(imageData -> imageService.deleteImageByID(imageData.getId()));
        lessonRepository.delete(lesson);
    }
}
