package com.ptit.elearningsecurity.controller;

import com.ptit.elearningsecurity.data.request.ChallengeCTFRequest;
import com.ptit.elearningsecurity.data.response.ChallengeCTFPageableResponse;
import com.ptit.elearningsecurity.data.response.ChallengeCTFResponse;
import com.ptit.elearningsecurity.exception.ChallengeCTFCustomException;
import com.ptit.elearningsecurity.service.challengeCTF.ChallengeCTFService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/challenge-ctf")
public class ChallengeCTFController {
    private final ChallengeCTFService challengeCTFService;

    @GetMapping("/all")
    public ResponseEntity<ChallengeCTFPageableResponse> getAllChallengeCTF(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size) {
        Pageable paging = PageRequest.of(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(challengeCTFService.getAllChallengeCTF(paging));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChallengeCTFResponse> getChallengeCTFById(@PathVariable("id") Integer challengeCTFId) throws ChallengeCTFCustomException {
        return ResponseEntity.status(HttpStatus.OK).body(challengeCTFService.getChallengeCTFById(challengeCTFId));
    }

    @GetMapping("/get-flag/{id}")
    public ResponseEntity<String> getFlagByChallengeCTFId(@PathVariable("id") Integer challengeCTFId) throws ChallengeCTFCustomException {
        return ResponseEntity.status(HttpStatus.OK).body(challengeCTFService.getFlagCTFById(challengeCTFId));
    }

    @GetMapping("/update-total-solve/{id}")
    public ResponseEntity<ChallengeCTFResponse> updateTotalSolveByChallengeCTFId(@PathVariable("id") Integer challengeCTFId) throws ChallengeCTFCustomException {
        return ResponseEntity.status(HttpStatus.OK).body(challengeCTFService.updateTotalSolveChallengeCTF(challengeCTFId));
    }

    @PostMapping("/create")
    public ResponseEntity<ChallengeCTFResponse> createChallengeCTF(@RequestBody ChallengeCTFRequest challengeCTFRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(challengeCTFService.createChallengeCTF(challengeCTFRequest));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ChallengeCTFResponse> updateChallengeCTF(
            @RequestBody ChallengeCTFRequest challengeCTFRequest,
            @PathVariable("id") Integer challengeCTFId) throws ChallengeCTFCustomException {
        return ResponseEntity.status(HttpStatus.OK).body(challengeCTFService.updateChallengeCTF(challengeCTFId, challengeCTFRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteChallengeCTF(@PathVariable("id") Integer challengeCTFId) throws ChallengeCTFCustomException {
        challengeCTFService.deleteChallengeCTF(challengeCTFId);
        return ResponseEntity.status(HttpStatus.OK).body("Delete Challenge CTF Successfully!");
    }
}
