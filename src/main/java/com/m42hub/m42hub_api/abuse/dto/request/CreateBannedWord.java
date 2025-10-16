package com.m42hub.m42hub_api.abuse.dto.request;

import java.util.List;

public record CreateBannedWord(List<String> words) {}