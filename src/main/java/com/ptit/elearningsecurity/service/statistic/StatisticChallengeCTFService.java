package com.ptit.elearningsecurity.service.statistic;

import com.ptit.elearningsecurity.data.dto.*;
import com.ptit.elearningsecurity.repository.ChallengeCTFRepository;
import com.ptit.elearningsecurity.repository.HistorySubmitChallengeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticChallengeCTFService implements IStatisticChallengeCTFService {

    private final ChallengeCTFRepository challengeCTFRepository;
    private final HistorySubmitChallengeRepository historySubmitChallengeRepository;

    @Override
    public StatisticChallengeCTFOverviewDTO getStatisticChallengeCTFOverview() {
        StatisticChallengeCTFOverviewDTO statisticChallengeCTFOverviewDTO =
                new StatisticChallengeCTFOverviewDTO();
        statisticChallengeCTFOverviewDTO.setTotalChallenge(challengeCTFRepository.count())
                .setTotalTag(challengeCTFRepository.getTotalTag())
                .setTotalSubmit(historySubmitChallengeRepository.count());
        return statisticChallengeCTFOverviewDTO;
    }

    @Override
    public List<TagTotalCompleteChallengeCTFDTO> getTagTotalCompleteChallengeCTF() {
        return challengeCTFRepository.findTagTotalCompleteChallengeCTF();
    }

    public List<TagTotalUnCompleteChallengeCTFDTO> getTagTotalUnCompleteChallengeCTF() {
        return challengeCTFRepository.findTagTotalUnCompleteChallengeCTF();
    }

    @Override
    public List<TagTotalSubmitChallengeCTFDTO> getTagTotalSubmitChallengeCTF() {
        return challengeCTFRepository.findTagTotalSubmitChallengeCTF();
    }

    @Override
    public List<TagTotalChallengeCTFDTO> getTagTotalChallengeCTF() {
        return challengeCTFRepository.findTagTotalChallengeCTF();
    }

    @Override
    public List<StatisticUserChallengeCTFDTO> getStatisticUserChallengeCTF() {
        List<StatisticUserChallengeCTFDTO> results = new ArrayList<>();
        for(Object[] object  : challengeCTFRepository.findStatisticUserChallengeCTF()) {
            StatisticUserChallengeCTFDTO statisticUserChallengeCTFDTO = new StatisticUserChallengeCTFDTO();
            statisticUserChallengeCTFDTO.setUserId((Integer) object[0]);
            statisticUserChallengeCTFDTO.setStudentIdentity((String) object[1]);
            statisticUserChallengeCTFDTO.setScore((Integer) object[2]);
            statisticUserChallengeCTFDTO.setUsername(object[4] + " " + object[3]);
            statisticUserChallengeCTFDTO.setTotalTry((BigInteger) object[5]);
            statisticUserChallengeCTFDTO.setTotalCorrect((BigDecimal) object[6]);
            statisticUserChallengeCTFDTO.setTotalWrong((BigDecimal) object[7]);
            statisticUserChallengeCTFDTO.setTotalSubmit((BigInteger) object[8]);
            results.add(statisticUserChallengeCTFDTO);
        }
        return results;
    }


}
