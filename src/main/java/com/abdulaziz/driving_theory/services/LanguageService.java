package com.abdulaziz.driving_theory.services;

import com.abdulaziz.driving_theory.domain.Language;
import com.abdulaziz.driving_theory.repositories.LanguageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LanguageService {
    private static final String DEFAULT_LANG_CODE = "uz";
    private final LanguageRepository repository;

    public LanguageService(LanguageRepository repository) {
        this.repository = repository;
    }

    public Language getDefaultLang() {
        return repository.findByCode(DEFAULT_LANG_CODE)
                .orElseThrow(() -> new IllegalStateException("Default language 'uz' not found"));
    }

    public List<Language> getLangChoices() {
        return repository.findAll();
    }

    public Language findLangByCode(String langCode) {
        return repository.findByCode(langCode)
                .orElseGet(this::getDefaultLang);
    }
}
