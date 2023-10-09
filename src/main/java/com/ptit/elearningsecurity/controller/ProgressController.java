package com.ptit.elearningsecurity.controller;

import com.ptit.elearningsecurity.data.request.ProgressRequest;
import com.ptit.elearningsecurity.data.response.ProgressResponse;
import com.ptit.elearningsecurity.exception.LessonCustomException;
import com.ptit.elearningsecurity.exception.ProgessCustomException;
import com.ptit.elearningsecurity.exception.UserCustomException;
import com.ptit.elearningsecurity.service.progress.ProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/progress")
public class ProgressController {

    private final ProgressService progressService;

    @GetMapping("/all")
    public ResponseEntity<List<ProgressResponse>> getAllProgress() {
        return ResponseEntity.status(HttpStatus.OK).body(progressService.getAllProgress());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProgressResponse> getProgressById(@PathVariable("id") int progressId) throws ProgessCustomException {
        return ResponseEntity.status(HttpStatus.OK).body(progressService.getProgressById(progressId));
    }

    @GetMapping("/get-percentage-progress-user")
    public ResponseEntity<Integer> getPercentageProgressUser(@RequestParam("userId") int userId) throws UserCustomException, ProgessCustomException {
        return ResponseEntity.status(HttpStatus.OK).body(progressService.caculateProgressPercentageByUser(userId));
    }

    @GetMapping("/find-by-user")
    public ResponseEntity<ProgressResponse> getProgressByUserId(@RequestParam int userId) throws ProgessCustomException, UserCustomException {
        return ResponseEntity.status(HttpStatus.OK).body(progressService.getProgressByUserId(userId));
    }

    @PostMapping("/create")
    public ResponseEntity<ProgressResponse> createProgress(@RequestBody ProgressRequest progressRequest) throws UserCustomException {
        return ResponseEntity.status(HttpStatus.OK).body(progressService.createProgress(progressRequest));
    }

    @PutMapping("/update-progress-user")
    public ResponseEntity<ProgressResponse> updateProgress(@RequestParam("lessonId") int lessonId, @RequestParam("userId") int userId) throws LessonCustomException, UserCustomException, ProgessCustomException {
        return ResponseEntity.status(HttpStatus.OK).body(progressService.updateProgressByUser(lessonId, userId));
    }
}
