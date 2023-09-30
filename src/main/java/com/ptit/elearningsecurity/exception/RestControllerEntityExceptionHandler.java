package com.ptit.elearningsecurity.exception;

import com.ptit.elearningsecurity.entity.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestControllerEntityExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(CategoryLessonCustomException.class)
    public ResponseEntity<ErrorResponse> handleCategoryLessonException(CategoryLessonCustomException exception) {
        ErrorResponse errorResponse = new ErrorResponse()
                .setErrorCode(exception.getErrorCode())
                .setErrorMessage(exception.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LessonCustomException.class)
    public ResponseEntity<ErrorResponse> handleLessonException(LessonCustomException exception) {
        ErrorResponse errorResponse = new ErrorResponse()
                .setErrorCode(exception.getErrorCode())
                .setErrorMessage(exception.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ImageDataCustomException.class)
    public ResponseEntity<ErrorResponse> handleImageDataException(ImageDataCustomException exception) {
        ErrorResponse errorResponse = new ErrorResponse()
                .setErrorCode(exception.getErrorCode())
                .setErrorMessage(exception.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TopicCustomException.class)
    public ResponseEntity<ErrorResponse> handleTopicException(TopicCustomException exception) {
        ErrorResponse errorResponse = new ErrorResponse()
                .setErrorCode(exception.getErrorCode())
                .setErrorMessage(exception.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserCustomException.class)
    public ResponseEntity<ErrorResponse> handleUserException(UserCustomException exception) {
        ErrorResponse errorResponse = new ErrorResponse()
                .setErrorCode(exception.getErrorCode())
                .setErrorMessage(exception.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
