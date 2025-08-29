package com.m42hub.m42hub_api.user.dto.request;

public record UserPasswordChangeRequest(
        String oldPassword,
        String newPassword
) {
}