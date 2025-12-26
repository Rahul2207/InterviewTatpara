package com.InterviewTatpara.auth_service_it.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.InterviewTatpara.auth_service_it.dto.LoginRequest;
import com.InterviewTatpara.auth_service_it.entity.UserCredential;
import com.InterviewTatpara.auth_service_it.repository.UserCredentialRepository;

@Service
public class AuthService {

	@Autowired
    private UserCredentialRepository repository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtService jwtService;

    public String saveUser(UserCredential credential) {
        // IMPORTANT: Encrypt password before saving!
        credential.setPassword(passwordEncoder.encode(credential.getPassword()));
        repository.save(credential);
        return "User added successfully";
    }

public Map<String, String> login(LoginRequest request) {
        
        // 1. Check if User exists
        Optional<UserCredential> userOptional = repository.findByEmail(request.getEmail());
        
        if (userOptional.isPresent()) {
            UserCredential user = userOptional.get();

            // 2. Validate Password
            // (Ideally use passwordEncoder.matches() here if using BCrypt)
            if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                
                // 3. Generate Token
                String token = jwtService.generateToken(user.getName());

                // 4. Prepare Response
                Map<String, String> response = new HashMap<>();
                response.put("email", user.getEmail());
                response.put("name", user.getName());
                response.put("token", token);
                
                return response;
            }
        }
        
        // If user not found or password wrong, throw exception
        throw new RuntimeException("Invalid Credentials");
    }

    public String generateToken(String username) {
        return jwtService.generateToken(username);
    }

    public void validateToken(String token) {
        jwtService.validateToken(token);
    }
}
