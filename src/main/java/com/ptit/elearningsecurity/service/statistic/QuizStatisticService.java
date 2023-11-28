package com.ptit.elearningsecurity.service.statistic;

import com.ptit.elearningsecurity.data.dto.QuizCorrectWrongDTO;
import com.ptit.elearningsecurity.data.dto.QuizScoreDTO;
import com.ptit.elearningsecurity.data.dto.QuizTimeCompletionDTO;
import com.ptit.elearningsecurity.data.dto.StatisticQuiz;
import com.ptit.elearningsecurity.data.response.QuizTimeCompletionResponse;
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
    public List<QuizTimeCompletionResponse> findQuizTimeCompletionAvg() {
        List<QuizTimeCompletionDTO> quizTimeCompletionDTOList =
                scoreRepository.findQuizAndAverageTimeCompletion();
        List<QuizTimeCompletionResponse> result = new ArrayList<>();
        quizTimeCompletionDTOList.forEach(quizTimeCompletionDTO -> {
            QuizTimeCompletionResponse quizTimeCompletionResponse = new QuizTimeCompletionResponse();
            quizTimeCompletionResponse.setQuizTitle(quizTimeCompletionDTO.getQuizTitle());
            quizTimeCompletionResponse.setTimeAvg(convertToSecond(quizTimeCompletionDTO.getTimeAvg()));
            result.add(quizTimeCompletionResponse);
        });
        return result;
    }

    private Integer convertToSecond(String time) {
        String [] parts = time.split(":");
        return Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
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
