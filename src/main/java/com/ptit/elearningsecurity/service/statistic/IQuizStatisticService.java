package com.ptit.elearningsecurity.service.statistic;

import com.ptit.elearningsecurity.data.dto.QuizCorrectWrongDTO;
import com.ptit.elearningsecurity.data.dto.QuizScoreDTO;
import com.ptit.elearningsecurity.data.dto.QuizTimeCompletionDTO;
import com.ptit.elearningsecurity.data.dto.StatisticQuiz;
import com.ptit.elearningsecurity.data.response.QuizTimeCompletionResponse;

import java.util.List;

public interface IQuizStatisticService {
    StatisticQuiz findStatisticQuizOverView();
    List<QuizScoreDTO> findQuizScoreAvg();
    List<QuizTimeCompletionResponse> findQuizTimeCompletionAvg();
    List<QuizCorrectWrongDTO> findQuizCorrectWrong();
}
