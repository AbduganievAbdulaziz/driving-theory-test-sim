package com.abdulaziz.driving_theory.repositories;

import com.abdulaziz.driving_theory.dtos.TemplateSummary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TemplatesRepository {
    private final JdbcTemplate jdbc;

    public TemplatesRepository(JdbcTemplate jdbc) {
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
}