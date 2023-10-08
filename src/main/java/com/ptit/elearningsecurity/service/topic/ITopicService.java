package com.ptit.elearningsecurity.service.topic;

import com.ptit.elearningsecurity.data.request.TopicRequest;
import com.ptit.elearningsecurity.data.response.TopicResponse;
import com.ptit.elearningsecurity.exception.TopicCustomException;

import java.util.List;

public interface ITopicService {
    List<TopicResponse> getAll();
    TopicResponse getById(int topicID) throws TopicCustomException;
    TopicResponse create(TopicRequest topicRequest);
    TopicResponse update(TopicRequest topicRequest, int topicID) throws TopicCustomException;
    void delete(int topicID) throws TopicCustomException;
}
