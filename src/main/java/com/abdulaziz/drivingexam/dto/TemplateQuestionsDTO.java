package com.abdulaziz.drivingexam.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TemplateQuestionsDTO {
    private List<QuestionDTO> questions;

    public void appendQuestion(QuestionDTO question) {
        if (this.questions == null) {
            this.questions = new ArrayList<>();
        }

        this.questions.add(question);
    }
}
