package com.ptit.elearningsecurity.repository;

import com.ptit.elearningsecurity.entity.lecture.CategoryLesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CategoryLessonRepository extends JpaRepository<CategoryLesson, Integer> {
    Optional<CategoryLesson> findByCategoryName(String categoryName);
}
