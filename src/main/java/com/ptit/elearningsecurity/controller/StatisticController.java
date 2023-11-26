package com.ptit.elearningsecurity.controller;

import com.ptit.elearningsecurity.data.dto.QuizScoreDTO;
import com.ptit.elearningsecurity.data.dto.QuizTimeCompletionDTO;
import com.ptit.elearningsecurity.data.dto.StatisticQuiz;
import com.ptit.elearningsecurity.data.response.UserStatisticChallengeCTFResponse;
import com.ptit.elearningsecurity.exception.UserCustomException;
import com.ptit.elearningsecurity.service.statistic.QuizStatisticService;
import com.ptit.elearningsecurity.service.statistic.UserStatisticChallengeCTFService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/statistic")
@RequiredArgsConstructor
public class StatisticController {
    private final UserStatisticChallengeCTFService userStatisticChallengeCTFService;
    private final QuizStatisticService quizStatisticService;

    @GetMapping("/challenge-ctf-user")
    public ResponseEntity<UserStatisticChallengeCTFResponse> statisticChallengeCTFByUser(@RequestParam("userId") Integer userId)
            throws UserCustomException {
        return ResponseEntity.status(HttpStatus.OK).body(
                userStatisticChallengeCTFService.getStatisticChallengeCTFByUser(userId));
    }

    @GetMapping("/quiz-overview")
    public ResponseEntity<StatisticQuiz> getStatisticQuizOverView() {
        return ResponseEntity.status(HttpStatus.OK).body(quizStatisticService.findStatisticQuizOverView());
    }

    @GetMapping("/quiz-score-avg")
    public ResponseEntity<List<QuizScoreDTO>> getQuizScoreAvg() {
        return ResponseEntity.status(HttpStatus.OK).body(quizStatisticService.findQuizScoreAvg());
    }

    @GetMapping("/quiz-time-completion-avg")
    public ResponseEntity<List<QuizTimeCompletionDTO>> getQuizTimeCompletionAvg() {
        return ResponseEntity.status(HttpStatus.OK).body(quizStatisticService.findQuizTimeCompletionAvg());
    }
}
