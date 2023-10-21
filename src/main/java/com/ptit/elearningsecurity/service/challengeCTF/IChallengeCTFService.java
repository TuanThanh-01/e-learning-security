package com.ptit.elearningsecurity.service.challengeCTF;

import com.ptit.elearningsecurity.data.request.ChallengeCTFRequest;
import com.ptit.elearningsecurity.data.response.ChallengeCTFPageableResponse;
import com.ptit.elearningsecurity.data.response.ChallengeCTFResponse;
import com.ptit.elearningsecurity.exception.ChallengeCTFCustomException;
import org.springframework.data.domain.Pageable;

public interface IChallengeCTFService {
    ChallengeCTFPageableResponse getAllChallengeCTF(Pageable pageable);
    ChallengeCTFResponse getChallengeCTFById(Integer challengeCTFId) throws ChallengeCTFCustomException;
    String getFlagCTFById(Integer challengeCTFId) throws ChallengeCTFCustomException;
    ChallengeCTFResponse createChallengeCTF(ChallengeCTFRequest challengeCTFRequest);
    ChallengeCTFResponse updateChallengeCTF(Integer challengeCTFId, ChallengeCTFRequest challengeCTFRequest) throws ChallengeCTFCustomException;
    ChallengeCTFResponse updateTotalSolveChallengeCTF(Integer challengeCTFId) throws ChallengeCTFCustomException;
    void deleteChallengeCTF(Integer challengeCTFId) throws ChallengeCTFCustomException;
}
