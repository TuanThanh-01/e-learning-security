package com.ptit.elearningsecurity.repository;

import com.ptit.elearningsecurity.entity.lecture.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Integer> {
    boolean existsByTitle(String title);
}
