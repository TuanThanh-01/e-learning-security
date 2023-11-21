package com.ptit.elearningsecurity.controller;

import com.ptit.elearningsecurity.data.request.LessonRequest;
import com.ptit.elearningsecurity.data.response.LessonResponse;
import com.ptit.elearningsecurity.exception.CategoryLessonCustomException;
import com.ptit.elearningsecurity.exception.ImageDataCustomException;
import com.ptit.elearningsecurity.exception.LessonCustomException;
import com.ptit.elearningsecurity.service.lesson.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/lesson")
public class LessonController {

    private final LessonService lessonService;

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<LessonResponse>> getAllLesson() {
        return ResponseEntity.status(HttpStatus.OK).body(lessonService.getAllLesson());
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<LessonResponse> findLessonById(@PathVariable("id") int lessonID) throws LessonCustomException {
        return ResponseEntity.status(HttpStatus.OK).body(lessonService.findById(lessonID));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/create", consumes = "multipart/form-data")
    public ResponseEntity<LessonResponse> createLesson(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("content") String content,
            @RequestParam("coverImage") MultipartFile coverImage,
            @RequestParam(value = "lstCategoryLessonName", required = false) List<String> lstCategoryLessonName
    ) throws CategoryLessonCustomException, IOException, LessonCustomException {
        LessonRequest lessonRequest = new LessonRequest();
        lessonRequest.setTitle(title)
                .setDescription(description)
                .setContent(content)
                .setCoverImage(coverImage)
                .setLstCategoryLessonName(lstCategoryLessonName);
        return ResponseEntity.status(HttpStatus.OK).body(lessonService.createLesson(lessonRequest));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<LessonResponse> updateLesson(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "content", required = false) String content,
            @RequestParam(value = "coverImage", required = false) MultipartFile coverImage,
            @RequestParam(value = "lstCategoryLessonName", required = false, defaultValue = "0") List<String> lstCategoryLessonName,
            @PathVariable("id") int lessonID
    ) throws CategoryLessonCustomException, IOException, ImageDataCustomException, LessonCustomException {
        LessonRequest lessonRequest = new LessonRequest();
        lessonRequest.setTitle(title)
                .setDescription(description)
                .setContent(content)
                .setCoverImage(coverImage)
                .setLstCategoryLessonName(lstCategoryLessonName);
        return ResponseEntity.status(HttpStatus.OK)
                .body(lessonService.updateLesson(lessonRequest, lessonID));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLesson(@PathVariable("id") int lessonID) throws LessonCustomException, IOException {
        lessonService.deleteLesson(lessonID);
        return ResponseEntity.status(HttpStatus.OK).body("Delete Lesson Successfully!");
    }
}
