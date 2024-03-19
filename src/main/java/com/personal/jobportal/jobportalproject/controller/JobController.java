package com.personal.jobportal.jobportalproject.controller;
import com.personal.jobportal.jobportalproject.dto.*;
import com.personal.jobportal.jobportalproject.exception.EmployerRoleException;
import com.personal.jobportal.jobportalproject.service.JobsService;
import com.personal.jobportal.jobportalproject.service.TfIdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("api/v1/jobs")
public class JobController {
    @Autowired
    private JobsService jobsService;
    @Autowired
    private TfIdfService tfIdfService;
    @PostMapping("/{employerId}")
    public JobDTO addJob(@RequestHeader("Authorization") String token, @RequestBody JobDTO jobDTO, @RequestParam int employerId) throws EmployerRoleException, Exception {
        try {
            return jobsService.addJob(token, jobDTO, employerId);
        } catch (Exception e){
            throw e;
        }
    }
    @GetMapping
    public List<JobListDTO> getJobList(@RequestHeader("Authorization") String token){
        try{
            return jobsService.getJobList(token);
        }catch (Exception e){
            throw e;
        }
    }
    @PutMapping("/{jobId}")
    public JobDTO updateJob(@RequestParam int jobId, @RequestBody JobDTO jobDTO, @RequestHeader("Authorization") String token) throws EmployerRoleException {
        try {
            return jobsService.updateJob(jobId, jobDTO, token);
        } catch (EmployerRoleException e) {
            throw e;
        }
    }
    @DeleteMapping("/{jobId}")
    public void deleteJob(@RequestParam int jobId, @RequestHeader("Authorization") String token) throws EmployerRoleException {
        try {
            jobsService.deleteJob(jobId, token);
        } catch (EmployerRoleException e) {
            throw e;
        }
    }
    @GetMapping("/{pageIndex}")
    public ResponseEntity<FilterJobDTO> getFilteredJob(@RequestBody FilterPayloadDTO payloadDTO, @RequestParam int pageIndex){
        try {
            return jobsService.getFilteredStory(payloadDTO, pageIndex);
        }catch (Exception e){
            throw e;
        }
    }
    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam(name = "query", required = false) String query, @RequestParam(defaultValue = "0") int pageIndex) {
        try {
            PaginatedJobResultDTO matchingStories = tfIdfService.getMatchingBlog(query, pageIndex);
            if (query == null || query.trim().isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Invalid query");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            if (matchingStories.getSearchResult().isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Invalid page index or no result found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            return ResponseEntity.status(HttpStatus.OK).body(matchingStories);
        }catch (Exception e){
            throw e;
        }
    }
}
