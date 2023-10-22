package com.ptit.elearningsecurity.repository;

import com.ptit.elearningsecurity.entity.User;
import com.ptit.elearningsecurity.entity.labCTF.ChallengeCTF;
import com.ptit.elearningsecurity.entity.labCTF.HistorySubmitChallengeCTF;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryAcceptChallengeRepository extends JpaRepository<HistorySubmitChallengeCTF, Integer> {
    Page<HistorySubmitChallengeCTF> findAllByUser(User user, Pageable pageable);
    Page<HistorySubmitChallengeCTF> findAllByChallengeCTF(ChallengeCTF challengeCTF, Pageable pageable);
}
