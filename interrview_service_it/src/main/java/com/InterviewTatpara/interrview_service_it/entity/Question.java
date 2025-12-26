package com.InterviewTatpara.interrview_service_it.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "questions")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String text; // e.g., "What is a HashMap?"

    @Enumerated(EnumType.STRING)
    private Category category; // JAVA, HR, BEHAVIORAL

    // Define your categories here
    public enum Category {
        JAVA, 
        HR, 
        BEHAVIORAL, 
        SQL,
        CUSTOM // Fallback for user input
    }
}