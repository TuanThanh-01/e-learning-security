package com.ptit.elearningsecurity.controller;

import com.ptit.elearningsecurity.data.dto.*;
import com.ptit.elearningsecurity.data.response.QuizTimeCompletionResponse;
import com.ptit.elearningsecurity.data.response.StatisticUserQuizResponse;
import com.ptit.elearningsecurity.data.response.UserStatisticChallengeCTFResponse;
import com.ptit.elearningsecurity.exception.UserCustomException;
import com.ptit.elearningsecurity.service.statistic.QuizStatisticService;
import com.ptit.elearningsecurity.service.statistic.StatisticChallengeCTFService;
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
    private final StatisticChallengeCTFService statisticChallengeCTFService;

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
    public ResponseEntity<List<QuizTimeCompletionResponse>> getQuizTimeCompletionAvg() {
        return ResponseEntity.status(HttpStatus.OK).body(quizStatisticService.findQuizTimeCompletionAvg());
    }

    @GetMapping("/quiz-correct-wrong")
    public ResponseEntity<List<QuizCorrectWrongDTO>> getQuizCorrectWrong() {
        return ResponseEntity.status(HttpStatus.OK).body(quizStatisticService.findQuizCorrectWrong());
    }

    @GetMapping("/user-quiz")
    public ResponseEntity<List<StatisticUserQuizResponse>> getUserQuiz() {
        return ResponseEntity.status(HttpStatus.OK).body(quizStatisticService.findStatisticUserQuiz());
    }

    @GetMapping("/challenge-ctf-overview")
    public ResponseEntity<StatisticChallengeCTFOverview> getStatisticChallengeCTFOverview() {
        return ResponseEntity.status(HttpStatus.OK).body(statisticChallengeCTFService.getStatisticChallengeCTFOverview());
    }

    @GetMapping("/tag-total-complete")
    public ResponseEntity<List<TagTotalCompleteChallengeCTF>> getTagTotalCompleteChallengeCTF() {
        return ResponseEntity.status(HttpStatus.OK).body(statisticChallengeCTFService.getTagTotalCompleteChallengeCTF());
    }

    @GetMapping("/tag-total-submit")
    public ResponseEntity<List<TagTotalSubmitChallengeCTF>> getTagTotalSubmitChallengeCTF() {
        return ResponseEntity.status(HttpStatus.OK).body(statisticChallengeCTFService.getTagTotalSubmitChallengeCTF());
    }
}
