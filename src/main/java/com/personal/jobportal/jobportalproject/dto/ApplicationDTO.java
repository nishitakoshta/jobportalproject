package com.personal.jobportal.jobportalproject.dto;
import com.personal.jobportal.jobportalproject.enums.ApplicationStatusEnum;
import com.personal.jobportal.jobportalproject.enums.AvailabilityEnum;
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
public class ApplicationDTO {
    private int applicationId;
    private int jobId;
    private int jobSeekerId;
    private String jobSeekerName;
    private List<CoverLetterAnswerDTO> coverLetter;
    private ApplicationStatusEnum status;
    private LocalDateTime appliedOn;
}
