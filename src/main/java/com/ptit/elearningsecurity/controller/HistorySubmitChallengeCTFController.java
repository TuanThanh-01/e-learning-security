package com.ptit.elearningsecurity.controller;

import com.ptit.elearningsecurity.data.request.HistorySubmitChallengeCTFRequest;
import com.ptit.elearningsecurity.data.response.HistorySubmitChallengeCTFPageableResponse;
import com.ptit.elearningsecurity.data.response.HistorySubmitChallengeCTFResponse;
import com.ptit.elearningsecurity.exception.ChallengeCTFCustomException;
import com.ptit.elearningsecurity.exception.UserCustomException;
import com.ptit.elearningsecurity.service.historySubmitChallengeCTF.HistorySubmitChallengeCTFService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/history-submit")
public class HistorySubmitChallengeCTFController {
    private final HistorySubmitChallengeCTFService historySubmitChallengeCTFService;

    @GetMapping("/all")
    public ResponseEntity<HistorySubmitChallengeCTFPageableResponse> getAllHistorySubmit(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable paging = PageRequest.of(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(historySubmitChallengeCTFService.getAllHistorySubmit(paging));
    }

    @GetMapping("/get-all-by-user/{id}")
    public ResponseEntity<HistorySubmitChallengeCTFPageableResponse> getAllHistorySubmitByUser(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @PathVariable("id") int userID
    ) throws UserCustomException {
        Pageable paging = PageRequest.of(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(historySubmitChallengeCTFService.getAllHistorySubmitByUser(paging, userID));
    }

    @GetMapping("/get-all-by-challengeCTF/{id}")
    public ResponseEntity<HistorySubmitChallengeCTFPageableResponse> getAllHistorySubmitByChallengeCTF(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @PathVariable("id") int challengeCTFId
    ) throws ChallengeCTFCustomException {
        Pageable paging = PageRequest.of(page, size);
        return ResponseEntity.status(HttpStatus.OK)
                .body(historySubmitChallengeCTFService.getAllHistorySubmitByChallengeCTF(paging, challengeCTFId));
    }

    @PostMapping("/create")
    public ResponseEntity<HistorySubmitChallengeCTFResponse> createHistorySubmit(
            @RequestBody HistorySubmitChallengeCTFRequest historySubmitChallengeCTFRequest
            ) throws ChallengeCTFCustomException, UserCustomException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(historySubmitChallengeCTFService.createHistorySubmit(historySubmitChallengeCTFRequest));
    }
}