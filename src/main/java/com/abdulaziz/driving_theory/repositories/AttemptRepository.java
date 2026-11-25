package com.abdulaziz.driving_theory.repositories;

import com.abdulaziz.driving_theory.domain.Language;
import com.abdulaziz.driving_theory.domain.QuestionOrder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AttemptRepository {
    private final JdbcTemplate jdbc;

    public AttemptRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Integer addNewAttempt(int templateId, Language language) {
        String sql = """
                INSERT INTO exam_attempts (template_id, lang_id)
                VALUES (?, ?)
                RETURNING attempt_id
                """;

        return jdbc.queryForObject(sql, Integer.class, templateId, language.id());
    }

    public void prepareProgress(int attemptId, int templateId) {
        String sql = """
                INSERT INTO attempt_progress (attempt_id, question_id, question_order)
                SELECT ?, t.question_id, t.display_order
                  FROM template_questions t
                 WHERE t.template_id = ?
                """;

        jdbc.update(sql, attemptId, templateId);
    }

    public Integer findAttemptLangId(int attemptId) {
        String sql = """
                SELECT t.lang_id
                  FROM exam_attempts t
                 WHERE t.attempt_id = ?
                """;

        return jdbc.queryForObject(
                sql,
                Integer.class,
                attemptId
        );
    }

    public int findQuestionCount(int attemptId) {
        String sql = """
                SELECT count(*) question_count
                  FROM exam_attempts ea
                  JOIN attempt_progress ap
                    ON ap.attempt_id = ea.attempt_id
                 WHERE ea.attempt_id = ?
                """;

        return jdbc.queryForObject(sql,
                (rs, rowNum) -> rs.getInt("question_count"),
                attemptId
        );
    }

    public Integer findQuestionIdByOrder(int attemptId, QuestionOrder order) {
        String sql = """
                SELECT t.question_id
                  FROM attempt_progress t
                 WHERE t.attempt_id = ?
                 ORDER BY t.question_order
                 OFFSET ?
                 FETCH FIRST ROW ONLY
                """;

        return jdbc.queryForObject(
                sql,
                Integer.class,
                attemptId,
                order.value() - 1
        );
    }

    public Integer findPickedOptionId(int attemptId, QuestionOrder order) {
        String sql = """
                SELECT t.picked_option_id
                  FROM attempt_progress t
                 WHERE t.attempt_id = ?
                 ORDER BY t.question_order
                 OFFSET ?
                 FETCH FIRST ROW ONLY
                """;

        return jdbc.queryForObject(
                sql,
                Integer.class,
                attemptId,
                order.value() - 1
        );
    }

    public Integer findOptionQuestionId(int optionId) {
        String sql = """
                SELECT t.question_id
                  FROM options t
                 WHERE t.option_id = ?
                """;

        return jdbc.queryForObject(sql, Integer.class, optionId);
    }

    public Boolean isOptionCorrect(int optionId) {
        String sql = """
                SELECT t.is_correct
                  FROM options t
                 WHERE t.option_id = ?
                """;

        return jdbc.queryForObject(sql, Boolean.class, optionId);
    }
}
