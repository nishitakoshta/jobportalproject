package com.personal.jobportal.jobportalproject.controller;
import com.personal.jobportal.jobportalproject.dto.ApplicationDTO;
import com.personal.jobportal.jobportalproject.dto.ApplicationListDTO;
import com.personal.jobportal.jobportalproject.dto.UpdateApplicationResponseDTO;
import com.personal.jobportal.jobportalproject.exception.EmployerRoleException;
import com.personal.jobportal.jobportalproject.service.ApplicationsService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("api/v1/applications")
public class ApplicationController {
    @Autowired
    private ApplicationsService applicationsService;
    @Operation(
            tags = "Apply For Job",
            description = "This API is used to apply for job seekers"
    )
    @PostMapping
    public ApplicationDTO applyForJob(@RequestBody ApplicationDTO applicationDTO, @RequestHeader("Authorization") String token){
        try{
            return applicationsService.applyForJob(applicationDTO, token);
        } catch (EmployerRoleException e) {
            throw new RuntimeException(e);
        }
    }
    @GetMapping
    public List<ApplicationListDTO> getApplicationList(@RequestHeader("Authorization") String token){
        try{
            return applicationsService.getApplicationList(token);
        } catch (EmployerRoleException e) {
            throw new RuntimeException(e);
        }
    }
    @GetMapping("/{applicationId}")
    public ApplicationDTO getApplicationId(@RequestParam int applicationId){
        try{
            return applicationsService.getApplicationById(applicationId);
        } catch (EmployerRoleException e) {
            throw new RuntimeException(e);
        }
    }
    @PutMapping("/{applicationId}")
    public ResponseEntity<?> updateApplicationStatus(@RequestHeader("Authorization") String token, @RequestParam int applicationId, @RequestBody UpdateApplicationResponseDTO updateResponse){
        try{
            return applicationsService.updateStatus(token, applicationId, updateResponse);
        } catch (EmployerRoleException e) {
            throw new RuntimeException(e);
        }
    }
    @DeleteMapping("/{applicationId}")
    public ResponseEntity<?> withdrawApplication(@RequestParam int applicationId){
        try{
            Map<String, Object> response = new HashMap<>();
            applicationsService.withdrawApplication(applicationId);
            response.put("message","Withdrawn application successfully!!");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }catch (Exception e){
            throw e;
        }
    }
}
