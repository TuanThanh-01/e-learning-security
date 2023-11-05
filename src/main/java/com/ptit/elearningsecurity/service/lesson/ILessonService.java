package com.ptit.elearningsecurity.service.lesson;

import com.ptit.elearningsecurity.data.request.LessonRequest;
import com.ptit.elearningsecurity.data.response.LessonResponse;
import com.ptit.elearningsecurity.exception.CategoryLessonCustomException;
import com.ptit.elearningsecurity.exception.ImageDataCustomException;
import com.ptit.elearningsecurity.exception.LessonCustomException;

import java.io.IOException;
import java.util.List;

public interface ILessonService {
    List<LessonResponse> getAllLesson();
    LessonResponse findById(int lessonID) throws LessonCustomException;
    LessonResponse createLesson(LessonRequest lessonRequest) throws CategoryLessonCustomException, IOException, LessonCustomException;
    LessonResponse updateLesson(LessonRequest lessonRequest, int lessonID) throws CategoryLessonCustomException, IOException, ImageDataCustomException, LessonCustomException;
    void deleteLesson(int lessonID) throws LessonCustomException, IOException;
}
