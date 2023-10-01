package com.ptit.elearningsecurity.repository;

import com.ptit.elearningsecurity.entity.discuss.ImageComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageCommentRepository extends JpaRepository<ImageComment, Integer> {
}
