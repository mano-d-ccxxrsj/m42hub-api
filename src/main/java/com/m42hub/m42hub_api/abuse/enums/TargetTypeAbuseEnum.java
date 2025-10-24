package com.m42hub.m42hub_api.abuse.enums;

/**
 * Enum utilizado para adicionar um tipo específico de TargetType. 
 * Sempre que for adicionado um novo módulo/item denunciável deve ser adicionado aqui no ENUM
 */
public enum TargetTypeAbuseEnum {
    USER("User"),
    PROJECT("Project");

    private final String displayName;

    private TargetTypeAbuseEnum(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
