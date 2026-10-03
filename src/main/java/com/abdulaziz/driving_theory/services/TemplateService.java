package com.abdulaziz.driving_theory.services;

import com.abdulaziz.driving_theory.dtos.QuestionContent;
import com.abdulaziz.driving_theory.dtos.TemplateSummary;
import com.abdulaziz.driving_theory.repositories.TemplateRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TemplateService {
    private final TemplateRepository repository;

    public TemplateService(TemplateRepository repository) {
        this.repository = repository;
    }

    public List<TemplateSummary> findAll() {
        return repository.findAll();
    }

    public List<QuestionContent> getQuestionsByTemplateId(int templateId) {
        return repository.getQuestionsByTemplateId(templateId);
    }
}
