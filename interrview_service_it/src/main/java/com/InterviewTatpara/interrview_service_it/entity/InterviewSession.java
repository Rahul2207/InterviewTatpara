package com.InterviewTatpara.interrview_service_it.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@Table(name = "interview_sessions")
public class InterviewSession {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String userEmail;
    
    @Column(length = 2000) // Increased length just in case
    private String questionText;
    
    private String videoUrl;
    private String category;

    // --- NEW FIELDS FOR HISTORY ---

    @Column(length = 5000) // Large text for transcript
    private String userAnswer; 

    @Column(length = 5000) // Large text for AI feedback
    private String aiFeedback;

    private Integer score; // 1-10 (Using Integer allows null if not scored yet)

    // -----------------------------

    @Enumerated(EnumType.STRING)
    private SessionStatus status; 

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) this.status = SessionStatus.UPLOADING;
    }

    public enum SessionStatus { UPLOADING, PROCESSING, COMPLETED, FAILED }
}