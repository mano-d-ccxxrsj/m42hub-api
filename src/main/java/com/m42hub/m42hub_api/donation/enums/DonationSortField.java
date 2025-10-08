package com.m42hub.m42hub_api.donation.enums;

import lombok.Getter;

@Getter
public enum DonationSortField {
    ID("id"),
    NAME("name"),
    USER("user"),
    STATUS("status"),
    TYPE("type"),
    DONATED_AT("donatedAt"),
    SUBMITTED_AT("submittedAt"),
    ASC("ASC"),
    DESC("DESC"),
    AMOUNT("amount"),
    PLATFORM("platform"),
    ;

    private final String fieldName;

    DonationSortField(String fieldName) {
        this.fieldName = fieldName;
    }

    public static DonationSortField fromString(String text) {
        for (DonationSortField field : DonationSortField.values()) {
            if (field.fieldName.equalsIgnoreCase(text)) {
                return field;
            }
        }
        return DONATED_AT;
    }
}