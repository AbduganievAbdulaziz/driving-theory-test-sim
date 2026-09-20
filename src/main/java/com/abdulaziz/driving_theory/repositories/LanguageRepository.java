package com.abdulaziz.driving_theory.repositories;

import com.abdulaziz.driving_theory.dtos.LanguageChoice;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LanguageRepository {
    private final JdbcTemplate jdbc;

    public LanguageRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<LanguageChoice> findAll() {
        String sql = """
                SELECT l.lang_id, l.name
                  FROM languages l
                 ORDER BY 1
                """;

        return jdbc.query(sql, ((rs, rowNum) ->
                new LanguageChoice(
                        rs.getInt("lang_id"),
                        rs.getString("name")
                )
        ));
    }
}
