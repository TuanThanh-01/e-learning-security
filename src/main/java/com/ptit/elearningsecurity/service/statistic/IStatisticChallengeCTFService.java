package com.ptit.elearningsecurity.service.statistic;

import com.ptit.elearningsecurity.data.dto.*;

import java.util.List;

public interface IStatisticChallengeCTFService {
    StatisticChallengeCTFOverviewDTO getStatisticChallengeCTFOverview();
    List<TagTotalCompleteChallengeCTFDTO> getTagTotalCompleteChallengeCTF();
    List<TagTotalSubmitChallengeCTFDTO> getTagTotalSubmitChallengeCTF();
    List<TagTotalChallengeCTFDTO> getTagTotalChallengeCTF();
    List<StatisticUserChallengeCTFDTO> getStatisticUserChallengeCTF();
}
