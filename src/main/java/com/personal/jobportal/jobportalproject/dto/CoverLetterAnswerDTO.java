package com.personal.jobportal.jobportalproject.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoverLetterAnswerDTO {
    private int questionId;
    private String answer;
}
