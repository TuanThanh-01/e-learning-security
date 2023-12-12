package com.ptit.elearningsecurity.controller;

import com.ptit.elearningsecurity.data.request.HistoryPracticeLabRequest;
import com.ptit.elearningsecurity.data.response.HistoryPracticeLabResponse;
import com.ptit.elearningsecurity.exception.LabCustomException;
import com.ptit.elearningsecurity.exception.UserCustomException;
import com.ptit.elearningsecurity.service.historyPracticeLab.HistoryPracticeLabService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/history-practice-lab")
public class HistoryPracticeLabController {
    private final HistoryPracticeLabService historyPracticeLabService;

    @GetMapping("/all")
    public ResponseEntity<List<HistoryPracticeLabResponse>> getAllHistoryPracticeLab() {
        return ResponseEntity.status(HttpStatus.OK).body(historyPracticeLabService.getHistoryPracticeLab());
    }

    @PostMapping("/create")
    public ResponseEntity<String> createHistoryPracticeLab(@RequestBody HistoryPracticeLabRequest historyPracticeLabRequest)
            throws LabCustomException, UserCustomException {
        historyPracticeLabService.createHistoryPracticeLab(historyPracticeLabRequest);
        return ResponseEntity.status(HttpStatus.OK).body("Create success");
    }
}
