package com.m42hub.m42hub_api.project.mapper;

import com.m42hub.m42hub_api.project.dto.request.ToolRequest;
import com.m42hub.m42hub_api.project.dto.request.TopicRequest;
import com.m42hub.m42hub_api.project.dto.response.TopicResponse;
import com.m42hub.m42hub_api.project.entity.Topic;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TopicMapper {

    public static Topic toTopic(TopicRequest request) {
        return Topic
                .builder()
                .name(request.name())
                .description(request.description())
                .hexColor(request.hexColor())
                .build();
    }

    public static TopicResponse toTopicResponse(Topic topic) {
        return TopicResponse
                .builder()
                .id(topic.getId())
                .name(topic.getName())
                .description(topic.getDescription())
                .hexColor(topic.getHexColor())
                .build();
    }

}
