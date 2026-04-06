package com.abdulaziz.drivingexam.dto;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class TemplateDTO {
    private Integer templateId;
    private String code;
    private String status;
}