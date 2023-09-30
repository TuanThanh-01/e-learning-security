package com.ptit.elearningsecurity.repository;

import com.ptit.elearningsecurity.entity.lecture.ImageLesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageLessonRepository extends JpaRepository<ImageLesson, Integer> {

}
