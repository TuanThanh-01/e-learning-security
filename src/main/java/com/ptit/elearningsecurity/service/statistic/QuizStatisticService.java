package com.ptit.elearningsecurity.service.statistic;

import com.ptit.elearningsecurity.data.dto.QuizCorrectWrongDTO;
import com.ptit.elearningsecurity.data.dto.QuizScoreDTO;
import com.ptit.elearningsecurity.data.dto.QuizTimeCompletionDTO;
import com.ptit.elearningsecurity.data.dto.StatisticQuiz;
import com.ptit.elearningsecurity.repository.QuestionRepository;
import com.ptit.elearningsecurity.repository.QuizRepository;
import com.ptit.elearningsecurity.repository.ScoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        List<QuizTimeCompletionDTO> quizTimeCompletionDTOList =
                scoreRepository.findQuizAndAverageTimeCompletion();
        List<QuizTimeCompletionDTO> result = new ArrayList<>();
        quizTimeCompletionDTOList.forEach(quizTimeCompletionDTO -> {
            QuizTimeCompletionDTO quizTimeCompletionDTOTemp = new QuizTimeCompletionDTO();
            quizTimeCompletionDTOTemp.setQuizTitle(quizTimeCompletionDTO.getQuizTitle());
            quizTimeCompletionDTOTemp.setTimeAvg(quizTimeCompletionDTO.getTimeAvg().split("\\.")[0]);
            result.add(quizTimeCompletionDTOTemp);
        });
        return result;
    }

    @Override
    public List<QuizCorrectWrongDTO> findQuizCorrectWrong() {
        List<QuizCorrectWrongDTO> quizCorrectWrongDTOList = scoreRepository.findQuizAndCorrectWrongAnswer();
        List<QuizCorrectWrongDTO> result = new ArrayList<>();
        quizCorrectWrongDTOList.forEach(quizCorrectWrongDTO -> {
            QuizCorrectWrongDTO quizCorrectWrongDTOTemp = new QuizCorrectWrongDTO();
            quizCorrectWrongDTOTemp.setQuizTitle(quizCorrectWrongDTO.getQuizTitle());
            long total = quizCorrectWrongDTO.getTotalCorrect() + quizCorrectWrongDTO.getTotalWrong();
            long percentageCorrect = quizCorrectWrongDTO.getTotalCorrect() * 100 / total;
            long percentageWrong = 100 - percentageCorrect;
            quizCorrectWrongDTOTemp.setTotalCorrect(percentageCorrect);
            quizCorrectWrongDTOTemp.setTotalWrong(percentageWrong);
            result.add(quizCorrectWrongDTOTemp);
        });
        return result;
    }
}
