package com.ptit.elearningsecurity.repository;

import com.ptit.elearningsecurity.data.dto.QuizScoreDTO;
import com.ptit.elearningsecurity.data.dto.QuizTimeCompletionDTO;
import com.ptit.elearningsecurity.entity.User;
import com.ptit.elearningsecurity.entity.quiz.Quiz;
import com.ptit.elearningsecurity.entity.quiz.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Integer> {
    List<Score> findAllByQuiz(Quiz quiz);
    List<Score> findAllByUser(User user);

    @Query("SELECT new com.ptit.elearningsecurity.data.dto.QuizScoreDTO(s.quiz.name, avg(s.score)) " +
            "FROM Score s " +
            "GROUP BY s.quiz.id")
    List<QuizScoreDTO> findQuizAndAverageScore();

    @Query("SELECT s.quiz.name as quizTitle, SEC_TO_TIME(AVG(TIME_TO_SEC(s.totalCompletionTime))) as timeAvg " +
            "FROM Score s " +
            "GROUP BY s.quiz.id")
    List<QuizTimeCompletionDTO> findQuizAndAverageTimeCompletion();
}
