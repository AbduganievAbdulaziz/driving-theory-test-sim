package com.abdulaziz.driving_theory.repositories.util;

import com.abdulaziz.driving_theory.repositories.LanguageRepository;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class LangResolver {
    private final LanguageRepository langRepo;

    public LangResolver(LanguageRepository langRepo) {
        this.langRepo = langRepo;
    }

    public String getContextLang() {
        return LocaleContextHolder.getLocale().getLanguage();
    }

    public int getContextLangId() {
        return langRepo.findByCode(getContextLang()).id();
    }
}
