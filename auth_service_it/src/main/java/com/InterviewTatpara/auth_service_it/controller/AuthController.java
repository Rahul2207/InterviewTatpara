package com.InterviewTatpara.auth_service_it.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Collections; // <--- Must be this one

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.InterviewTatpara.auth_service_it.dto.AuthRequest;
import com.InterviewTatpara.auth_service_it.dto.LoginRequest;
import com.InterviewTatpara.auth_service_it.entity.UserCredential;
import com.InterviewTatpara.auth_service_it.service.AuthService;



import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")

public class AuthController {
	
	@Autowired
    private AuthService service;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<?> addNewUser(@RequestBody UserCredential user) {
    	
    	service.saveUser(user);
    	Map<String, String> response = new HashMap<>();
        
        
    	return ResponseEntity.ok(Collections.singletonMap("message", "User registered successfully"));
        
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            // The Controller just asks the Service for the answer
            return ResponseEntity.ok(service.login(request));
        } catch (RuntimeException e) {
            // If Service throws "Invalid Credentials", we return 401
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    @PostMapping("/token")
    public String getToken(@RequestBody AuthRequest authRequest) {
        // Authenticate using Spring Security
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );
        
        // If password is correct, generate JWT
        if (authenticate.isAuthenticated()) {
            return service.generateToken(authRequest.getUsername());
        } else {
            throw new RuntimeException("Invalid Access: Wrong username or password");
        }
    }

    @GetMapping("/validate")
    public String validateToken(@RequestParam("token") String token) {
        service.validateToken(token);
        return "Token is valid";
    }

}
