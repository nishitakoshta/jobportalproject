package com.personal.jobportal.jobportalproject.service;
import com.personal.jobportal.jobportalproject.dto.FilterJobDTO;
import com.personal.jobportal.jobportalproject.dto.FilterPayloadDTO;
import com.personal.jobportal.jobportalproject.dto.JobDTO;
import com.personal.jobportal.jobportalproject.dto.JobListDTO;
import com.personal.jobportal.jobportalproject.exception.EmployerRoleException;
import org.springframework.http.ResponseEntity;

import java.util.List;
public interface JobsService {
    JobDTO addJob(String token, JobDTO jobDTO, int employerId) throws EmployerRoleException;
    List<JobListDTO> getJobList(String token);
    JobDTO updateJob(int jobId, JobDTO jobDTO, String token) throws EmployerRoleException;
    void deleteJob(int jobId, String token) throws EmployerRoleException;
    ResponseEntity<FilterJobDTO> getFilteredStory(FilterPayloadDTO filterPayload, int pageIndex);
}
