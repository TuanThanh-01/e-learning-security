package com.ptit.elearningsecurity.repository;

import com.ptit.elearningsecurity.entity.lecture.CategoryLesson;
import com.ptit.elearningsecurity.entity.lecture.Lesson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Integer> {
    Page<Lesson> findAll(Pageable pageable);
    Page<Lesson> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    Page<Lesson> findByCategoryLesson(CategoryLesson categoryLesson, Pageable pageable);
    boolean existsByTitle(String title);
}
