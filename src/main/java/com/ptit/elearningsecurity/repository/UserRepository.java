package com.ptit.elearningsecurity.repository;

import com.ptit.elearningsecurity.data.dto.RankingScoreDTO;
import com.ptit.elearningsecurity.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    @Query(value = "select * from users where role <> 'ADMIN'", nativeQuery = true)
    List<User> findAllUserWithRoleNotEqualAdmin();

    List<User> findTop5ByOrderByScoredChallengeCTFDesc();
}
