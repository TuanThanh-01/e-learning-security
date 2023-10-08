package com.ptit.elearningsecurity.service.question;

import com.ptit.elearningsecurity.data.request.QuestionRequest;
import com.ptit.elearningsecurity.data.response.QuestionResponse;
import com.ptit.elearningsecurity.exception.QuestionCustomException;
import com.ptit.elearningsecurity.exception.QuizCustomException;

import java.util.List;

public interface IQuestionService {
    List<QuestionResponse> getAllQuestionByQuizId(int quizId) throws QuizCustomException;
    QuestionResponse getQuestionById(int questionId) throws QuestionCustomException;
    QuestionResponse createQuestion(QuestionRequest questionRequest) throws QuizCustomException;
    QuestionResponse updateQuestion(QuestionRequest questionRequest, int questionId) throws QuestionCustomException;
    void deleteQuestion(int questionId) throws QuestionCustomException;
}
