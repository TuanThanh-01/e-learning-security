package com.ptit.elearningsecurity.service.statistic;

import com.ptit.elearningsecurity.common.DataUtils;
import com.ptit.elearningsecurity.data.dto.TotalTagChallenge;
import com.ptit.elearningsecurity.data.response.UserStatisticChallengeCTFResponse;
import com.ptit.elearningsecurity.entity.User;
import com.ptit.elearningsecurity.exception.UserCustomException;
import com.ptit.elearningsecurity.repository.ChallengeCTFRepository;
import com.ptit.elearningsecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserStatisticChallengeCTFService implements IUserStatisticChallengeCTFService {
    private final ChallengeCTFRepository challengeCTFRepository;
    private final UserRepository userRepository;

    @Override
    public UserStatisticChallengeCTFResponse getStatisticChallengeCTFByUser(Integer userId) throws UserCustomException {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new UserCustomException("User Not Found", DataUtils.ERROR_USER_NOT_FOUND);
        }
        User user = userOptional.get();

        List<TotalTagChallenge> listTotalTag = challengeCTFRepository.findTotalTag();
        List<TotalTagChallenge> listChallengeCTFCompletedByTagForUser =
                challengeCTFRepository.findTotalChallngeCompletedByTagForUser(userId);
        listTotalTag.add(new TotalTagChallenge("all", calculateTotalRecord(listTotalTag)));
        listChallengeCTFCompletedByTagForUser.add(
                new TotalTagChallenge("all", calculateTotalRecord(listChallengeCTFCompletedByTagForUser)));

        return getUserStatisticChallengeCTFResponse(user, listTotalTag, listChallengeCTFCompletedByTagForUser);
    }

    private UserStatisticChallengeCTFResponse getUserStatisticChallengeCTFResponse(User user, List<TotalTagChallenge> listTotalTag, List<TotalTagChallenge> listChallengeCTFCompletedByTagForUser) {
        HashMap<String, Integer> mapPercentageTagResult = new HashMap<>();
        for (int i = 0; i < listChallengeCTFCompletedByTagForUser.size(); i++) {
            if (listTotalTag.get(i).getTotalChallenge() == 0) {
                mapPercentageTagResult.put(listChallengeCTFCompletedByTagForUser.get(i).getTag(), 0);
            }
            else {
                Integer percentage = Math.toIntExact(listChallengeCTFCompletedByTagForUser.get(i).getTotalChallenge() * 100
                        / listTotalTag.get(i).getTotalChallenge());
                mapPercentageTagResult.put(listChallengeCTFCompletedByTagForUser.get(i).getTag(), percentage);
            }
        }
        UserStatisticChallengeCTFResponse userStatisticChallengeCTFResponse = new UserStatisticChallengeCTFResponse();
        userStatisticChallengeCTFResponse.setStudentIdentity(user.getStudentIdentity())
                .setUserScore(user.getScoredChallengeCTF())
                .setPercentageWebExploitation(mapPercentageTagResult.get("web exploitation"))
                .setPercentageForensics(mapPercentageTagResult.get("forensics"))
                .setPercentageBinaryExploitation(mapPercentageTagResult.get("binary exploitation"))
                .setPercentageReverseEngineering(mapPercentageTagResult.get("reverse engineering"))
                .setPercentageCryptography(mapPercentageTagResult.get("cryptography"))
                .setPercentageMiscellaneous(mapPercentageTagResult.get("miscellaneous"))
                .setPercentageAll(mapPercentageTagResult.get("all"));
        return userStatisticChallengeCTFResponse;
    }

    private Long calculateTotalRecord(List<TotalTagChallenge> listTotalTagChallenge) {
        Long result = 0L;
        for (TotalTagChallenge totalTagChallenge : listTotalTagChallenge) {
            result += totalTagChallenge.getTotalChallenge();
        }
        return result;
    }
}
