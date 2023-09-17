package com.ptit.elearningsecurity.repository;

import com.ptit.elearningsecurity.entity.CategoryLesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryLessonRepository extends JpaRepository<CategoryLesson, Integer> {
}
