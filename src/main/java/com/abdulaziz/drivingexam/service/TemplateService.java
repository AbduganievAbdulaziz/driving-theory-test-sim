package com.abdulaziz.drivingexam.service;

import com.abdulaziz.drivingexam.dao.TemplateDAO;
import com.abdulaziz.drivingexam.dto.TemplateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TemplateService {
    private final TemplateDAO templateDAO;

    public Optional<TemplateDTO> getTemplate(int id) {
        return templateDAO.getTemplateById(id);
    }
}
