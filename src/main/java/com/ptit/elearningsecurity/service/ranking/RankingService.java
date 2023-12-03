package com.ptit.elearningsecurity.service.ranking;

import com.ptit.elearningsecurity.data.dto.RankingScoreDTO;
import com.ptit.elearningsecurity.entity.User;
import com.ptit.elearningsecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RankingService implements IRankingService{

    private final UserRepository userRepository;

    @Override
    public List<RankingScoreDTO> findTop5User() {
        List<User> users = userRepository.findTop5ByOrderByScoredChallengeCTFDesc();
        List<RankingScoreDTO> results = new ArrayList<>();
        for(User user : users) {
            RankingScoreDTO rankingScoreDTO = new RankingScoreDTO();
            rankingScoreDTO.setStudentIdentity(user.getStudentIdentity())
                    .setScore(user.getScoredChallengeCTF());
            results.add(rankingScoreDTO);
        }
        return results;
    }
}
