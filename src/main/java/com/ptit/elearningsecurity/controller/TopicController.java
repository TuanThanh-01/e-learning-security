package com.ptit.elearningsecurity.controller;

import com.ptit.elearningsecurity.data.request.TopicRequest;
import com.ptit.elearningsecurity.data.response.TopicResponse;
import com.ptit.elearningsecurity.exception.TopicCustomException;
import com.ptit.elearningsecurity.service.topic.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/topic")
public class TopicController {

    private final TopicService topicService;

    @GetMapping("/all")
    public ResponseEntity<List<TopicResponse>> getAllTopic() {
        return ResponseEntity.status(HttpStatus.OK).body(topicService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TopicResponse> getSingleTopic(@PathVariable("id") int topicID) throws TopicCustomException {
        return ResponseEntity.status(HttpStatus.OK).body(topicService.getById(topicID));
    }

    @PostMapping("/create")
    public ResponseEntity<TopicResponse> createTopic(@RequestBody TopicRequest topicRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(topicService.create(topicRequest));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<TopicResponse> updateTopic(@PathVariable("id") int topicID, @RequestBody TopicRequest topicRequest)
            throws TopicCustomException {
        return ResponseEntity.status(HttpStatus.OK).body(topicService.update(topicRequest, topicID));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTopic(@PathVariable("id") int topicID) throws TopicCustomException {
        topicService.delete(topicID);
        return ResponseEntity.status(HttpStatus.OK).body("Delete Topic Successfully!");
    }
}
