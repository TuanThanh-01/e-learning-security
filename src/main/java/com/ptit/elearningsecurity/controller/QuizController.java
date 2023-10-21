package com.ptit.elearningsecurity.controller;

import com.ptit.elearningsecurity.data.request.QuizRequest;
import com.ptit.elearningsecurity.data.response.QuizPageableResponse;
import com.ptit.elearningsecurity.data.response.QuizResponse;
import com.ptit.elearningsecurity.exception.QuizCustomException;
import com.ptit.elearningsecurity.service.quiz.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/quiz")
public class QuizController {

    private final QuizService quizService;

    @GetMapping("/all")
    public ResponseEntity<QuizPageableResponse> getAllQuiz(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable paging = PageRequest.of(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(quizService.findAllQuiz(paging));
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuizResponse> findQuizById(@PathVariable("id") int quizId) throws QuizCustomException {
        return ResponseEntity.status(HttpStatus.OK).body(quizService.findQuizById(quizId));
    }

    @GetMapping("/find-by-name")
    public ResponseEntity<QuizResponse> findQuizByName(@RequestParam String name) throws QuizCustomException {
        return ResponseEntity.status(HttpStatus.OK).body(quizService.findQuizByName(name));
    }

    @PostMapping("/create")
    public ResponseEntity<QuizResponse> createQuiz(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam(value = "image", required = false) MultipartFile image
            ) throws IOException {
        QuizRequest quizRequest = new QuizRequest();
        quizRequest.setName(name)
                .setDescription(description)
                .setImage(image);
        return ResponseEntity.status(HttpStatus.OK).body(quizService.createQuiz(quizRequest));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<QuizResponse> updateQuiz(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @PathVariable("id") int quizId
    ) throws QuizCustomException, IOException {
        QuizRequest quizRequest = new QuizRequest();
        quizRequest.setName(name)
                .setDescription(description)
                .setImage(image);
        return ResponseEntity.status(HttpStatus.OK).body(quizService.updateQuiz(quizRequest, quizId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteQuiz(@PathVariable("id") int quizId) throws QuizCustomException {
        quizService.deleteQuiz(quizId);
        return ResponseEntity.status(HttpStatus.OK).body("Delete Quiz Successfully");
    }
}