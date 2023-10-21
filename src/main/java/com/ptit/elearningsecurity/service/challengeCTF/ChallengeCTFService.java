package com.ptit.elearningsecurity.service.challengeCTF;

import com.ptit.elearningsecurity.common.DataUtils;
import com.ptit.elearningsecurity.data.mapper.ChallengeCTFMapper;
import com.ptit.elearningsecurity.data.request.ChallengeCTFRequest;
import com.ptit.elearningsecurity.data.response.ChallengeCTFPageableResponse;
import com.ptit.elearningsecurity.data.response.ChallengeCTFResponse;
import com.ptit.elearningsecurity.entity.labCTF.ChallengeCTF;
import com.ptit.elearningsecurity.exception.ChallengeCTFCustomException;
import com.ptit.elearningsecurity.repository.ChallengeCTFRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChallengeCTFService implements IChallengeCTFService {
    private final ChallengeCTFRepository challengeCTFRepository;
    private final ChallengeCTFMapper challengeCTFMapper;

    @Override
    public ChallengeCTFPageableResponse getAllChallengeCTF(Pageable pageable) {
        Page<ChallengeCTF> challengeCTFPage = challengeCTFRepository.findAll(pageable);
        List<ChallengeCTF> challengeCTFList = challengeCTFPage.getContent();
        List<ChallengeCTFResponse> challengeCTFResponseList = challengeCTFMapper.toListResponse(challengeCTFList);
        ChallengeCTFPageableResponse challengeCTFPageableResponse = new ChallengeCTFPageableResponse();
        return challengeCTFPageableResponse.setData(challengeCTFResponseList)
                .setTotalItems(challengeCTFPage.getTotalElements())
                .setCurrentPage(challengeCTFPage.getNumber())
                .setTotalPages(challengeCTFPage.getTotalPages());
    }

    @Override
    public ChallengeCTFResponse getChallengeCTFById(Integer challengeCTFId) throws ChallengeCTFCustomException {
        Optional<ChallengeCTF> challengeCTFOptional = challengeCTFRepository.findById(challengeCTFId);
        if(challengeCTFOptional.isEmpty()) {
            throw new ChallengeCTFCustomException("Challenge CTF Not Found",
                    DataUtils.ERROR_CHALLENGE_CTF_NOT_FOUND);
        }
        return challengeCTFMapper.toResponse(challengeCTFOptional.get());
    }

    @Override
    public String getFlagCTFById(Integer challengeCTFId) throws ChallengeCTFCustomException {
        Optional<ChallengeCTF> challengeCTFOptional = challengeCTFRepository.findById(challengeCTFId);
        if(challengeCTFOptional.isEmpty()) {
            throw new ChallengeCTFCustomException("Challenge CTF Not Found",
                    DataUtils.ERROR_CHALLENGE_CTF_NOT_FOUND);
        }
        return challengeCTFOptional.get().getFlag();
    }

    @Override
    public ChallengeCTFResponse createChallengeCTF(ChallengeCTFRequest challengeCTFRequest) {
        ChallengeCTF challengeCTF = challengeCTFMapper.toPojo(challengeCTFRequest);
        challengeCTF.setTotalSolve(0);
        challengeCTF.setFlag("CTF_PTIT_FLAG{" + challengeCTFRequest.getFlag() + "}");
        ChallengeCTF challengeCTFSaved = challengeCTFRepository.save(challengeCTF);
        return challengeCTFMapper.toResponse(challengeCTFSaved);
    }

    @Override
    public ChallengeCTFResponse updateChallengeCTF(Integer challengeCTFId, ChallengeCTFRequest challengeCTFRequest) throws ChallengeCTFCustomException {
        Optional<ChallengeCTF> challengeCTFOptional = challengeCTFRepository.findById(challengeCTFId);
        if(challengeCTFOptional.isEmpty()) {
            throw new ChallengeCTFCustomException("Challenge CTF Not Found",
                    DataUtils.ERROR_CHALLENGE_CTF_NOT_FOUND);
        }
        ChallengeCTF challengeCTF = challengeCTFOptional.get();
        if (Objects.nonNull(challengeCTFRequest.getTitle()) && !"".equalsIgnoreCase(challengeCTFRequest.getTitle())) {
            challengeCTF.setTitle(challengeCTFRequest.getTitle());
        }
        if (Objects.nonNull(challengeCTFRequest.getContent()) && !"".equalsIgnoreCase(challengeCTFRequest.getContent())) {
            challengeCTF.setContent(challengeCTFRequest.getContent());
        }
        if (Objects.nonNull(challengeCTFRequest.getLevel()) && !"".equalsIgnoreCase(challengeCTFRequest.getLevel())) {
            challengeCTF.setLevel(challengeCTFRequest.getLevel());
        }
        if (Objects.nonNull(challengeCTFRequest.getTag()) && !"".equalsIgnoreCase(challengeCTFRequest.getTag())) {
            challengeCTF.setTag(challengeCTFRequest.getTag());
        }
        if (Objects.nonNull(challengeCTFRequest.getHint()) && !"".equalsIgnoreCase(challengeCTFRequest.getHint())) {
            challengeCTF.setHint(challengeCTFRequest.getHint());
        }
        if (Objects.nonNull(challengeCTFRequest.getFlag()) && !"".equalsIgnoreCase(challengeCTFRequest.getFlag())) {
            challengeCTF.setFlag(challengeCTFRequest.getFlag());
        }
        if (Objects.nonNull(challengeCTFRequest.getPoint()) && challengeCTFRequest.getPoint() > 0) {
            challengeCTF.setPoint(challengeCTFRequest.getPoint());
        }
        challengeCTF.setUpdatedAt(Instant.now());
        challengeCTFRepository.save(challengeCTF);
        return challengeCTFMapper.toResponse(challengeCTF);
    }

    @Override
    public ChallengeCTFResponse updateTotalSolveChallengeCTF(Integer challengeCTFId) throws ChallengeCTFCustomException {
        Optional<ChallengeCTF> challengeCTFOptional = challengeCTFRepository.findById(challengeCTFId);
        if(challengeCTFOptional.isEmpty()) {
            throw new ChallengeCTFCustomException("Challenge CTF Not Found",
                    DataUtils.ERROR_CHALLENGE_CTF_NOT_FOUND);
        }
        ChallengeCTF challengeCTF = challengeCTFOptional.get();
        challengeCTF.setTotalSolve(challengeCTF.getTotalSolve() + 1);
        challengeCTFRepository.save(challengeCTF);
        return challengeCTFMapper.toResponse(challengeCTF);
    }

    @Override
    public void deleteChallengeCTF(Integer challengeCTFId) throws ChallengeCTFCustomException {
        Optional<ChallengeCTF> challengeCTFOptional = challengeCTFRepository.findById(challengeCTFId);
        if(challengeCTFOptional.isEmpty()) {
            throw new ChallengeCTFCustomException("Challenge CTF Not Found",
                    DataUtils.ERROR_CHALLENGE_CTF_NOT_FOUND);
        }
        challengeCTFRepository.delete(challengeCTFOptional.get());
    }
}
