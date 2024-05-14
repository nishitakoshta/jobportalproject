package com.personal.jobportal.jobportalproject.service.impl;
import com.personal.jobportal.jobportalproject.config.JwtService;
import com.personal.jobportal.jobportalproject.dto.ApplicationDTO;
import com.personal.jobportal.jobportalproject.dto.ApplicationListDTO;
import com.personal.jobportal.jobportalproject.dto.UpdateApplicationResponseDTO;
import com.personal.jobportal.jobportalproject.entity.Applications;
import com.personal.jobportal.jobportalproject.entity.Jobs;
import com.personal.jobportal.jobportalproject.entity.Users;
import com.personal.jobportal.jobportalproject.enums.ApplicationStatusEnum;
import com.personal.jobportal.jobportalproject.exception.EmployerRoleException;
import com.personal.jobportal.jobportalproject.repository.ApplicationsRepository;
import com.personal.jobportal.jobportalproject.repository.JobRepository;
import com.personal.jobportal.jobportalproject.repository.UserRepository;
import com.personal.jobportal.jobportalproject.service.ApplicationsService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
@AllArgsConstructor
@Slf4j
public class ApplicationsServiceImpl implements ApplicationsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private ApplicationsRepository applicationsRepository;
    @Autowired
    private JwtService jwtService;
    @Override
    public ApplicationDTO applyForJob(ApplicationDTO applicationDTO, String token) throws EmployerRoleException {
        Applications applications = new Applications();
        Users jobSeeker = userRepository.findById(1)
                .orElseThrow(()-> new EntityNotFoundException("Job seeker not found with id "+1));
        if (jobSeeker.getRole() == 0) {
            applications.setJobSeeker(jobSeeker);
        } else {
            throw new EmployerRoleException("Role is not valid, it should be employer");
        }
        Jobs job = jobRepository.findById(applicationDTO.getJobId())
                .orElseThrow(() -> new EntityNotFoundException("Job not found with id "+applicationDTO.getJobId()));
        applications.setJob(job);
        applications.setCoverLetter(applicationDTO.getCoverLetter());
        applications.setAppliedOn(LocalDateTime.now());
        applicationsRepository.save(applications);
        return ApplicationDTO.builder()
                .applicationId(applications.getApplicationId())
                .jobId(applications.getJob().getJobId())
                .jobSeekerId(applications.getJobSeeker().getUserId())
                .jobSeekerName(applications.getJobSeeker().getUsername())
                .coverLetter(applications.getCoverLetter())
                .status(ApplicationStatusEnum.values()[applications.getStatus()])
                .appliedOn(applications.getAppliedOn())
                .build();
    }
    @Override
    public List<ApplicationListDTO> getApplicationList(String token) throws EmployerRoleException {
        List<ApplicationListDTO> applicationDTOList = new ArrayList<>();
        Integer userRole = jwtService.extractUserRole(token);
        if(userRole == 0) {
            List<Applications> applications = applicationsRepository.findAll();
            for (Applications application : applications) {
                applicationDTOList.add(ApplicationListDTO.builder()
                        .applicationId(application.getApplicationId())
                        .jobId(application.getJob().getJobId())
                        .jobSeekerId(application.getJobSeeker().getUserId())
                        .jobSeekerName(application.getJobSeeker().getUsername())
                        .status(ApplicationStatusEnum.values()[application.getStatus()])
                        .appliedOn(application.getAppliedOn())
                        .build()
                );
            }
            return applicationDTOList;
        }else {
            throw new EmployerRoleException("Role is not valid, it should be employer");
        }
    }
    @Override
    public ApplicationDTO getApplicationById(int applicationId) throws EmployerRoleException {
        Applications application = applicationsRepository.findById(applicationId)
                .orElseThrow(EntityNotFoundException::new);
            return ApplicationDTO.builder()
                    .applicationId(application.getApplicationId())
                    .jobId(application.getJob().getJobId())
                    .jobSeekerId(application.getJobSeeker().getUserId())
                    .jobSeekerName(application.getJobSeeker().getUsername())
                    .coverLetter(application.getCoverLetter())
                    .status(ApplicationStatusEnum.values()[application.getStatus()])
                    .appliedOn(application.getAppliedOn())
                    .build();
    }
    @Override
    public ResponseEntity<?> updateStatus(String token, int applicationId, UpdateApplicationResponseDTO updateResponse) throws EmployerRoleException {
        Map<String, Object> response = new HashMap<>();
        Applications application = applicationsRepository.findById(applicationId)
                .orElseThrow(EntityNotFoundException::new);
        Integer userRole = jwtService.extractUserRole(token);
        LocalDate appliedOnDate = LocalDate.from(application.getAppliedOn());
        LocalDate currentDate = LocalDate.now();
        LocalDate deadlineDate = appliedOnDate.plusDays(15);
        if (currentDate.isEqual(deadlineDate) || currentDate.isAfter(deadlineDate)) {
            application.setStatus(ApplicationStatusEnum.RESPONSE_UNLIKELY.ordinal());
        }
        deadlineDate = appliedOnDate.plusDays(10);
        if (currentDate.isEqual(deadlineDate) || currentDate.isAfter(deadlineDate)) {
            application.setStatus(ApplicationStatusEnum.NOT_SELECTED.ordinal());
        }
        if(userRole == 0){
            application.setStatus(updateResponse.getStatus().ordinal());
            applicationsRepository.save(application);
            response.put("message", "Status updated successfully!!!");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }else {
            throw new EmployerRoleException("Role is not valid, it should be employer");
        }
    }
    @Override
    public void withdrawApplication(int applicationId){
        applicationsRepository.findById(applicationId)
                .orElseThrow(EntityNotFoundException::new);
        applicationsRepository.deleteById(applicationId);
    }
}
