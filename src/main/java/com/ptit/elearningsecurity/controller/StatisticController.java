package com.ptit.elearningsecurity.controller;

import com.ptit.elearningsecurity.data.response.UserStatisticChallengeCTFResponse;
import com.ptit.elearningsecurity.exception.UserCustomException;
import com.ptit.elearningsecurity.service.statistic.UserStatisticChallengeCTFService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/statistic")
@RequiredArgsConstructor
public class StatisticController {
    private final UserStatisticChallengeCTFService userStatisticChallengeCTFService;

    @GetMapping("/challenge-ctf-user")
    public ResponseEntity<UserStatisticChallengeCTFResponse> statisticChallengeCTFByUser(@RequestParam("userId") Integer userId)
            throws UserCustomException {
        return ResponseEntity.status(HttpStatus.OK).body(
                userStatisticChallengeCTFService.getStatisticChallengeCTFByUser(userId));
    }
}
