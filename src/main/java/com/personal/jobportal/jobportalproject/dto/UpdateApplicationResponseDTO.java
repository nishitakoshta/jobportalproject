package com.personal.jobportal.jobportalproject.dto;
import com.personal.jobportal.jobportalproject.enums.ApplicationStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateApplicationResponseDTO {
    private ApplicationStatusEnum status;
}
