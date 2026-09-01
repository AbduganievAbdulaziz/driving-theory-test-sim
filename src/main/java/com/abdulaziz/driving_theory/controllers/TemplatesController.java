package com.abdulaziz.driving_theory.controllers;

import com.abdulaziz.driving_theory.dtos.TemplateSummary;
import com.abdulaziz.driving_theory.repositories.TemplatesRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TemplatesController {
    private final TemplatesRepository repository;

    public TemplatesController (TemplatesRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/templates")
    public List<TemplateSummary> getAllTemplates() {
        return repository.findAll();
    }
}