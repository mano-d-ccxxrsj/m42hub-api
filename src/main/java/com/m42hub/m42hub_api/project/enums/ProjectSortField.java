package com.m42hub.m42hub_api.project.enums;

import lombok.Getter;

@Getter
public enum ProjectSortField {
    ID("id"),
    NAME("name"),
    CREATED_AT("createdAt"),
    UPDATED_AT("updatedAt"),
    START_DATE("startDate"),
    END_DATE("endDate"),
    ASC("ASC"),
    DESC("DESC");

    private final String fieldName;

    ProjectSortField(String fieldName) {
        this.fieldName = fieldName;
    }

    public static ProjectSortField fromString(String text) {
        for (ProjectSortField field : ProjectSortField.values()) {
            if (field.fieldName.equalsIgnoreCase(text)) {
                return field;
            }
        }
        return CREATED_AT;
    }
}