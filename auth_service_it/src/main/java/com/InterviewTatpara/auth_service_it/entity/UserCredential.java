package com.InterviewTatpara.auth_service_it.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_credentials")
public class UserCredential {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false)
    private String name; // This functions as the username

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;
}
