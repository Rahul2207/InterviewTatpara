package com.InterviewTatpara.auth_service_it.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.InterviewTatpara.auth_service_it.config.CustomUserDetails;
import com.InterviewTatpara.auth_service_it.entity.UserCredential;
import com.InterviewTatpara.auth_service_it.repository.UserCredentialRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService{

	@Autowired
    private UserCredentialRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserCredential> credential = repository.findByName(username);
        return credential.map(CustomUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
}
