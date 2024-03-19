package com.personal.jobportal.jobportalproject.service.impl;
import com.personal.jobportal.jobportalproject.dto.AuthRequestDTO;
import com.personal.jobportal.jobportalproject.dto.JwtResponseDTO;
import com.personal.jobportal.jobportalproject.dto.UserDTO;
import com.personal.jobportal.jobportalproject.entity.Users;
import com.personal.jobportal.jobportalproject.enums.RoleEnum;
import com.personal.jobportal.jobportalproject.repository.UserRepository;
import com.personal.jobportal.jobportalproject.service.JwtService;
import com.personal.jobportal.jobportalproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository usersRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;
    @Override
    public UserDTO addUser(UserDTO userDTO) {
        try {
            Users user = new Users();
            user.setUsername(userDTO.getUserName());
            String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
            user.setPassword(encodedPassword);
            user.setEmail(userDTO.getEmail());
            user.setRole(userDTO.getRole().ordinal());
            user.setCreatedOn(LocalDateTime.now());
            user.setUpdatedOn(LocalDateTime.now());
            usersRepository.save(user);
            return UserDTO.builder()
                    .userId(user.getUserId())
                    .userName(user.getUsername())
                    .password(user.getPassword())
                    .email(user.getEmail())
                    .role(RoleEnum.values()[user.getRole()])
                    .createdOn(user.getCreatedOn())
                    .updatedOn(user.getUpdatedOn())
                    .build();
        } catch (Exception ex) {
            throw ex;
        }
    }
    @Override
    public JwtResponseDTO userLogin(AuthRequestDTO authRequestDTO) {
        String username = authRequestDTO.getUsername();
        String password = authRequestDTO.getPassword();
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Username or password is empty");
        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));
        if (authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtService.generateToken(userDetails);
            return JwtResponseDTO.builder()
                    .accessToken(token)
                    .build();
        } else {
            throw new UsernameNotFoundException("Invalid credentials");
        }
    }
}
