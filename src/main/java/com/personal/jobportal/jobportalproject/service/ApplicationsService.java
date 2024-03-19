package com.personal.jobportal.jobportalproject.service;
import com.personal.jobportal.jobportalproject.dto.ApplicationDTO;
import com.personal.jobportal.jobportalproject.dto.ApplicationListDTO;
import com.personal.jobportal.jobportalproject.dto.UpdateApplicationResponseDTO;
import com.personal.jobportal.jobportalproject.exception.EmployerRoleException;
import org.springframework.http.ResponseEntity;

import java.util.List;
public interface ApplicationsService {
    ApplicationDTO applyForJob(ApplicationDTO applicationDTO, String token) throws EmployerRoleException;
    List<ApplicationListDTO> getApplicationList(String token) throws EmployerRoleException;
    ApplicationDTO getApplicationById(int applicationId) throws EmployerRoleException;
    ResponseEntity<?> updateStatus(String token, int applicationId, UpdateApplicationResponseDTO updateResponse) throws EmployerRoleException;
    void withdrawApplication(int applicationId);
}
