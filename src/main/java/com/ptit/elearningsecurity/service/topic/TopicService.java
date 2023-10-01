package com.ptit.elearningsecurity.service.topic;

import com.ptit.elearningsecurity.common.DataUtils;
import com.ptit.elearningsecurity.data.mapper.TopicMapper;
import com.ptit.elearningsecurity.data.request.TopicRequest;
import com.ptit.elearningsecurity.data.response.TopicResponse;
import com.ptit.elearningsecurity.entity.discuss.Topic;
import com.ptit.elearningsecurity.exception.TopicCustomException;
import com.ptit.elearningsecurity.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TopicService implements ITopicService{

    private final TopicRepository topicRepository;
    private final TopicMapper topicMapper;

    @Override
    public List<TopicResponse> getAll() {
        return topicMapper.topicResponseList(topicRepository.findAll());
    }

    @Override
    public TopicResponse getById(int topicID) throws TopicCustomException {
        Optional<Topic> topicOptional = topicRepository.findById(topicID);
        if(topicOptional.isEmpty()) {
            throw new TopicCustomException("Topic Not Found", DataUtils.ERROR_TOPIC_NOT_FOUND);
        }
        return topicMapper.toResponse(topicOptional.get());
    }

    @Override
    public TopicResponse create(TopicRequest topicRequest) {
         return topicMapper.toResponse(
                 topicRepository.save(topicMapper.toPojo(topicRequest)
                 ));
    }

    @Override
    public TopicResponse update(TopicRequest topicRequest, int topicID) throws TopicCustomException {
        Optional<Topic> topicOptional = topicRepository.findById(topicID);
        if(topicOptional.isEmpty()) {
            throw new TopicCustomException("Topic Not Found", DataUtils.ERROR_TOPIC_NOT_FOUND);
        }
        Topic topic = topicOptional.get();
        if(Objects.nonNull(topicRequest.getName()) && !"".equalsIgnoreCase(topic.getName())) {
            topic.setName(topicRequest.getName());
        }

        topic.setUpdatedAt(Instant.now());
        topicRepository.save(topic);
        return topicMapper.toResponse(topic);
    }

    @Override
    public void delete(int topicID) throws TopicCustomException {
        Optional<Topic> topicOptional = topicRepository.findById(topicID);
        if(topicOptional.isEmpty()) {
            throw new TopicCustomException("Topic Not Found", DataUtils.ERROR_TOPIC_NOT_FOUND);
        }
        topicRepository.delete(topicOptional.get());
    }
}
