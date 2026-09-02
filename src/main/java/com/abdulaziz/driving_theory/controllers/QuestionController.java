package com.abdulaziz.driving_theory.controllers;

import com.abdulaziz.driving_theory.dtos.QuestionContent;
import com.abdulaziz.driving_theory.repositories.TemplateRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/template_questions")
public class QuestionController {
    private final TemplateRepository repository;

    public QuestionController(TemplateRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public String getQuestion(@RequestParam Integer template_id,
                              Model model) {
        List<QuestionContent> questions = repository.getQuestionsByTemplateId(template_id);
        model.addAttribute("questions", questions);

        return "template_questions";
    }
}
