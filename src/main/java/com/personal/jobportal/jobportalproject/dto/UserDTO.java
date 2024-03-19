package com.personal.jobportal.jobportalproject.dto;
import com.personal.jobportal.jobportalproject.enums.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Integer userId;
    private String userName;
    private String password;
    private String email;
    private RoleEnum role;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
}
