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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/progress")
public class ProgressController {

    private final ProgressService progressService;

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<ProgressResponse>> getAllProgress() {
        return ResponseEntity.status(HttpStatus.OK).body(progressService.getAllProgress());
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ProgressResponse> getProgressById(@PathVariable("id") int progressId) throws ProgessCustomException {
        return ResponseEntity.status(HttpStatus.OK).body(progressService.getProgressById(progressId));
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/get-percentage-progress-user")
    public ResponseEntity<Integer> getPercentageProgressUser(@RequestParam("userId") int userId) throws UserCustomException, ProgessCustomException {
        return ResponseEntity.status(HttpStatus.OK).body(progressService.caculateProgressPercentageByUser(userId));
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/find-by-user")
    public ResponseEntity<ProgressResponse> getProgressByUserId(@RequestParam int userId) throws ProgessCustomException, UserCustomException {
        return ResponseEntity.status(HttpStatus.OK).body(progressService.getProgressByUserId(userId));
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<ProgressResponse> createProgress(@RequestBody ProgressRequest progressRequest) throws UserCustomException {
        return ResponseEntity.status(HttpStatus.OK).body(progressService.createProgress(progressRequest));
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PutMapping("/update-progress-user")
    public ResponseEntity<ProgressResponse> updateProgress(@RequestParam("lessonId") int lessonId, @RequestParam("userId") int userId) throws LessonCustomException, UserCustomException, ProgessCustomException {
        return ResponseEntity.status(HttpStatus.OK).body(progressService.updateProgressByUser(lessonId, userId));
    }
}
