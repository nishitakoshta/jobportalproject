package com.personal.jobportal.jobportalproject.service;
import com.personal.jobportal.jobportalproject.dto.AuthRequestDTO;
import com.personal.jobportal.jobportalproject.dto.JwtResponseDTO;
import com.personal.jobportal.jobportalproject.dto.UserDTO;
public interface UserService {
    UserDTO addUser(UserDTO userDTO);
    JwtResponseDTO userLogin(AuthRequestDTO authRequestDTO);
}
