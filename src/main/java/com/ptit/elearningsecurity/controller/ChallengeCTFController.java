package com.ptit.elearningsecurity.controller;

import com.ptit.elearningsecurity.data.request.ChallengeCTFRequest;
import com.ptit.elearningsecurity.data.response.ChallengeCTFPageableResponse;
import com.ptit.elearningsecurity.data.response.ChallengeCTFResponse;
import com.ptit.elearningsecurity.exception.ChallengeCTFCustomException;
import com.ptit.elearningsecurity.service.challengeCTF.ChallengeCTFService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/challenge-ctf")
public class ChallengeCTFController {
    private final ChallengeCTFService challengeCTFService;

    @GetMapping("/all")
    public ResponseEntity<List<ChallengeCTFResponse>> getAllChallengeCTF() {
        return ResponseEntity.status(HttpStatus.OK).body(challengeCTFService.getAllChallengeCTF());
    }

    @GetMapping("/all-resolved-by-user")
    public ResponseEntity<List<ChallengeCTFResponse>> getAllChallengeCTF(@RequestParam("userId") Integer userId) {
        return ResponseEntity.status(HttpStatus.OK).body(challengeCTFService.getAllChallengeCTFResolveByUser(userId));
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
    public ResponseEntity<ChallengeCTFResponse> createChallengeCTF(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("level") String level,
            @RequestParam("tag") String tag,
            @RequestParam("hint") String hint,
            @RequestParam("flag") String flag,
            @RequestParam("point") Integer point,
            @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        ChallengeCTFRequest challengeCTFRequest = new ChallengeCTFRequest();
        challengeCTFRequest.setTitle(title)
                .setContent(content)
                .setLevel(level)
                .setTag(tag)
                .setHint(hint)
                .setFlag(flag)
                .setPoint(point);
        return ResponseEntity.status(HttpStatus.OK).body(challengeCTFService.createChallengeCTF(challengeCTFRequest, file));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ChallengeCTFResponse> updateChallengeCTF(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("level") String level,
            @RequestParam("tag") String tag,
            @RequestParam("hint") String hint,
            @RequestParam("flag") String flag,
            @RequestParam("point") Integer point,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @PathVariable("id") Integer challengeCTFId) throws ChallengeCTFCustomException, IOException {
        ChallengeCTFRequest challengeCTFRequest = new ChallengeCTFRequest();
        challengeCTFRequest.setTitle(title)
                .setContent(content)
                .setLevel(level)
                .setTag(tag)
                .setHint(hint)
                .setFlag(flag)
                .setPoint(point);
        return ResponseEntity.status(HttpStatus.OK)
                .body(challengeCTFService
                        .updateChallengeCTF(challengeCTFId, challengeCTFRequest, file));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteChallengeCTF(@PathVariable("id") Integer challengeCTFId) throws ChallengeCTFCustomException, IOException {
        challengeCTFService.deleteChallengeCTF(challengeCTFId);
        return ResponseEntity.status(HttpStatus.OK).body("Delete Challenge CTF Successfully!");
    }
}
