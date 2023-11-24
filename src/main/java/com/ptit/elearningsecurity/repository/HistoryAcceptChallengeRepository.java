package com.ptit.elearningsecurity.repository;

import com.ptit.elearningsecurity.entity.User;
import com.ptit.elearningsecurity.entity.labCTF.ChallengeCTF;
import com.ptit.elearningsecurity.entity.labCTF.HistorySubmitChallengeCTF;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryAcceptChallengeRepository extends JpaRepository<HistorySubmitChallengeCTF, Integer> {
    List<HistorySubmitChallengeCTF> findTop8ByUserOrderByCreatedAtDesc(User user);
    Page<HistorySubmitChallengeCTF> findAllByChallengeCTF(ChallengeCTF challengeCTF, Pageable pageable);
}
