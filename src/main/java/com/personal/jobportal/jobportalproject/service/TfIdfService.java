package com.personal.jobportal.jobportalproject.service;
import com.personal.jobportal.jobportalproject.dto.PaginatedJobResultDTO;

import java.util.List;
import java.util.Map;
public interface TfIdfService {
    Map<String, Double> calculateTFIDF(String string, List<String> stringList);
    void saveTFIDFScores(Map<String, Double> tfIdfScores, String fieldType, Integer blogId);
    PaginatedJobResultDTO getMatchingBlog(String query, int pageIndex);
}
