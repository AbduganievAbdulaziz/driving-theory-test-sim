package com.abdulaziz.driving_theory.controllers;

import com.abdulaziz.driving_theory.domain.Language;
import com.abdulaziz.driving_theory.domain.QuestionOrder;
import com.abdulaziz.driving_theory.services.ExamAttemptService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ExamAttemptController {
    private final ExamAttemptService attemptService;

    public record validateAnswerDTO(Integer attemptId, Integer questionOrder, Integer optionId) {}

    public ExamAttemptController(ExamAttemptService attemptService) {
        this.attemptService = attemptService;
    }

    @PostMapping("/attempt-template/{template_id}")
    public String createTemplateAttempt(@PathVariable Integer template_id,
                                        Language language) {
        return "redirect:/attempts/" + attemptService.createExamAttempt(template_id, language) + "/questions/1";
    }

    @GetMapping(value = {"/attempts/{attempt_id}/questions",
                         "/attempts/{attempt_id}/questions/{question_order}"})
    public String getAttemptContent(@PathVariable Integer attempt_id,
                                    @PathVariable(required = false) Integer question_order,
                                    Model model) {
        if (question_order == null) {
            return "redirect:/attempts/" + attempt_id + "/questions/1";
        }
        QuestionOrder qOrder = new QuestionOrder(question_order);

        model.addAttribute("question", attemptService.getQuestionByOrder(attempt_id, qOrder));
        model.addAttribute("questionCount", attemptService.getQuestionCount(attempt_id));
        model.addAttribute("qOrder", qOrder.value());

        return "exam_attempt";
    }

    @GetMapping("/validate-answer")
    @ResponseBody
    public String validateAnswer(@RequestBody validateAnswerDTO requestData) {
        QuestionOrder qOrder = new QuestionOrder(requestData.questionOrder());
        return attemptService.isOptionCorrect(requestData.optionId(), requestData.attemptId(), qOrder) ? "Y" : "N";
    }
}
