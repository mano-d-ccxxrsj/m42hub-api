package com.m42hub.m42hub_api.abuse.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

@NotBlank
public record CreateBannedWord(List<String> words) {}