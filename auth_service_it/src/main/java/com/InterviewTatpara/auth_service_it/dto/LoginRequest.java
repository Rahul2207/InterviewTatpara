package com.InterviewTatpara.auth_service_it.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
