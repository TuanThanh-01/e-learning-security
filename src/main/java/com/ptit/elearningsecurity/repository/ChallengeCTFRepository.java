package com.ptit.elearningsecurity.repository;

import com.ptit.elearningsecurity.data.dto.ChallengeCTFResponseDTO;
import com.ptit.elearningsecurity.data.dto.TagTotalCompleteChallengeCTF;
import com.ptit.elearningsecurity.data.dto.TagTotalSubmitChallengeCTF;
import com.ptit.elearningsecurity.data.dto.TotalTagChallenge;
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

    @Query("SELECT NEW com.ptit.elearningsecurity.data.dto.ChallengeCTFResponseDTO(c.id, c.title, c.content, " +
            "c.level, c.tag, c.hint, c.flag, c.point, c.urlFile, c.totalSolve, c.createdAt, c.updatedAt, r.isCompleted) " +
            "FROM ChallengeCTF c " +
            "LEFT JOIN ChallengeCTFResult r " +
            "ON c.id = r.challengeCTF.id " +
            "AND r.user.id = :user_id")
    List<ChallengeCTFResponseDTO> findAllChallengeCTFResponseDTOByUser(@Param("user_id") Integer userId);

    @Query("SELECT NEW com.ptit.elearningsecurity.data.dto.TotalTagChallenge(c.tag, COUNT(c)) " +
            "FROM ChallengeCTF c group by c.tag ")
    List<TotalTagChallenge> findTotalTag();

    @Query("SELECT NEW com.ptit.elearningsecurity.data.dto.TotalTagChallenge(c.tag, " +
            "SUM(CASE WHEN r.isCompleted = true THEN 1 ELSE 0 END)) " +
            "FROM ChallengeCTF c " +
            "LEFT JOIN ChallengeCTFResult r " +
            "ON c.id = r.challengeCTF.id AND r.user.id = :userId " +
            "group by c.tag")
    List<TotalTagChallenge> findTotalChallengeCompletedByTagForUser(@Param("userId") Integer userId);

    @Query("SELECT COUNT(distinct tag) from ChallengeCTF")
    Long getTotalTag();

    @Query("SELECT new com.ptit.elearningsecurity.data.dto.TagTotalCompleteChallengeCTF(c.tag, count(*)) " +
            "FROM ChallengeCTFResult r " +
            "INNER JOIN ChallengeCTF c " +
            "ON c.id = r.challengeCTF.id " +
            "WHERE r.isCompleted = true " +
            "GROUP BY c.tag")
    List<TagTotalCompleteChallengeCTF> findTagTotalCompleteChallengeCTF();

    @Query("SELECT new com.ptit.elearningsecurity.data.dto.TagTotalSubmitChallengeCTF(c.tag, count(*)) " +
            "FROM HistorySubmitChallengeCTF h " +
            "INNER JOIN ChallengeCTF c " +
            "ON c.id = h.challengeCTF.id " +
            "GROUP BY c.tag")
    List<TagTotalSubmitChallengeCTF> findTagTotalSubmitChallengeCTF();
}
