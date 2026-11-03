package com.abdulaziz.driving_theory.controllers;

import com.abdulaziz.driving_theory.domain.Language;
import com.abdulaziz.driving_theory.domain.QuestionOrder;
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
                         "/{template_id}/questions/{question_order}"})
    public String initExamAttempt(@PathVariable Integer template_id,
                                  @PathVariable(required = false) Integer question_order,
                                  Language language,
                                  Model model) {
        if (question_order == null) {
            return "redirect:/templates/" + template_id + "/questions/1";
        }
        QuestionOrder qOrder = new QuestionOrder(question_order);

        model.addAttribute("question", attemptService.getQuestionByOrder(template_id, qOrder, language));
        model.addAttribute("qOrder", qOrder.value());
        model.addAttribute("questionCount", attemptService.getQuestionCount(template_id, language));

        return "exam_attempt";
    }
}
