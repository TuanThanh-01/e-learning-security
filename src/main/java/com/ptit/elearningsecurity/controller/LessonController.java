package com.ptit.elearningsecurity.controller;

import com.ptit.elearningsecurity.data.response.LessonResponse;
import com.ptit.elearningsecurity.exception.LessonCustomException;
import com.ptit.elearningsecurity.service.lesson.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/lesson")
public class LessonController {

    private final LessonService lessonService;

    @GetMapping("/all")
    public ResponseEntity<List<LessonResponse>> getAllLesson() {
        return ResponseEntity.status(HttpStatus.OK).body(lessonService.getAllLesson());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LessonResponse> findLessonById(@PathVariable("id") int lessonID) throws LessonCustomException {
        return ResponseEntity.status(HttpStatus.OK).body(lessonService.findById(lessonID));
    }

    @PostMapping("/create")
    public ResponseEntity<LessonResponse> createLesson(
            @RequestPart("title") String title,
            @RequestPart("description") String description,
            @RequestPart("content") String content,
            @RequestPart("coverImage")MultipartFile coverImage,
            @RequestPart("contentsImages") List<MultipartFile> contentsImages,
            @RequestPart("categoryLessonID") int categoryLessonID
            ) {

        return null;
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<LessonResponse> updateLesson() {
        return null;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLesson(@PathVariable("id") int lessonID) throws LessonCustomException {
        lessonService.deleteLesson(lessonID);
        return ResponseEntity.status(HttpStatus.OK).body("Delete Lesson Successfully!");
    }
}
