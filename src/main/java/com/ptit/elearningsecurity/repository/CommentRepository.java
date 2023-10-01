package com.ptit.elearningsecurity.repository;

import com.ptit.elearningsecurity.entity.discuss.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
}
