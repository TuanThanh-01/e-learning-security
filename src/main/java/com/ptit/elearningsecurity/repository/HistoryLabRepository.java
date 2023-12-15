package com.ptit.elearningsecurity.repository;

import com.ptit.elearningsecurity.entity.User;
import com.ptit.elearningsecurity.entity.lab.HistoryPracticeLab;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryLabRepository extends JpaRepository<HistoryPracticeLab, Integer> {
    List<HistoryPracticeLab> findAllByUser(User user);
    @Query(value = "SELECT * FROM HistoryPracticeLab h ORDER BY h.created_at DESC", nativeQuery = true)
    List<HistoryPracticeLab> findALLHistoryPractice();
}
