package com.ptit.elearningsecurity.service.challengeCTF;

import com.ptit.elearningsecurity.common.DataUtils;
import com.ptit.elearningsecurity.data.mapper.ChallengeCTFMapper;
import com.ptit.elearningsecurity.data.request.ChallengeCTFRequest;
import com.ptit.elearningsecurity.data.response.ChallengeCTFResponse;
import com.ptit.elearningsecurity.entity.labCTF.ChallengeCTF;
import com.ptit.elearningsecurity.exception.ChallengeCTFCustomException;
import com.ptit.elearningsecurity.repository.ChallengeCTFRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChallengeCTFService implements IChallengeCTFService {
    private final ChallengeCTFRepository challengeCTFRepository;
    private final ChallengeCTFMapper challengeCTFMapper;
    private static final Path CURRENT_FOLDER = Paths.get(System.getProperty("user.dir"));

    @Override
    public List<ChallengeCTFResponse> getAllChallengeCTF() {
        List<ChallengeCTF> challengeCTFList = challengeCTFRepository.findAll();
        return challengeCTFMapper.toListResponse(challengeCTFList);
    }

    @Override
    public List<ChallengeCTFResponse> getAllChallengeCTFResolveByUser(int userId) {
        List<ChallengeCTF> challengeCTFList = challengeCTFRepository.findAllChallengeCTFResolvedByUser(userId);
        return challengeCTFMapper.toListResponse(challengeCTFList);
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
    public ChallengeCTFResponse createChallengeCTF(ChallengeCTFRequest challengeCTFRequest, MultipartFile file) throws IOException {
        ChallengeCTF challengeCTF = challengeCTFMapper.toPojo(challengeCTFRequest);
        challengeCTF.setTotalSolve(0);
        challengeCTF.setFlag("CTF_PTIT_FLAG{" + challengeCTFRequest.getFlag() + "}");
        if(Objects.nonNull(file)) {
            String filePath = uploadFile(file);
            challengeCTF.setUrlFile(filePath);
        }
        ChallengeCTF challengeCTFSaved = challengeCTFRepository.save(challengeCTF);
        return challengeCTFMapper.toResponse(challengeCTFSaved);
    }

    private String uploadFile(MultipartFile file) throws IOException {
        Path staticPath = Paths.get("static");
        Path challengePath = Paths.get("challenge");
        Path downloadPath = Paths.get("download");

        if (!Files.exists(CURRENT_FOLDER.resolve(staticPath).resolve(challengePath).resolve(downloadPath))) {
            Files.createDirectories(CURRENT_FOLDER.resolve(staticPath).resolve(challengePath).resolve(downloadPath));
        }
        String timeStamp = new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());
        String fileName = timeStamp.concat("_").concat(Objects.requireNonNull(file.getOriginalFilename()));

        Path filePath = CURRENT_FOLDER.resolve(staticPath)
                .resolve(challengePath).resolve(downloadPath)
                .resolve(fileName);
        Files.copy(file.getInputStream(), filePath);
        return filePath.toString();
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
