package com.personal.jobportal.jobportalproject.dto;
import lombok.*;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthRequestDTO {
    private String email;
    private String password;
}
