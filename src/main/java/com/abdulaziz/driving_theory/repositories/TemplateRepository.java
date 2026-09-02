package com.abdulaziz.driving_theory.repositories;

import com.abdulaziz.driving_theory.dtos.QuestionContent;
import com.abdulaziz.driving_theory.dtos.TemplateSummary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TemplateRepository {
    private final int lang_id = 1;
    private final JdbcTemplate jdbc;

    public TemplateRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<TemplateSummary> findAll() {
        String sql = """
                SELECT t.template_id, t.code
                  FROM templates t
                 WHERE t.status = 'A'
                 ORDER BY t.template_id""";

        return jdbc.query(
                sql,
                (rs, rowNum) ->
                        new TemplateSummary(
                                rs.getInt("template_id"),
                                rs.getString("code")
                        )
        );
    }

    public List<QuestionContent> getQuestionsByTemplateId(int templateId) {
        String sql = """
                SELECT qc.question_id, qc.question_text
                  FROM template_questions tq
                  JOIN question_contents qc
                    ON qc.question_id = tq.question_id
                 WHERE tq.template_id = ?
                   AND qc.lang_id = ?
                 ORDER BY tq.display_order
                """;

        return jdbc.query(sql,
                (rs, rowNum) ->
                        new QuestionContent(
                                rs.getInt("question_id"),
                                rs.getString("question_text")
                        ),
                templateId,
                lang_id
        );
    }
}