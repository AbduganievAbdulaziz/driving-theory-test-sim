package com.abdulaziz.driving_theory.controllers.resolvers;

import com.abdulaziz.driving_theory.domain.Language;
import com.abdulaziz.driving_theory.services.LanguageService;
import org.jspecify.annotations.Nullable;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class LanguageArgumentResolver implements HandlerMethodArgumentResolver {
    private final LanguageService langService;

    public LanguageArgumentResolver(LanguageService langService) {
        this.langService = langService;
    }

    private String getContextLangCode() {
        return LocaleContextHolder.getLocale().getLanguage();
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(Language.class);
    }

    @Override
    public @Nullable Object resolveArgument(MethodParameter parameter,
                                  @Nullable ModelAndViewContainer mavContainer,
                                            NativeWebRequest webRequest,
                                  @Nullable WebDataBinderFactory binderFactory) throws Exception {
        return langService.findLangByCode(getContextLangCode());
    }
}
