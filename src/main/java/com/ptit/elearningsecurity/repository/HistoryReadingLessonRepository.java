package com.ptit.elearningsecurity.repository;

import com.ptit.elearningsecurity.entity.User;
import com.ptit.elearningsecurity.entity.lecture.HistoryReadingLesson;
import com.ptit.elearningsecurity.entity.lecture.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryReadingLessonRepository extends JpaRepository<HistoryReadingLesson, Integer> {

    List<HistoryReadingLesson> findAllByUser(User user);
    List<HistoryReadingLesson> findAllByLesson(Lesson lesson);

    @Query(value = "SELECT a.* FROM historyreadinglesson a INNER JOIN (" +
            "SELECT lesson_id, MIN(id) as id " +
            "FROM historyreadinglesson GROUP BY lesson_id) as b " +
            "ON a.lesson_id = b.lesson_id AND a.id = b.id " +
            "WHERE user_id = :userId " +
            "ORDER BY created_at DESC " +
            "LIMIT 4", nativeQuery = true)
    List<HistoryReadingLesson> findHistoryReadingLessonRecentByUser(@Param("userId") Integer userId);
}
