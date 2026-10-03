package com.abdulaziz.driving_theory.controllers;

import com.abdulaziz.driving_theory.dtos.LanguageChoice;
import com.abdulaziz.driving_theory.dtos.QuestionContent;
import com.abdulaziz.driving_theory.dtos.TemplateSummary;
import com.abdulaziz.driving_theory.services.LanguageService;
import com.abdulaziz.driving_theory.services.TemplateService;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

@Controller
@RequestMapping("/templates")
public class TemplateController {
    private final LanguageService langService;
    private final TemplateService templateService;
    private final Logger logger = Logger.getLogger(this.getClass().getName());

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
                                       Locale locale,
                                       Model model) {
        Locale current = LocaleContextHolder.getLocale();
        logger.info("Incoming locale: " + locale.getLanguage());
        logger.info("Current locale from context: " + current.getLanguage());
        List<QuestionContent> questions = templateService.getQuestionsByTemplateId(template_id);
        model.addAttribute("questions", questions);

        return "template_questions";
    }
}