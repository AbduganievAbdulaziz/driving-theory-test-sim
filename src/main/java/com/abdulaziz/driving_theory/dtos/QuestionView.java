package com.abdulaziz.driving_theory.dtos;

import java.util.List;

public record QuestionView (
                 int id,
              String text,
              String imgUrl,
    List<OptionView> options) {
}
