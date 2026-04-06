package com.abdulaziz.drivingexam.controller;

import com.abdulaziz.drivingexam.dto.TemplateDTO;
import com.abdulaziz.drivingexam.service.TemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/template")
public class TemplateController {
    private final TemplateService templateService;

    @GetMapping("/{id}")
    public TemplateDTO getTemplateById(@PathVariable int id) {
        Optional<TemplateDTO> template = templateService.getTemplate(id);

        return template.get();
    }
}
