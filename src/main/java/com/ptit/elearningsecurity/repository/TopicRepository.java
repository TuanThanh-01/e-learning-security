package com.ptit.elearningsecurity.repository;

import com.ptit.elearningsecurity.entity.discuss.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Integer> {
}