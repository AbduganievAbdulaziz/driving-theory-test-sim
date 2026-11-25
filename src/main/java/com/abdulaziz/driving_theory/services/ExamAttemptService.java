package com.abdulaziz.driving_theory.services;

import com.abdulaziz.driving_theory.domain.Language;
import com.abdulaziz.driving_theory.domain.QuestionOrder;
import com.abdulaziz.driving_theory.dtos.OptionView;
import com.abdulaziz.driving_theory.dtos.QuestionContent;
import com.abdulaziz.driving_theory.dtos.QuestionView;
import com.abdulaziz.driving_theory.repositories.AttemptRepository;
import com.abdulaziz.driving_theory.repositories.QuestionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamAttemptService {
    private final LanguageService languageService;
    private final QuestionRepository questionRepository;
    private final AttemptRepository attemptRepository;

    public ExamAttemptService(LanguageService languageService,
                           QuestionRepository questionRepository,
                           AttemptRepository  attemptRepository) {
        this.languageService = languageService;
        this.questionRepository = questionRepository;
        this.attemptRepository = attemptRepository;
    }

    public int getQuestionCount(int attemptId) {
        return attemptRepository.findQuestionCount(attemptId);
    }

    public QuestionView getQuestionByOrder(int attemptId,
                                           QuestionOrder order) {
        int questionId = attemptRepository.findQuestionIdByOrder(attemptId, order);
        Language attemptLang = findAttemptLanguage(attemptId);

        QuestionContent qContent = questionRepository.findQuestionContentById(questionId, attemptLang)
                .orElseThrow(() -> new IllegalArgumentException("Question was not found"));
        List<OptionView> qOptions = questionRepository.findOptionsByQuestionId(qContent.id(), attemptLang);

        return new QuestionView(
                qContent.id(),
                qContent.text(),
                qContent.imgUrl(),
                qOptions);
    }

    public Integer createExamAttempt(int templateId, Language language) {
        Integer newAttemptId = attemptRepository.addNewAttempt(templateId, language);
        attemptRepository.prepareProgress(newAttemptId, templateId);

        return newAttemptId;
    }

    private Language findAttemptLanguage(int attemptId) {
        int langId = attemptRepository.findAttemptLangId(attemptId);
        return languageService.findLangById(langId);
    }
}
