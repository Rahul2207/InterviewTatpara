package com.InterviewTatpara.auth_service_it.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.InterviewTatpara.auth_service_it.entity.UserCredential;

@Repository
public interface UserCredentialRepository extends JpaRepository<UserCredential, Integer> {

	// Spring Data JPA generates the SQL for this automatically
    Optional<UserCredential> findByName(String username);
    Optional<UserCredential> findByEmail(String username);
}
