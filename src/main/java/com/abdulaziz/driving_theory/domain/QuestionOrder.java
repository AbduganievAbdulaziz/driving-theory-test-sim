package com.abdulaziz.driving_theory.domain;

public record QuestionOrder(int value) {
    public QuestionOrder {
        if (value < 1) {
            throw new IllegalArgumentException("Question order must be greater than zero");
        }
    }

    public static QuestionOrder first() {
        return new QuestionOrder(1);
    }
}
