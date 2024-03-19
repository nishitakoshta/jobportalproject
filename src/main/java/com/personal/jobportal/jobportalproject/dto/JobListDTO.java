package com.personal.jobportal.jobportalproject.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobListDTO {
    private int jobId;
    private String title;
    private String description;
    private String location;
    private LocalDateTime createdOn;
}
