package com.personal.jobportal.jobportalproject.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FilterPayloadDTO {
    private List<String> title;
    private List<String> category;
    private List<String> location;
    private String sortType;
}
