package com.ptit.elearningsecurity.repository;

import com.ptit.elearningsecurity.entity.lab.Lab;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LabRepository extends JpaRepository<Lab, Integer> {
}
