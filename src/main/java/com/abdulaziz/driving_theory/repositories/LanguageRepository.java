package com.abdulaziz.driving_theory.repositories;

import com.abdulaziz.driving_theory.domain.Language;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class LanguageRepository {
    private final JdbcTemplate jdbc;
    private final RowMapper<Language> languageRowMapper = (rs, rowNum) ->
            new Language(
                    rs.getInt("lang_id"),
                    rs.getString("lang_code"),
                    rs.getString("name")
            );

    public LanguageRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<Language> findAll() {
        String sql = """
                SELECT l.lang_id, l.lang_code, l.name
                  FROM languages l
                 ORDER BY 1
                """;

        return jdbc.query(sql, languageRowMapper);
    }

    public Optional<Language> findByCode(String langCode) {
        String sql = """
                SELECT l.lang_id, l.lang_code, l.name
                  FROM languages l
                 WHERE l.lang_code = ?
                """;
        try {
            return Optional.of(jdbc.queryForObject(sql, languageRowMapper, langCode));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<Language> findById(int langId) {
        String sql = """
                SELECT l.lang_id, l.lang_code, l.name
                  FROM languages l
                 WHERE l.lang_id = ?
                """;
        try {
            return Optional.of(jdbc.queryForObject(sql, languageRowMapper, langId));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
