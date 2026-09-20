package com.abdulaziz.driving_theory.services;

import com.abdulaziz.driving_theory.dtos.LanguageChoice;
import com.abdulaziz.driving_theory.repositories.LanguageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LanguageService {
    private final LanguageRepository repository;

    public LanguageService(LanguageRepository repository) {
        this.repository = repository;
    }

    public List<LanguageChoice> getLangChoices() {
        return repository.findAll();
    }
}
