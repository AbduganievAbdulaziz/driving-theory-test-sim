package com.abdulaziz.driving_theory.repositories;

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

    public Optional<QuestionContent> findQuestionContentById(int questionId, int langId) {
        String sql = """
                SELECT t.question_id, t.question_text, t.image_url
                  FROM question_contents t
                 WHERE t.question_id = ?
                   and t.lang_id = ?
                """;

        try {
            return Optional.of(jdbc.queryForObject(sql,
                            (rs, rowNum) ->
                                    new QuestionContent(
                                            rs.getInt("question_id"),
                                            rs.getString("question_text"),
                                            rs.getString("image_url")
                                    ),
                    questionId,
                           langId
                    )
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<OptionView> findOptionsByQuestionId(int questionId, int langId) {
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
                langId
        );
    }
}
