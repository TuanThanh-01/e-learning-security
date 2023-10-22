package com.ptit.elearningsecurity.service.historySubmitChallengeCTF;

import com.ptit.elearningsecurity.data.request.HistorySubmitChallengeCTFRequest;
import com.ptit.elearningsecurity.data.response.HistorySubmitChallengeCTFPageableResponse;
import com.ptit.elearningsecurity.data.response.HistorySubmitChallengeCTFResponse;
import com.ptit.elearningsecurity.exception.ChallengeCTFCustomException;
import com.ptit.elearningsecurity.exception.UserCustomException;
import org.springframework.data.domain.Pageable;

public interface IHistorySubmitChallengeCTFService {
    HistorySubmitChallengeCTFPageableResponse getAllHistorySubmit(Pageable pageable);

    HistorySubmitChallengeCTFResponse createHistorySubmit(HistorySubmitChallengeCTFRequest historySubmitChallengeCTFRequest) throws UserCustomException, ChallengeCTFCustomException;

    HistorySubmitChallengeCTFPageableResponse getAllHistorySubmitByUser(Pageable pageable, Integer userId) throws UserCustomException;

    HistorySubmitChallengeCTFPageableResponse getAllHistorySubmitByChallengeCTF(Pageable pageable, Integer challengeCTFId) throws ChallengeCTFCustomException;


}
