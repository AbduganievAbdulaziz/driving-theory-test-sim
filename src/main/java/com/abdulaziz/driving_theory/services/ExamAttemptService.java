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
    private final QuestionRepository questionRepository;
    private final AttemptRepository attemptRepository;

    public ExamAttemptService(QuestionRepository questionRepository, AttemptRepository attemptRepository) {
        this.questionRepository = questionRepository;
        this.attemptRepository = attemptRepository;
    }

    public int getQuestionCount(int attemptId) {
        return attemptRepository.findQuestionCount(attemptId);
    }

    private QuestionContent getQuestionContentByOrder(int attemptId, int langId, QuestionOrder order) {
        int questionId = attemptRepository.findQuestionIdByOrder(attemptId, order);

        return questionRepository.findQuestionContentById(questionId, langId)
                .orElseThrow(() -> new IllegalArgumentException("Question was not found"));
    }

    public QuestionView getQuestionByOrder(int attemptId,
                                           QuestionOrder order) {
        int langId = attemptRepository.findAttemptLangId(attemptId);
        QuestionContent qContent = getQuestionContentByOrder(attemptId, langId, order);
        List<OptionView> qOptions = questionRepository.findOptionsByQuestionId(qContent.id(), langId);


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
}
