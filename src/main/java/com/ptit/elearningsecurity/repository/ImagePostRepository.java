package com.ptit.elearningsecurity.repository;

import com.ptit.elearningsecurity.entity.discuss.ImagePost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImagePostRepository extends JpaRepository<ImagePost, Integer> {
}
