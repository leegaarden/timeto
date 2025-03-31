package com.timeto.domain.enums;

public enum Color {
    DEFAULT("기본"),
    RED01("빨강01"),
    RED02("빨강02"),
    PURPLE01("보라01"),
    PURPLE02("보라02"),
    BLUE01("파랑01"),
    BLUE02("파랑02"),
    GREEN01("초록01"),
    GREEN02("초록02"),
    BROWN01("갈색01"),
    BROWN02("갈색02");

    private final String description;

    Color(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}