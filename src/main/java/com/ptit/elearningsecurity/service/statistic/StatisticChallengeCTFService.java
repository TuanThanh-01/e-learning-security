package com.ptit.elearningsecurity.service.statistic;

import com.ptit.elearningsecurity.data.dto.StatisticChallengeCTFOverview;
import com.ptit.elearningsecurity.data.dto.TagTotalCompleteChallengeCTF;
import com.ptit.elearningsecurity.data.dto.TagTotalSubmitChallengeCTF;
import com.ptit.elearningsecurity.repository.ChallengeCTFRepository;
import com.ptit.elearningsecurity.repository.HistorySubmitChallengeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticChallengeCTFService implements IStatisticChallengeCTFService {

    private final ChallengeCTFRepository challengeCTFRepository;
    private final HistorySubmitChallengeRepository historySubmitChallengeRepository;

    @Override
    public StatisticChallengeCTFOverview getStatisticChallengeCTFOverview() {
        StatisticChallengeCTFOverview statisticChallengeCTFOverview =
                new StatisticChallengeCTFOverview();
        statisticChallengeCTFOverview.setTotalChallenge(challengeCTFRepository.count())
                .setTotalTag(challengeCTFRepository.getTotalTag())
                .setTotalSubmit(historySubmitChallengeRepository.count());
        return statisticChallengeCTFOverview;
    }

    @Override
    public List<TagTotalCompleteChallengeCTF> getTagTotalCompleteChallengeCTF() {
        return challengeCTFRepository.findTagTotalCompleteChallengeCTF();
    }

    @Override
    public List<TagTotalSubmitChallengeCTF> getTagTotalSubmitChallengeCTF() {
        return challengeCTFRepository.findTagTotalSubmitChallengeCTF();
    }
}
