package com.InterviewTatpara.interrview_service_it.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.InterviewTatpara.interrview_service_it.entity.InterviewSession;

@Repository
public interface InterviewSessionRepository extends JpaRepository<InterviewSession, UUID> {
	
	List<InterviewSession> findByUserEmailAndStatusOrderByCreatedAtDesc(
	        String userEmail, 
	        InterviewSession.SessionStatus status
	    );
} 
