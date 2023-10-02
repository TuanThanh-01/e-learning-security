package com.ptit.elearningsecurity.repository;

import com.ptit.elearningsecurity.entity.discuss.Comment;
import com.ptit.elearningsecurity.entity.discuss.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findAllByPost(Post post);

    List<Comment> findAllByParentId(int cmtID);
}
