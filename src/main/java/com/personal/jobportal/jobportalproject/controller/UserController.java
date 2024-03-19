package com.personal.jobportal.jobportalproject.controller;
import com.personal.jobportal.jobportalproject.dto.AuthRequestDTO;
import com.personal.jobportal.jobportalproject.dto.JwtResponseDTO;
import com.personal.jobportal.jobportalproject.dto.UserDTO;
import com.personal.jobportal.jobportalproject.service.impl.JwtServiceImpl;
import com.personal.jobportal.jobportalproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("api/v1/users")
public class UserController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtServiceImpl jwtServiceImpl;
    @Autowired
    private UserService userService;
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO) {
        try {
            UserDTO responseDto = userService.addUser(userDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
        } catch (Exception e) {
            throw e;
        }
    }
    @PostMapping("/login")
    public JwtResponseDTO authenticateAndGetToken(@RequestBody AuthRequestDTO authRequestDTO) {
        try {
            return userService.userLogin(authRequestDTO);
        } catch (Exception e) {
            throw e;
        }
    }
}
