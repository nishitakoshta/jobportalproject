package com.personal.jobportal.jobportalproject.dto;
import com.personal.jobportal.jobportalproject.entity.Jobs;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FilterJobDTO {
    private List<Jobs> searchResult;
    private boolean pagination;
    private int pageIndex;
}
