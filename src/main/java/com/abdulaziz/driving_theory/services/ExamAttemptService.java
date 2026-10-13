package com.abdulaziz.driving_theory.services;

import com.abdulaziz.driving_theory.domain.Language;
import com.abdulaziz.driving_theory.domain.QuestionPosition;
import com.abdulaziz.driving_theory.dtos.OptionView;
import com.abdulaziz.driving_theory.dtos.QuestionContent;
import com.abdulaziz.driving_theory.dtos.QuestionView;
import com.abdulaziz.driving_theory.repositories.QuestionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamAttemptService {
    private final QuestionRepository repository;

    public ExamAttemptService(QuestionRepository repository) {
        this.repository = repository;
    }

    public int getQuestionCount(int templateId, Language language) {
        return repository.findQuestionCount(templateId, language);
    }

    public QuestionView getQuestionByPosition(int templateId,
                                              QuestionPosition position,
                                              Language language) {
        QuestionContent qContent = repository.findContentByPosition(templateId, position, language)
                .orElseThrow(() -> new IllegalArgumentException("Question was not found"));
        List<OptionView> qOptions = repository.findOptionsByQuestionId(qContent.id(), language);


        return new QuestionView(
                qContent.id(),
                qContent.text(),
                qContent.imgUrl(),
                qOptions);
    }
}
