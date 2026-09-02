package com.abdulaziz.driving_theory.controllers;

import com.abdulaziz.driving_theory.dtos.TemplateSummary;
import com.abdulaziz.driving_theory.repositories.TemplateRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/")
public class TemplateController {
    private final TemplateRepository repository;

    public TemplateController(TemplateRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public String getAllTemplates(Model model) {
        List<TemplateSummary> templates = repository.findAll();
        model.addAttribute("templates", templates);

        return "template_list";
    }
}