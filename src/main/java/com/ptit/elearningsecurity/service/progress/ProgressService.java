package com.ptit.elearningsecurity.service.progress;

import com.ptit.elearningsecurity.common.DataUtils;
import com.ptit.elearningsecurity.data.mapper.ProgressMapper;
import com.ptit.elearningsecurity.data.mapper.UserMapper;
import com.ptit.elearningsecurity.data.request.ProgressRequest;
import com.ptit.elearningsecurity.data.response.ProgressResponse;
import com.ptit.elearningsecurity.entity.User;
import com.ptit.elearningsecurity.entity.lecture.Lesson;
import com.ptit.elearningsecurity.entity.lecture.Progress;
import com.ptit.elearningsecurity.exception.LessonCustomException;
import com.ptit.elearningsecurity.exception.ProgessCustomException;
import com.ptit.elearningsecurity.exception.UserCustomException;
import com.ptit.elearningsecurity.repository.LessonRepository;
import com.ptit.elearningsecurity.repository.ProgressRepository;
import com.ptit.elearningsecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProgressService implements IProgressService{

    private final ProgressRepository progressRepository;
    private final UserRepository userRepository;
    private final LessonRepository lessonRepository;
    private final ProgressMapper progressMapper;
    private final UserMapper userMapper;

    @Override
    public List<ProgressResponse> getAllProgress() {
        List<Progress> progresses = progressRepository.findAll();
        List<ProgressResponse> progressResponses = new ArrayList<>();
        progresses.forEach(progress -> {
            ProgressResponse progressResponse = progressMapper.toProgressResponse(progress);
            progressResponse.setUserResponse(userMapper.toResponse(progress.getUser()));
            progressResponse.setLessonFinished(progress.getLessons().stream().map(Lesson::getTitle).toList());
            progressResponses.add(progressResponse);
        });
        return progressResponses;
    }

    @Override
    public ProgressResponse getProgressById(int progressId) throws ProgessCustomException {
        Optional<Progress> progressOptional = progressRepository.findById(progressId);
        if(progressOptional.isEmpty()) {
            throw new ProgessCustomException("Progress Not Found", DataUtils.ERROR_PROGRESS_NOT_FOUND);
        }
        Progress progress = progressOptional.get();
        ProgressResponse progressResponse = progressMapper.toProgressResponse(progress);
        progressResponse.setUserResponse(userMapper.toResponse(progress.getUser()));
        progressResponse.setLessonFinished(progress.getLessons().stream().map(Lesson::getTitle).toList());
        return progressResponse;
    }

    @Override
    public ProgressResponse getProgressByUserId(int userId) throws UserCustomException, ProgessCustomException {
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isEmpty()) {
            throw new UserCustomException("User Not Found", DataUtils.ERROR_USER_NOT_FOUND);
        }
        Optional<Progress> progressOptional = progressRepository.findByUser(userOptional.get());
        if (progressOptional.isEmpty()) {
            throw new ProgessCustomException("Progress Not Found", DataUtils.ERROR_PROGRESS_NOT_FOUND);
        }
        Progress progress = progressOptional.get();
        ProgressResponse progressResponse = progressMapper.toProgressResponse(progress);
        progressResponse.setUserResponse(userMapper.toResponse(progress.getUser()));
        progressResponse.setLessonFinished(progress.getLessons().stream().map(Lesson::getTitle).toList());
        return progressResponse;
    }

    @Override
    public ProgressResponse createProgress(ProgressRequest progressRequest) throws UserCustomException {
        Progress progress = progressMapper.toPojo(progressRequest);
        Optional<User> userOptional = userRepository.findById(progressRequest.getUserId());
        if(userOptional.isEmpty()) {
            throw new UserCustomException("User Not Found", DataUtils.ERROR_USER_NOT_FOUND);
        }
        progress.setUser(userOptional.get());
        progress.setTotalCompletedLessons(0);
        progress.setLessons(new ArrayList<>());
        return progressMapper.toProgressResponse(progress);
    }

    @Override
    public ProgressResponse updateProgressByUser(Integer lessonId, int userId) throws UserCustomException, ProgessCustomException, LessonCustomException {
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isEmpty()) {
            throw new UserCustomException("User Not Found", DataUtils.ERROR_USER_NOT_FOUND);
        }
        Optional<Progress> progressOptional = progressRepository.findByUser(userOptional.get());
        if (progressOptional.isEmpty()) {
            throw new ProgessCustomException("Progress Not Found", DataUtils.ERROR_PROGRESS_NOT_FOUND);
        }
        Optional<Lesson> lessonOptional = lessonRepository.findById(lessonId);
        if(lessonOptional.isEmpty()) {
            throw new LessonCustomException("Lesson Not Found", DataUtils.ERROR_LESSON_NOT_FOUND);
        }
        Progress progress = progressOptional.get();
        List<Lesson> lessons = progress.getLessons();
        lessons.add(lessonOptional.get());
        progress.setLessons(lessons);
        progress.setTotalCompletedLessons(lessons.size());
        progress.setUpdatedAt(Instant.now());
        progressRepository.save(progress);
        ProgressResponse progressResponse = progressMapper.toProgressResponse(progress);
        progressResponse.setUserResponse(userMapper.toResponse(progress.getUser()));
        progressResponse.setLessonFinished(progress.getLessons().stream().map(Lesson::getTitle).toList());
        return progressResponse;
    }

    @Override
    public Integer caculateProgressPercentageByUser(int userId) throws UserCustomException, ProgessCustomException {
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isEmpty()) {
            throw new UserCustomException("User Not Found", DataUtils.ERROR_USER_NOT_FOUND);
        }
        Optional<Progress> progressOptional = progressRepository.findByUser(userOptional.get());
        if (progressOptional.isEmpty()) {
            throw new ProgessCustomException("Progress Not Found", DataUtils.ERROR_PROGRESS_NOT_FOUND);
        }
        Progress progress = progressOptional.get();
        long totalLesson = lessonRepository.count();
        return (int) Math.round(100.0 / progress.getTotalCompletedLessons() * totalLesson);
    }
}
