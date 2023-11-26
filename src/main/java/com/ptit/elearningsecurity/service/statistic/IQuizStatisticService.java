package com.ptit.elearningsecurity.service.statistic;

import com.ptit.elearningsecurity.data.dto.QuizScoreDTO;
import com.ptit.elearningsecurity.data.dto.QuizTimeCompletionDTO;
import com.ptit.elearningsecurity.data.dto.StatisticQuiz;

import java.util.List;

public interface IQuizStatisticService {
    StatisticQuiz findStatisticQuizOverView();
    List<QuizScoreDTO> findQuizScoreAvg();
    List<QuizTimeCompletionDTO> findQuizTimeCompletionAvg();
}
