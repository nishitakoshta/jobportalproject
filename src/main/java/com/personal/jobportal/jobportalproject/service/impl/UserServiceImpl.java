package com.personal.jobportal.jobportalproject.service.impl;
import com.personal.jobportal.jobportalproject.config.JwtService;
import com.personal.jobportal.jobportalproject.dto.AuthRequestDTO;
import com.personal.jobportal.jobportalproject.dto.JwtResponseDTO;
import com.personal.jobportal.jobportalproject.dto.UserDTO;
import com.personal.jobportal.jobportalproject.entity.Users;
import com.personal.jobportal.jobportalproject.enums.RoleEnum;
import com.personal.jobportal.jobportalproject.repository.UserRepository;
import com.personal.jobportal.jobportalproject.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository usersRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    @Override
    public UserDTO addUser(UserDTO userDTO) {
        try {
            String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
            var user = Users.builder()
                    .username(userDTO.getUserName())
                    .password(encodedPassword)
                    .email(userDTO.getEmail())
                    .role(userDTO.getRole().ordinal())
                    .build();
            usersRepository.save(user);
            return UserDTO.builder()
                    .userId(user.getUserId())
                    .userName(user.getUsername())
                    .password(user.getPassword())
                    .email(user.getEmail())
                    .role(RoleEnum.values()[user.getRole()])
                    .createdOn(LocalDateTime.now())
                    .updatedOn(LocalDateTime.now())
                    .build();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public JwtResponseDTO userLogin(AuthRequestDTO authRequestDTO) {
        String username = authRequestDTO.getEmail();
        String password = authRequestDTO.getPassword();
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));
        var user = usersRepository.findByEmail(username).orElseThrow();
        // Retrieve UserDetails after successful authentication
        // Generate JWT token using JwtService
        String token = jwtService.generateToken(new CustomUserDetails(user));
        // Create JwtResponseDTO with the generated token
        return JwtResponseDTO.builder()
                .accessToken(token)
                .build();
    }
}
