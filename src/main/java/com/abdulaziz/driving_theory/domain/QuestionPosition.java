package com.abdulaziz.driving_theory.domain;

public record QuestionPosition (int value) {
    public QuestionPosition {
        if (value < 1) {
            throw new IllegalArgumentException("Question position must be greater than zero");
        }
    }

    public static QuestionPosition first() {
        return new QuestionPosition(1);
    }
}
