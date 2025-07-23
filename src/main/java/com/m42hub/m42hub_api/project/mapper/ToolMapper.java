package com.m42hub.m42hub_api.project.mapper;

import com.m42hub.m42hub_api.project.dto.request.ToolRequest;
import com.m42hub.m42hub_api.project.dto.response.ToolResponse;
import com.m42hub.m42hub_api.project.entity.Tool;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ToolMapper {

    public static Tool toTool(ToolRequest request) {
        return Tool
                .builder()
                .name(request.name())
                .description(request.description())
                .hexColor(request.hexColor())
                .build();
    }

    public static ToolResponse toToolResponse(Tool tool) {
        return ToolResponse
                .builder()
                .id(tool.getId())
                .name(tool.getName())
                .description(tool.getDescription())
                .hexColor(tool.getHexColor())
                .build();
    }

}
