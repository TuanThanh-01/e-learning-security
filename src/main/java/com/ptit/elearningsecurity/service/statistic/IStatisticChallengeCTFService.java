package com.ptit.elearningsecurity.service.statistic;

import com.ptit.elearningsecurity.data.dto.StatisticChallengeCTFOverview;
import com.ptit.elearningsecurity.data.dto.TagTotalCompleteChallengeCTF;
import com.ptit.elearningsecurity.data.dto.TagTotalSubmitChallengeCTF;

import java.util.List;

public interface IStatisticChallengeCTFService {
    StatisticChallengeCTFOverview getStatisticChallengeCTFOverview();
    List<TagTotalCompleteChallengeCTF> getTagTotalCompleteChallengeCTF();
    List<TagTotalSubmitChallengeCTF> getTagTotalSubmitChallengeCTF();
}
