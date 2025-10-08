package com.m42hub.m42hub_api.contribution.enums;

import lombok.Getter;

@Getter
public enum ContributionSortField {
    ID("id"),
    NAME("name"),
    USER("user"),
    STATUS("status"),
    TYPE("type"),
    APPROVED_AT("approvedAt"),
    SUBMITTED_AT("submittedAt"),
    ASC("ASC"),
    DESC("DESC"),
    AMOUNT("amount"),
    PLATFORM("platform"),
    ;

    private final String fieldName;

    ContributionSortField(String fieldName) {
        this.fieldName = fieldName;
    }

    public static ContributionSortField fromString(String text) {
        for (ContributionSortField field : ContributionSortField.values()) {
            if (field.fieldName.equalsIgnoreCase(text)) {
                return field;
            }
        }
        return APPROVED_AT;
    }
}