package com.abdulaziz.driving_theory.controllers;

import com.abdulaziz.driving_theory.domain.Language;
import com.abdulaziz.driving_theory.dtos.QuestionContentOld;
import com.abdulaziz.driving_theory.dtos.TemplateSummary;
import com.abdulaziz.driving_theory.services.LanguageService;
import com.abdulaziz.driving_theory.services.TemplateService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @ModelAttribute
    public void initLangChoices(Language userLang, Model model) {
        model.addAttribute("languages", langService.getLangChoices());
        model.addAttribute("userLang", userLang);
    }

    @GetMapping
    public String getAllTemplates(@RequestParam(required = false) String lang, Model model) {
        if (lang != null) {
            return "redirect:/templates";
        }
        List<TemplateSummary> templates = templateService.findAll();
        model.addAttribute("templates", templates);

        return "template_list";
    }
}