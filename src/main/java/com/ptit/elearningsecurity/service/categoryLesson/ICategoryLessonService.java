package com.ptit.elearningsecurity.service.categoryLesson;

import com.ptit.elearningsecurity.data.request.CategoryLessonRequest;
import com.ptit.elearningsecurity.data.response.CategoryLessonResponse;
import com.ptit.elearningsecurity.entity.CategoryLesson;
import com.ptit.elearningsecurity.entity.Lesson;
import com.ptit.elearningsecurity.exception.CategoryLessonCustomException;

import java.util.List;

public interface ICategoryLessonService {
    List<CategoryLessonResponse> getAll();
    CategoryLessonResponse getSingleById(int categoryLessonID) throws CategoryLessonCustomException;
    CategoryLessonResponse create(CategoryLessonRequest categoryLessonRequest);
    CategoryLessonResponse update(CategoryLessonRequest categoryLessonRequest, int categoryLessonID) throws CategoryLessonCustomException;
    void delete(int id) throws CategoryLessonCustomException;
}