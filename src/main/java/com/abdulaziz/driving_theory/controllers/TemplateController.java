package com.abdulaziz.driving_theory.controllers;

import com.abdulaziz.driving_theory.domain.Language;
import com.abdulaziz.driving_theory.dtos.LanguageChoice;
import com.abdulaziz.driving_theory.dtos.QuestionContent;
import com.abdulaziz.driving_theory.dtos.TemplateSummary;
import com.abdulaziz.driving_theory.services.LanguageService;
import com.abdulaziz.driving_theory.services.TemplateService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/templates")
public class TemplateController {
    private final LanguageService langService;
    private final TemplateService templateService;

    public TemplateController(LanguageService langService, TemplateService templateService) {
        this.langService = langService;
        this.templateService = templateService;
    }

    @GetMapping
    public String getAllTemplates(Model model) {
        List<TemplateSummary> templates = templateService.findAll();
        List<LanguageChoice> langChoices = langService.getLangChoices();
        model.addAttribute("templates", templates);
        model.addAttribute("languages", langChoices);

        return "template_list";
    }

    @GetMapping("/{template_id}/questions")
    public String getTemplateQuestions(@PathVariable Integer template_id,
                                       Language language,
                                       Model model) {
        List<QuestionContent> questions = templateService.getQuestionsByTemplateId(template_id, language);
        model.addAttribute("questions", questions);

        return "template_questions";
    }
}