package com.personal.jobportal.jobportalproject.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobDTO {
    private int jobId;
    private String title;
    private String description;
    private List<String> requirements;
    private String location;
    private String category;
    private LocalDateTime createdOn;
    private Double score;
}
