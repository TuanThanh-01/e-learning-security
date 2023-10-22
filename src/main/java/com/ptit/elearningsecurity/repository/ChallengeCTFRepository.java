package com.ptit.elearningsecurity.repository;

import com.ptit.elearningsecurity.entity.labCTF.ChallengeCTF;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChallengeCTFRepository extends JpaRepository<ChallengeCTF, Integer> {
    Optional<ChallengeCTF> findByTitle(String title);
}
