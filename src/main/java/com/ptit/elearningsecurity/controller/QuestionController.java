package com.ptit.elearningsecurity.controller;

import com.ptit.elearningsecurity.data.request.QuestionRequest;
import com.ptit.elearningsecurity.data.response.QuestionResponse;
import com.ptit.elearningsecurity.exception.QuestionCustomException;
import com.ptit.elearningsecurity.exception.QuizCustomException;
import com.ptit.elearningsecurity.service.question.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/question")
public class QuestionController {
    private final QuestionService questionService;

    @GetMapping("/all")
    public ResponseEntity<List<QuestionResponse>> getAllQuestionByQuiz(@RequestParam("quizId") int quizId) throws QuizCustomException {
        return ResponseEntity.status(HttpStatus.OK).body(questionService.getAllQuestionByQuizId(quizId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionResponse> getSingleQuestionById(@PathVariable("id") int questionId) throws QuestionCustomException {
        return ResponseEntity.status(HttpStatus.OK).body(questionService.getQuestionById(questionId));
    }

    @PostMapping("/create")
    public ResponseEntity<QuestionResponse> createQuestion(@RequestBody QuestionRequest questionRequest) throws QuizCustomException {
        return ResponseEntity.status(HttpStatus.OK).body(questionService.createQuestion(questionRequest));
    }

    @PostMapping("/upload-question-by-excel")
    public ResponseEntity<List<QuestionResponse>> uploadQuestionByExcel(
            @RequestParam("quizId") int quizId, @RequestParam("file") MultipartFile excelFile) throws QuizCustomException, IOException {
        return ResponseEntity.status(HttpStatus.OK).body(questionService.saveAllQuestionByExcel(excelFile, quizId));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<QuestionResponse> updateQuestion(
            @RequestBody QuestionRequest questionRequest,
            @PathVariable("id") int questionId) throws QuestionCustomException {
        return ResponseEntity.status(HttpStatus.OK).body(questionService.updateQuestion(questionRequest, questionId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteQuestion(@PathVariable("id") int questionId) throws QuestionCustomException {
        questionService.deleteQuestion(questionId);
        return ResponseEntity.status(HttpStatus.OK).body("Delete Question Successfully");
    }
}
