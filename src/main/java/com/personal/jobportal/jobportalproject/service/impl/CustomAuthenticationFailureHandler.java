package com.personal.jobportal.jobportalproject.service.impl;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        String errorMessage;
        if (exception instanceof BadCredentialsException) {
            errorMessage = "Invalid username or password";
        } else {
            errorMessage = "Authentication failed";
        }
        String jsonErrorResponse = String.format("{\"Error\": \"%s\"}", errorMessage);
        response.getWriter().write(jsonErrorResponse);
    }
}
