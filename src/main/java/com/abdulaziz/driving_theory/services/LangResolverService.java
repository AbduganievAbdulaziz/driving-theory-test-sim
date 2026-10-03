package com.abdulaziz.driving_theory.services;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class LangResolverService {
    private final LanguageService langService;

    public LangResolverService(LanguageService langService) {
        this.langService = langService;
    }

    public Locale getCurrentLocale() {
        return LocaleContextHolder.getLocale();
    }

    public
}
