package com.ptit.elearningsecurity.service.progress;

import com.ptit.elearningsecurity.data.request.ProgressRequest;
import com.ptit.elearningsecurity.data.response.ProgressResponse;
import com.ptit.elearningsecurity.exception.LessonCustomException;
import com.ptit.elearningsecurity.exception.ProgessCustomException;
import com.ptit.elearningsecurity.exception.UserCustomException;

import java.util.List;

public interface IProgressService {
    List<ProgressResponse> getAllProgress();
    ProgressResponse getProgressById(int progressId) throws ProgessCustomException;
    ProgressResponse getProgressByUserId(int userId) throws UserCustomException, ProgessCustomException;
    ProgressResponse createProgress(ProgressRequest progressRequest) throws UserCustomException;
    ProgressResponse updateProgressByUser(Integer lessonId, int userId) throws UserCustomException, ProgessCustomException, LessonCustomException;
    Integer caculateProgressPercentageByUser(int userId) throws UserCustomException, ProgessCustomException;
}
