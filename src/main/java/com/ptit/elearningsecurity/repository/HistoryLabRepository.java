package com.ptit.elearningsecurity.repository;

import com.ptit.elearningsecurity.entity.lab.HistoryPracticeLab;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryLabRepository extends JpaRepository<HistoryPracticeLab, Integer> {
}
