package com.m42hub.m42hub_api.file.dto.response;

public record ImgBBResponse(
        ImgBBData data,
        boolean success,
        int status
) {
}

