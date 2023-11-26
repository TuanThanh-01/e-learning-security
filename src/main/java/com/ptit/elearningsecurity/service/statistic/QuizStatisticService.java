package com.ptit.elearningsecurity.service.statistic;

import com.ptit.elearningsecurity.data.dto.QuizScoreDTO;
import com.ptit.elearningsecurity.data.dto.QuizTimeCompletionDTO;
import com.ptit.elearningsecurity.data.dto.StatisticQuiz;
import com.ptit.elearningsecurity.repository.QuestionRepository;
import com.ptit.elearningsecurity.repository.QuizRepository;
import com.ptit.elearningsecurity.repository.ScoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizStatisticService implements IQuizStatisticService{
    private final QuestionRepository questionRepository;
    private final QuizRepository quizRepository;
    private final ScoreRepository scoreRepository;

    @Override
    public StatisticQuiz findStatisticQuizOverView() {
        return new StatisticQuiz()
                .setTotalQuiz(quizRepository.count())
                .setTotalQuestion(questionRepository.count())
                .setTotalSolve(scoreRepository.count());
    }

    @Override
    public List<QuizScoreDTO> findQuizScoreAvg() {
        return scoreRepository.findQuizAndAverageScore();
    }

    @Override
    public List<QuizTimeCompletionDTO> findQuizTimeCompletionAvg() {
        List<QuizTimeCompletionDTO> test = scoreRepository.findQuizAndAverageTimeCompletion();
        for(QuizTimeCompletionDTO quizTimeCompletionDTO : test) {
            System.out.println(quizTimeCompletionDTO);
        }
        return scoreRepository.findQuizAndAverageTimeCompletion();
    }
}