package com.abdulaziz.driving_theory.controllers;

import com.abdulaziz.driving_theory.domain.Language;
import com.abdulaziz.driving_theory.domain.QuestionPosition;
import com.abdulaziz.driving_theory.services.ExamAttemptService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/templates")
public class ExamAttemptController {
    private final ExamAttemptService attemptService;

    public ExamAttemptController(ExamAttemptService attemptService) {
        this.attemptService = attemptService;
    }

    @GetMapping(value = {"/{template_id}/questions",
                         "/{template_id}/questions/{question_position}"})
    public String initExamAttempt(@PathVariable Integer template_id,
                                  @PathVariable(required = false) Integer question_position,
                                  Language language,
                                  Model model) {
        if (question_position == null) {
            return "redirect:/templates/" + template_id + "/questions/1";
        }
        QuestionPosition qPosition = new QuestionPosition(question_position);

        model.addAttribute("question", attemptService.getQuestionByPosition(template_id, qPosition, language));
        model.addAttribute("qPosition", qPosition.value());
        model.addAttribute("questionCount", attemptService.getQuestionCount(template_id, language));

        return "exam_attempt";
    }
}
