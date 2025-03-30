package com.timeto.domain.enums;

public enum Level {
    HIGH("높음"),
    MIDDLE("중간"),
    LOW("낮음");

    private final String description;

    Level(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}