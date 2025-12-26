package com.InterviewTatpara.analysis_service_it.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.UUID;

@Entity
@Data
@Table(name = "interview_sessions")
public class InterviewSession {

    @Id
    private UUID id; // We only read by ID

    private String userEmail;
    private String category;
    
    @Column(length = 5000) // Large text for transcript
    private String userAnswer; 


    private Integer score; 
    
    @Enumerated(EnumType.STRING)
    private SessionStatus status;

    @Column(columnDefinition = "TEXT")
    private String aiFeedback;
    
    

    public enum SessionStatus { UPLOADING, PROCESSING, COMPLETED, FAILED }
}
