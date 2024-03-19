package com.personal.jobportal.jobportalproject.service.impl;
import com.personal.jobportal.jobportalproject.dto.FilterJobDTO;
import com.personal.jobportal.jobportalproject.dto.FilterPayloadDTO;
import com.personal.jobportal.jobportalproject.dto.JobDTO;
import com.personal.jobportal.jobportalproject.dto.JobListDTO;
import com.personal.jobportal.jobportalproject.entity.Jobs;
import com.personal.jobportal.jobportalproject.entity.Users;
import com.personal.jobportal.jobportalproject.exception.EmployerRoleException;
import com.personal.jobportal.jobportalproject.repository.JobRepository;
import com.personal.jobportal.jobportalproject.repository.UserRepository;
import com.personal.jobportal.jobportalproject.service.JobsService;
import com.personal.jobportal.jobportalproject.service.JwtService;
import com.personal.jobportal.jobportalproject.service.TfIdfService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
@Service
public class JobServiceImpl implements JobsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private TfIdfService tfIdfService;
    @Override
    public JobDTO addJob(String token, JobDTO jobDTO, int employerId) throws EmployerRoleException {
        Users employer = userRepository.findById(employerId)
                .orElseThrow(() -> new EntityNotFoundException("Employer not found with id " + employerId));
        Jobs job = new Jobs();
        if (employer.getRole() == 0) {
            job.setEmployer(employer);
        } else {
            throw new EmployerRoleException("Role is not valid, it should be employer");
        }
        job.setTitle(jobDTO.getTitle());
        job.setDescription(jobDTO.getDescription());
        job.setRequirements(jobDTO.getRequirements());
        job.setLocation(jobDTO.getLocation());
        job.setCategory(jobDTO.getCategory());
        job.setCreatedOn(LocalDateTime.now());
        List<Jobs> jobList = jobRepository.findAll();
        List<String> jobTitle = jobList.stream()
                .map(Jobs::getTitle)
                .toList();
        List<String> jobCategory = jobList.stream()
                .map(Jobs::getCategory)
                .toList();
        jobRepository.save(job);
        Map<String, Double> blogContentScore = tfIdfService.calculateTFIDF(job.getTitle(), jobTitle != null ? jobTitle : Collections.singletonList(jobDTO.getTitle()));
        tfIdfService.saveTFIDFScores(blogContentScore, "title", job.getJobId());
        Map<String, Double> blogTagLineScore = tfIdfService.calculateTFIDF(job.getCategory(), jobCategory != null ? jobCategory : Collections.singletonList(jobDTO.getCategory()));
        tfIdfService.saveTFIDFScores(blogTagLineScore, "category", job.getJobId());
        return JobDTO.builder()
                .jobId(job.getJobId())
                .title(job.getTitle())
                .description(job.getDescription())
                .requirements(job.getRequirements())
                .location(job.getLocation())
                .category(job.getCategory())
                .createdOn(job.getCreatedOn())
                .build();
    }
    @Override
    public List<JobListDTO> getJobList(String token){
        List<JobListDTO> jobList = new ArrayList<>();
        Integer userRole = jwtService.extractUserRole(token);
        List<Jobs> jobs = jobRepository.findAll();
        if(userRole == 1){
            for(Jobs job : jobs){
                jobList.add(JobListDTO.builder()
                        .jobId(job.getJobId())
                        .title(job.getTitle())
                        .description(job.getDescription())
                        .location(job.getLocation())
                        .createdOn(job.getCreatedOn())
                        .build()
                );
            }
        }
        return jobList;
    }
    @Override
    public JobDTO updateJob(int jobId, JobDTO jobDTO, String token) throws EmployerRoleException {
        Jobs job = jobRepository.findById(jobId)
                .orElseThrow(EntityNotFoundException::new);
        Integer role = jwtService.extractUserRole(token);
        if(role == 0){
            job.setTitle(jobDTO.getTitle());
            job.setDescription(jobDTO.getDescription());
            job.setRequirements(jobDTO.getRequirements());
            job.setLocation(jobDTO.getLocation());
            job.setCategory(jobDTO.getCategory());
            job.setCreatedOn(LocalDateTime.now());
            List<Jobs> jobList = jobRepository.findAll();
            List<String> jobTitle = jobList.stream()
                    .map(Jobs::getTitle)
                    .toList();
            List<String> jobCategory = jobList.stream()
                    .map(Jobs::getCategory)
                    .toList();
            jobRepository.save(job);
            Map<String, Double> blogContentScore = tfIdfService.calculateTFIDF(job.getTitle(), jobTitle != null ? jobTitle : Collections.singletonList(jobDTO.getTitle()));
            tfIdfService.saveTFIDFScores(blogContentScore, "title", job.getJobId());
            Map<String, Double> blogTagLineScore = tfIdfService.calculateTFIDF(job.getCategory(), jobCategory != null ? jobCategory : Collections.singletonList(jobDTO.getCategory()));
            tfIdfService.saveTFIDFScores(blogTagLineScore, "category", job.getJobId());
            return JobDTO.builder()
                    .jobId(job.getJobId())
                    .title(job.getTitle())
                    .description(job.getDescription())
                    .requirements(job.getRequirements())
                    .location(job.getLocation())
                    .category(job.getCategory())
                    .createdOn(job.getCreatedOn())
                    .build();
        }else {
            throw new EmployerRoleException("Role is not valid, it should be employer");
        }
    }
    @Override
    public void deleteJob(int jobId, String token) throws EmployerRoleException {
        jobRepository.findById(jobId)
                .orElseThrow(EntityNotFoundException::new);
        Integer userRole = jwtService.extractUserRole(token);
        if(userRole == 0){
            jobRepository.deleteById(jobId);
        }else {
            throw new EmployerRoleException("Role is not valid, it should be employer");
        }
    }
    @Override
    public ResponseEntity<FilterJobDTO> getFilteredStory(FilterPayloadDTO filterPayload, int pageIndex) {
        FilterJobDTO storyListDTO;
        List<String> title = filterPayload.getTitle();
        List<String> location = filterPayload.getLocation();
        List<String> category = filterPayload.getCategory();
        String sortType = filterPayload.getSortType();
        List<Jobs> filteredResults = new ArrayList<>(jobRepository.findAll());
        filteredResults = filteredResults.stream()
                .filter(job -> {
                    boolean titleCheck = true;
                    boolean categoryCheck = true;
                    boolean locationCheck = true;
                    if (title != null && !title.isEmpty()) {
                        titleCheck = title.stream()
                                .anyMatch(titleVal -> job.getTitle().equals(titleVal));
                    }
                    if (location != null && !location.isEmpty()) {
                        locationCheck = location.stream()
                                .anyMatch(locationVal -> job.getLocation().equals(locationVal));
                    }
                    if (category != null && !category.isEmpty()) {
                        categoryCheck = category.stream()
                                .anyMatch(categoryVal -> job.getCategory().equals(categoryVal));
                    }
                    return titleCheck && locationCheck && categoryCheck;
                })
                .collect(Collectors.toList());
        if (sortType != null && sortType.equals("RECENTLY")) {
            filteredResults.sort(Comparator.comparing(Jobs::getCreatedOn).reversed());
        }
        int pageSize = 2;
        int startIndex = pageIndex * pageSize;
        int endIndex = Math.min(startIndex + pageSize, filteredResults.size());
        List<Jobs> paginatedStoryDTOList = filteredResults.subList(startIndex, endIndex);
        boolean pagination = endIndex < filteredResults.size();
        pageIndex = pagination ? pageIndex + 1 : 0;
        storyListDTO = FilterJobDTO.builder()
                .searchResult(paginatedStoryDTOList)
                .pagination(pagination)
                .pageIndex(pageIndex)
                .build();
        if(paginatedStoryDTOList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(storyListDTO);
        }
        return ResponseEntity.status(HttpStatus.OK).body(storyListDTO);
    }

}
