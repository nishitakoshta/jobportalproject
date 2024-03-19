package com.personal.jobportal.jobportalproject.dto;
import com.personal.jobportal.jobportalproject.enums.ApplicationStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationDetailDTO {
    private int applicationId;
    private int jobId;
    private int jobSeekerId;
    private String jobSeekerName;
}
