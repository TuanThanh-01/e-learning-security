package com.ptit.elearningsecurity.service.lesson;

import com.ptit.elearningsecurity.common.DataUtils;
import com.ptit.elearningsecurity.data.mapper.LessonMapper;
import com.ptit.elearningsecurity.data.request.LessonRequest;
import com.ptit.elearningsecurity.data.response.LessonPageableResponse;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.util.*;

import static com.ptit.elearningsecurity.common.DataUtils.encodeBase64;

@Service
@RequiredArgsConstructor
public class LessonService implements ILessonService {

    private final LessonRepository lessonRepository;
    private final CategoryLessonRepository categoryLessonRepository;
    private final ImageService imageService;
    private final LessonMapper lessonMapper;

    @Override
    public LessonPageableResponse getAllLesson(Pageable pageable) {
        Page<Lesson> lessonPage = lessonRepository.findAll(pageable);
        List<Lesson> lessons = lessonPage.getContent();
        List<LessonResponse> lessonResponses = new ArrayList<>();
        lessons.forEach(lesson -> lessonResponses.add(mapImageDataToLessonResponse(lesson)));

        LessonPageableResponse lessonPageableResponse = new LessonPageableResponse();
        lessonPageableResponse.setData(lessonResponses);
        lessonPageableResponse.setTotalItems(lessonPage.getTotalElements());
        lessonPageableResponse.setTotalPages(lessonPage.getTotalPages());
        lessonPageableResponse.setCurrentPage(lessonPage.getNumber());
        return lessonPageableResponse;
    }

    @Override
    public LessonPageableResponse getAllLessonByCategoryName(String categoryName, Pageable pageable) throws CategoryLessonCustomException {
        Optional<CategoryLesson> categoryLessonOptional = categoryLessonRepository.findByCategoryName(categoryName);
        if(categoryLessonOptional.isEmpty()) {
            throw new CategoryLessonCustomException("Category Lesson Not Found", DataUtils.ERROR_CATEGORY_LESSON_NOT_FOUND);
        }
        Page<Lesson> lessonPage = lessonRepository.findByCategoryLesson(categoryLessonOptional.get(), pageable);
        List<Lesson> lessons = lessonPage.getContent();
        List<LessonResponse> lessonResponses = new ArrayList<>();
        lessons.forEach(lesson -> lessonResponses.add(mapImageDataToLessonResponse(lesson)));

        LessonPageableResponse lessonPageableResponse = new LessonPageableResponse();
        lessonPageableResponse.setData(lessonResponses);
        lessonPageableResponse.setTotalItems(lessonPage.getTotalElements());
        lessonPageableResponse.setTotalPages(lessonPage.getTotalPages());
        lessonPageableResponse.setCurrentPage(lessonPage.getNumber());
        return lessonPageableResponse;
    }

    @Override
    public LessonPageableResponse getAllLessonByName(String lessonName, Pageable paging) {
        Page<Lesson> lessonPage = lessonRepository.findByTitleContainingIgnoreCase(lessonName, paging);
        List<Lesson> lessons = lessonPage.getContent();
        List<LessonResponse> lessonResponses = new ArrayList<>();
        lessons.forEach(lesson -> lessonResponses.add(mapImageDataToLessonResponse(lesson)));
        LessonPageableResponse lessonPageableResponse = new LessonPageableResponse();
        lessonPageableResponse.setData(lessonResponses);
        lessonPageableResponse.setTotalItems(lessonPage.getTotalElements());
        lessonPageableResponse.setTotalPages(lessonPage.getTotalPages());
        lessonPageableResponse.setCurrentPage(lessonPage.getNumber());
        return lessonPageableResponse;
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
        ImageLesson coverImage = lesson.getCoverImage();
        List<ImageLesson> contentsImages = lesson.getContentsImages();
        List<String> listContentImageUrl = contentsImages.stream().map(ImageLesson::getImageUrl).toList();
        LessonResponse lessonResponse = lessonMapper.toResponse(lesson);
        lessonResponse.setCoverImageUrl(coverImage.getImageUrl());
        lessonResponse.setContentsImagesUrl(listContentImageUrl);
        return lessonResponse;
    }


    @Override
    public LessonResponse createLesson(LessonRequest lessonRequest) throws CategoryLessonCustomException, IOException, LessonCustomException {
        Lesson lesson = lessonMapper.toPojo(lessonRequest);
        Optional<CategoryLesson> categoryLessonOptional =
                categoryLessonRepository.findById(lessonRequest.getCategoryLessonID());
        if (categoryLessonOptional.isEmpty()) {
            throw new CategoryLessonCustomException("Category Lesson Not Found", DataUtils.ERROR_CATEGORY_LESSON_NOT_FOUND);
        }
        if(lessonRepository.existsByTitle(lessonRequest.getTitle())) {
            throw new LessonCustomException(
                    "Lesson Exists By Title: " + lessonRequest.getTitle(),
                    DataUtils.ERROR_LESSON_EXISTS
            );
        }
        String lessonTitleEncoded = encodeBase64(lesson.getTitle());
        ImageLesson coverImage = imageService.saveImage(lessonTitleEncoded, lessonRequest.getCoverImage());
        List<ImageLesson> contentImageData = imageService.saveAllImages(lessonTitleEncoded, lessonRequest.getContentsImages());
        lesson.setCoverImage(coverImage);
        lesson.setContentsImages(contentImageData);
        lesson.setCategoryLesson(categoryLessonOptional.get());
        return mapImageDataToLessonResponse(lessonRepository.save(lesson));
    }

    // kiểm tra lại chức năng cập nhật ảnh
    @Override
    public LessonResponse updateLesson(LessonRequest lessonRequest, int lessonID) throws CategoryLessonCustomException, IOException, ImageDataCustomException, LessonCustomException {
        Optional<Lesson> lessonOptional = lessonRepository.findById(lessonID);
        if (lessonOptional.isEmpty()) {
            throw new LessonCustomException("Lesson Not Found", DataUtils.ERROR_LESSON_NOT_FOUND);
        }
        Lesson lesson = lessonOptional.get();
        String titleEncode = encodeBase64(lesson.getTitle());
        if (Objects.nonNull(lessonRequest.getTitle()) && !"".equalsIgnoreCase(lessonRequest.getTitle())) {
            // update image directory name and image url
            imageService.updateImageDirectoryName(titleEncode, encodeBase64(lessonRequest.getTitle()));
            lesson.setCoverImage(imageService.renameImageFolder(lesson.getCoverImage().getId(), encodeBase64(lessonRequest.getTitle())));
            List<ImageLesson> contentImages = new ArrayList<>();
            for (ImageLesson imageData : lesson.getContentsImages()) {
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
            if (!lesson.getCoverImage().getImageName().equals(lessonRequest.getCoverImage().getOriginalFilename())) {
                System.out.println("update image");
                imageService.deleteImageByID(lesson.getId());
                imageService.deleteImageResource(lesson.getCoverImage().getImageUrl());
                imageService.updateImage(lesson.getCoverImage().getId(), titleEncode, lessonRequest.getCoverImage());
            }
        }
        if (Objects.nonNull(lessonRequest.getContentsImages()) && lessonRequest.getContentsImages().size() > 0) {
            System.out.println("update image contents");
            for (int i = 0; i < lessonRequest.getContentsImages().size(); i++) {
                if (!lesson.getContentsImages().get(i).getImageName()
                        .equals(lessonRequest.getContentsImages().get(i).getOriginalFilename())) {
                    imageService.deleteImageByID(lesson.getContentsImages().get(i).getId());
                    imageService.deleteImageResource(lesson.getContentsImages().get(i).getImageUrl());
                    imageService.updateImage(lesson.getContentsImages().get(i).getId(), titleEncode, lessonRequest.getContentsImages().get(i));
                }
            }
        }
        if (lessonRequest.getCategoryLessonID() > 0) {
            Optional<CategoryLesson> categoryLessonOptional =
                    categoryLessonRepository.findById(lessonRequest.getCategoryLessonID());
            if (categoryLessonOptional.isEmpty()) {
                throw new CategoryLessonCustomException("Category Lesson Not Found", DataUtils.ERROR_CATEGORY_LESSON_NOT_FOUND);
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
            throw new LessonCustomException("Lesson Not Found", DataUtils.ERROR_LESSON_NOT_FOUND);
        }
        Lesson lesson = lessonOptional.get();
        imageService.deleteImageDirectory(encodeBase64(lesson.getTitle()));
        imageService.deleteImageByID(lesson.getCoverImage().getId());
        lesson.getContentsImages().forEach(imageData -> imageService.deleteImageByID(imageData.getId()));
        lessonRepository.delete(lesson);
    }
}
