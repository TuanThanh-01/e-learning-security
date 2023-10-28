package com.ptit.elearningsecurity.repository;

import com.ptit.elearningsecurity.entity.labCTF.ChallengeCTF;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChallengeCTFRepository extends JpaRepository<ChallengeCTF, Integer> {

    @Query(value = "SELECT c.* FROM ChallengeCTF c " +
            "inner join ChallengeCTFResult r " +
            "on c.id = r.challenge_ctf_id) " +
            "where c.user_id = :user_id", nativeQuery = true)
    List<ChallengeCTF> findAllChallengeCTFResolvedByUser(@Param("user_id") Integer userId);
}
