package com.abdulaziz.driving_theory.repositories;

import com.abdulaziz.driving_theory.domain.Language;
import com.abdulaziz.driving_theory.domain.QuestionPosition;
import com.abdulaziz.driving_theory.dtos.OptionView;
import com.abdulaziz.driving_theory.dtos.QuestionContent;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class QuestionRepository {
    private final JdbcTemplate jdbc;

    public QuestionRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public int findQuestionCount(int templateId, Language lang) {
        String sql = """
                SELECT count(*) question_count
                  FROM template_questions tq
                  JOIN question_contents qc
                    ON qc.question_id = tq.question_id
                 WHERE tq.template_id = ?
                   and qc.lang_id = ?
                """;

        return jdbc.queryForObject(sql,
                (rs, rowNum) -> rs.getInt("question_count"),
                templateId,
                lang.id()
        );
    }

    public Optional<QuestionContent> findContentByPosition(int templateId, QuestionPosition position, Language lang) {
        String sql = """
                SELECT qc.question_id, qc.question_text, qc.image_url
                  FROM template_questions tq
                  JOIN question_contents qc
                    ON qc.question_id = tq.question_id
                 WHERE tq.template_id = ?
                   and qc.lang_id = ?
                 ORDER BY tq.display_order
                 OFFSET ?
                 FETCH FIRST ROW ONLY
                """;

        try {
            return Optional.of(jdbc.queryForObject(sql,
                            (rs, rowNum) ->
                                    new QuestionContent(
                                            rs.getInt("question_id"),
                                            rs.getString("question_text"),
                                            rs.getString("image_url")
                                    ),
                            templateId,
                            lang.id(),
                            position.value() - 1
                    )
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<OptionView> findOptionsByQuestionId(int questionId, Language lang) {
        String sql = """
                SELECT oc.option_id, oc.option_text
                  FROM options o
                  JOIN option_contents oc
                    ON oc.option_id = o.option_id
                 WHERE o.question_id = ?
                   and oc.lang_id = ?
                 ORDER BY o.display_order
                """;

        return jdbc.query(sql,
                (rs, rowNum) ->
                        new OptionView(
                                rs.getInt("option_id"),
                                rs.getString("option_text")
                        ),
                questionId,
                lang.id()
        );
    }
}
