package com.abdulaziz.driving_theory.repositories;

import com.abdulaziz.driving_theory.domain.Language;
import com.abdulaziz.driving_theory.dtos.LanguageChoice;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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

    public Optional<Language> findByCode(String langCode) {
        String sql = """
                SELECT l.lang_id, l.lang_code, l.name
                  FROM languages l
                 WHERE l.lang_code = ?
                 ORDER BY 1
                """;
        try {
            return Optional.of(jdbc.queryForObject(sql, (rs, rowNum) ->
                            new Language(
                                    rs.getInt("lang_id"),
                                    rs.getString("lang_code"),
                                    rs.getString("name")
                            ),
                    langCode
            ));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
