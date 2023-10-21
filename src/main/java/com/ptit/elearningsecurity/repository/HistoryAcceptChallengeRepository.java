package com.ptit.elearningsecurity.repository;

import com.ptit.elearningsecurity.entity.labCTF.HistorySubmitChallengeCTF;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryAcceptChallengeRepository extends JpaRepository<HistorySubmitChallengeCTF, Integer> {
}
