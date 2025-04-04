package com.timeto.domain.enums;

public enum Level {
    HIGH("상"),
    MIDDLE("중"),
    LOW("하");

    private final String description;

    Level(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String toDisplayText() {
        return switch (this) {
            case HIGH -> "상";
            case MIDDLE -> "중";
            case LOW -> "하";
            default -> this.name();
        };
    }
}