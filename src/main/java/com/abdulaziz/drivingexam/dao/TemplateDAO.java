package com.abdulaziz.drivingexam.dao;

import com.abdulaziz.drivingexam.dto.TemplateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class TemplateDAO {
    private final JdbcClient jdbcClient;

    private static final String GET_BY_ID = """
                                           select t.template_id,
                                                  t.code,
                                                  t.status
                                             from templates t
                                            where t.template_id = ?
                                           """;

    public Optional<TemplateDTO> getTemplateById(int id) {
        return jdbcClient.sql(GET_BY_ID)
                .param(id)
                .query((rs, rowNum) -> new TemplateDTO(rs.getInt("template_id"),
                                                                    rs.getString("code"),
                                                                    rs.getString("status")))
                .optional();
    }
}
