package com.ptit.elearningsecurity.service.challengeCTF;

import com.ptit.elearningsecurity.data.request.ChallengeCTFRequest;
import com.ptit.elearningsecurity.data.response.ChallengeCTFResponse;
import com.ptit.elearningsecurity.exception.ChallengeCTFCustomException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IChallengeCTFService {
    List<ChallengeCTFResponse> getAllChallengeCTF();
    List<ChallengeCTFResponse> getAllChallengeCTFResolveByUser(int userId);
    ChallengeCTFResponse getChallengeCTFById(Integer challengeCTFId) throws ChallengeCTFCustomException;
    String getFlagCTFById(Integer challengeCTFId) throws ChallengeCTFCustomException;
    ChallengeCTFResponse createChallengeCTF(ChallengeCTFRequest challengeCTFRequest, MultipartFile file) throws IOException;
    ChallengeCTFResponse updateChallengeCTF(Integer challengeCTFId, ChallengeCTFRequest challengeCTFRequest) throws ChallengeCTFCustomException;
    ChallengeCTFResponse updateTotalSolveChallengeCTF(Integer challengeCTFId) throws ChallengeCTFCustomException;
    void deleteChallengeCTF(Integer challengeCTFId) throws ChallengeCTFCustomException;
}
