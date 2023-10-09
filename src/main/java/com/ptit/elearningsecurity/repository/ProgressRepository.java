package com.ptit.elearningsecurity.repository;

import com.ptit.elearningsecurity.entity.User;
import com.ptit.elearningsecurity.entity.lecture.Progress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProgressRepository extends JpaRepository<Progress, Integer> {
   Optional<Progress> findByUser(User user);
}
