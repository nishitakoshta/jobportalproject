package com.personal.jobportal.jobportalproject.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaginatedJobResultDTO {
    private List<JobDTO> searchResult;
    private boolean pagination;
    private int pageIndex;
}
