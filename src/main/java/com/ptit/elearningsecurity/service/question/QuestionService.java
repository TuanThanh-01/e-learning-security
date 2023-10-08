package com.ptit.elearningsecurity.service.question;

import com.ptit.elearningsecurity.common.DataUtils;
import com.ptit.elearningsecurity.data.mapper.QuestionMapper;
import com.ptit.elearningsecurity.data.request.QuestionRequest;
import com.ptit.elearningsecurity.data.response.QuestionResponse;
import com.ptit.elearningsecurity.entity.quiz.Question;
import com.ptit.elearningsecurity.entity.quiz.Quiz;
import com.ptit.elearningsecurity.exception.QuestionCustomException;
import com.ptit.elearningsecurity.exception.QuizCustomException;
import com.ptit.elearningsecurity.repository.QuestionRepository;
import com.ptit.elearningsecurity.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionService implements IQuestionService{
    private final QuestionRepository questionRepository;
    private final QuizRepository quizRepository;
    private final QuestionMapper questionMapper;

    @Override
    public List<QuestionResponse> getAllQuestionByQuizId(int quizId) throws QuizCustomException {
        Optional<Quiz> quizOptional = quizRepository.findById(quizId);
        if(quizOptional.isEmpty()) {
            throw new QuizCustomException("Quiz Not Found", DataUtils.ERROR_QUIZ_NOT_FOUND);
        }
        return questionMapper.toQuestionResponses(questionRepository.findAllByQuiz(quizOptional.get()));
    }

    @Override
    public QuestionResponse getQuestionById(int questionId) throws QuestionCustomException {
        Optional<Question> questionOptional = questionRepository.findById(questionId);
        if(questionOptional.isEmpty()) {
            throw new QuestionCustomException("Question Not Found", DataUtils.ERROR_QUESTION_NOT_FOUND);
        }
        return questionMapper.toResponse(questionOptional.get());
    }

    @Override
    public QuestionResponse createQuestion(QuestionRequest questionRequest) throws QuizCustomException {
        Question question = questionMapper.toPojo(questionRequest);
        Optional<Quiz> quizOptional = quizRepository.findById(questionRequest.getQuizId());
        if(quizOptional.isEmpty()) {
            throw new QuizCustomException("Quiz Not Found", DataUtils.ERROR_QUIZ_NOT_FOUND);
        }
        question.setQuiz(quizOptional.get());
        return questionMapper.toResponse(questionRepository.save(question));
    }

    @Override
    public QuestionResponse updateQuestion(QuestionRequest questionRequest, int questionId) throws QuestionCustomException {
        Optional<Question> questionOptional = questionRepository.findById(questionId);
        if(questionOptional.isEmpty()) {
            throw new QuestionCustomException("Question Not Found", DataUtils.ERROR_QUESTION_NOT_FOUND);
        }
        Question question = questionOptional.get();
        if(Objects.nonNull(questionRequest.getQuestionTitle()) && !"".equalsIgnoreCase(questionRequest.getQuestionTitle())) {
            question.setQuestionTitle(questionRequest.getQuestionTitle());
        }
        if(Objects.nonNull(questionRequest.getOption1()) && !"".equalsIgnoreCase(questionRequest.getOption1())) {
            question.setOption1(questionRequest.getOption1());
        }
        if(Objects.nonNull(questionRequest.getOption2()) && !"".equalsIgnoreCase(questionRequest.getOption2())) {
            question.setOption2(questionRequest.getOption2());
        }
        if(Objects.nonNull(questionRequest.getOption3()) && !"".equalsIgnoreCase(questionRequest.getOption3())) {
            question.setOption3(questionRequest.getOption3());
        }
        if(Objects.nonNull(questionRequest.getOption4()) && !"".equalsIgnoreCase(questionRequest.getOption4())) {
            question.setOption4(questionRequest.getOption4());
        }
        if(Objects.nonNull(questionRequest.getCorrectAnswer()) && !"".equalsIgnoreCase(questionRequest.getCorrectAnswer())) {
            question.setCorrectAnswer(questionRequest.getCorrectAnswer());
        }

        question.setUpdatedAt(Instant.now());
        return questionMapper.toResponse(questionRepository.save(question));
    }

    @Override
    public void deleteQuestion(int questionId) throws QuestionCustomException {
        Optional<Question> questionOptional = questionRepository.findById(questionId);
        if(questionOptional.isEmpty()) {
            throw new QuestionCustomException("Question Not Found", DataUtils.ERROR_QUESTION_NOT_FOUND);
        }
    }
}
