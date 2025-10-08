package com.m42hub.m42hub_api.abuse.enums;

public enum AbuseStatusEnum {
    OPEN("Open"),
    IN_REVIEW("In Review"),
    CLOSED("Closed"),
    RESOLVED("Resolved");

    private final String displayName;

    private AbuseStatusEnum(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

