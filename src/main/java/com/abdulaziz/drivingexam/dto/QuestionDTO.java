package com.abdulaziz.drivingexam.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class QuestionDTO {
    private Integer questionId;
    private String status;
    private String questionText;
    private String explanationText;
    private String imageUrl;
    private String videoUrl;
}
