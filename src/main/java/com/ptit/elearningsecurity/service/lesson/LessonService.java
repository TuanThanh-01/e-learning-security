package com.ptit.elearningsecurity.service.lesson;

import com.ptit.elearningsecurity.common.DataUtils;
import com.ptit.elearningsecurity.data.mapper.LessonMapper;
import com.ptit.elearningsecurity.data.request.LessonRequest;
import com.ptit.elearningsecurity.data.response.LessonResponse;
import com.ptit.elearningsecurity.entity.CategoryLesson;
import com.ptit.elearningsecurity.entity.ImageData;
import com.ptit.elearningsecurity.entity.Lesson;
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
import java.util.*;

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
            throw new LessonCustomException(
                    "Lesson Not Found With ID: " + lessonID,
                    DataUtils.ERROR_LESSON_NOT_FOUND
            );
        }
        return mapImageDataToLessonResponse(lessonOptional.get());
    }


    private LessonResponse mapImageDataToLessonResponse(Lesson lesson) {
        ImageData coverImage = lesson.getCoverImage();
        List<ImageData> contentsImages = lesson.getContentsImages();
        List<String> listContentImageUrl = contentsImages.stream().map(ImageData::getImageUrl).toList();
        LessonResponse lessonResponse = lessonMapper.toResponse(lesson);
        lessonResponse.setCoverImageUrl(coverImage.getImageUrl());
        lessonResponse.setContentsImagesUrl(listContentImageUrl);
        return lessonResponse;
    }

    public String encodeBase64(String message) {
        return Base64.getEncoder().encodeToString(message.getBytes());
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
        String lessonTitleEncoded = encodeBase64(lesson.getTitle());
        ImageData coverImage = imageService.saveImage(lessonTitleEncoded, lessonRequest.getCoverImage());
        List<ImageData> contentImageData = imageService.saveAllImages(lessonTitleEncoded, lessonRequest.getContentsImages());
        lesson.setCoverImage(coverImage);
        lesson.setContentsImages(contentImageData);
        lesson.setCategoryLesson(categoryLessonOptional.get());
        return mapImageDataToLessonResponse(lessonRepository.save(lesson));
    }

    @Override
    public LessonResponse updateLesson(LessonRequest lessonRequest, int lessonID) throws CategoryLessonCustomException, IOException, ImageDataCustomException, LessonCustomException {
        Optional<Lesson> lessonOptional = lessonRepository.findById(lessonID);
        if (lessonOptional.isEmpty()) {
            throw new LessonCustomException(
                    "Lesson Not Found With ID: " + lessonID,
                    DataUtils.ERROR_LESSON_NOT_FOUND
            );
        }
        Lesson lesson = lessonOptional.get();
        String titleEncode = encodeBase64(lesson.getTitle());
        if (Objects.nonNull(lessonRequest.getTitle()) && !"".equalsIgnoreCase(lessonRequest.getTitle())) {
            // update image directory name and image url
            imageService.updateImageDirectoryName(titleEncode, encodeBase64(lessonRequest.getTitle()));
            lesson.setCoverImage(imageService.renameImageFolder(lesson.getCoverImage().getId(), encodeBase64(lessonRequest.getTitle())));
            List<ImageData> contentImages = new ArrayList<>();
            for (ImageData imageData : lesson.getContentsImages()) {
                contentImages.add(imageService.renameImageFolder(imageData.getId(), encodeBase64(lessonRequest.getTitle())));
            }
            lesson.setContentsImages(contentImages);
            lesson.setTitle(lessonRequest.getTitle());
            titleEncode = encodeBase64(lesson.getTitle());
        }

        if (Objects.nonNull(lessonRequest.getDescription()) && !"".equalsIgnoreCase(lessonRequest.getDescription())) {
            lesson.setDescription(lessonRequest.getDescription());
        }

        if (Objects.nonNull(lessonRequest.getContent()) && !"".equalsIgnoreCase(lessonRequest.getContent())) {
            lesson.setContent(lessonRequest.getContent());
        }
        if (Objects.nonNull(lessonRequest.getCoverImage())) {
            if(!lesson.getCoverImage().getImageName().equals(lessonRequest.getCoverImage().getOriginalFilename())) {
                System.out.println("update image");
                imageService.deleteImageByID(lesson.getId());
                imageService.deleteImageResource(lesson.getCoverImage().getImageUrl());
                imageService.updateImage(lesson.getCoverImage().getId(), titleEncode, lessonRequest.getCoverImage());
            }
        }
        if (Objects.nonNull(lessonRequest.getContentsImages()) && lessonRequest.getContentsImages().size() > 0) {
            System.out.println("update image contents");
            for (int i = 0;i < lessonRequest.getContentsImages().size();i++) {
                if(!lesson.getContentsImages().get(i).getImageName()
                        .equals(lessonRequest.getContentsImages().get(i).getOriginalFilename())) {
                    imageService.deleteImageByID(lesson.getContentsImages().get(i).getId());
                    imageService.deleteImageResource(lesson.getContentsImages().get(i).getImageUrl());
                    imageService.updateImage(lesson.getContentsImages().get(i).getId(), titleEncode, lessonRequest.getContentsImages().get(i));
                }
            }
        }
        if(lessonRequest.getCategoryLessonID() > 0) {
            Optional<CategoryLesson> categoryLessonOptional =
                    categoryLessonRepository.findById(lessonRequest.getCategoryLessonID());
            if (categoryLessonOptional.isEmpty()) {
                throw new CategoryLessonCustomException(
                        "Category Lesson Not Found With ID: " + lessonRequest.getCategoryLessonID(),
                        DataUtils.ERROR_CATEGORY_LESSON_NOT_FOUND
                );
            }
            lesson.setCategoryLesson(categoryLessonOptional.get());
        }
        lesson.setUpdatedAt(Instant.now());
        lessonRepository.save(lesson);
        return mapImageDataToLessonResponse(lesson);
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
        Lesson lesson = lessonOptional.get();
        imageService.deleteImageDirectory(encodeBase64(lesson.getTitle()));
        imageService.deleteImageByID(lesson.getCoverImage().getId());
        lesson.getContentsImages().forEach(imageData -> imageService.deleteImageByID(imageData.getId()));
        lessonRepository.delete(lesson);
    }
}
