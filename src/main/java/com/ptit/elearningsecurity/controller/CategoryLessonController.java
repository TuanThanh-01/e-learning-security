package com.ptit.elearningsecurity.controller;

import com.ptit.elearningsecurity.data.request.CategoryLessonRequest;
import com.ptit.elearningsecurity.data.response.CategoryLessonResponse;
import com.ptit.elearningsecurity.exception.CategoryLessonCustomException;
import com.ptit.elearningsecurity.service.categoryLesson.CategoryLessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/category-lesson")
public class CategoryLessonController {

    private final CategoryLessonService categoryLessonService;

    @GetMapping("/all")
    public ResponseEntity<List<CategoryLessonResponse>> findAllCategoryLesson() {
        return ResponseEntity.status(HttpStatus.OK).body(categoryLessonService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryLessonResponse> findById(@PathVariable("id") int categoryLessonID) throws CategoryLessonCustomException {
        return ResponseEntity.status(HttpStatus.OK).body(categoryLessonService.getSingleById(categoryLessonID));
    }

    @PostMapping("/create")
    public ResponseEntity<CategoryLessonResponse> createCategoryLesson(@RequestBody CategoryLessonRequest categoryLessonRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(categoryLessonService.create(categoryLessonRequest));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CategoryLessonResponse> updateById(@PathVariable("id") int categoyLessonID, CategoryLessonRequest categoryLessonRequest) throws CategoryLessonCustomException {
        return ResponseEntity.status(HttpStatus.OK).body(categoryLessonService.update(categoryLessonRequest, categoyLessonID));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteById(@PathVariable("id") int categoyLessonID) throws CategoryLessonCustomException {
        categoryLessonService.delete(categoyLessonID);
        return ResponseEntity.status(HttpStatus.OK).body("Delete category lesson with ID " + categoyLessonID + " successfully!");
    }

}
