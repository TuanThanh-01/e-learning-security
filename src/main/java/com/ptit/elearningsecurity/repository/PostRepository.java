package com.ptit.elearningsecurity.repository;

import com.ptit.elearningsecurity.entity.discuss.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
}
