package com.InterviewTatpara.interrview_service_it.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.InterviewTatpara.interrview_service_it.dto.VideoMessage;
import com.InterviewTatpara.interrview_service_it.config.RabbitMQConfig;
import com.InterviewTatpara.interrview_service_it.entity.InterviewSession;
import com.InterviewTatpara.interrview_service_it.entity.InterviewSession.SessionStatus;
import com.InterviewTatpara.interrview_service_it.entity.Question.Category;
import com.InterviewTatpara.interrview_service_it.repository.InterviewSessionRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class InterviewService {

    @Autowired
    private InterviewSessionRepository repository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    // This absolute path is correct for your Mac setup
    private final String UPLOAD_DIR = "/Users/rahulkumar/Downloads/New Projects/Interview Tatpara/uploads/";

    public InterviewSession processVideo(MultipartFile file, String email, String question,String category) throws IOException {
        
        // 1. Create DB Record
        InterviewSession session = new InterviewSession();
        session.setUserEmail(email);
        session.setQuestionText(question); // Save question to DB
        session = repository.save(session); 

        // 2. Save File
        File directory = new File(UPLOAD_DIR);
        if (!directory.exists()) directory.mkdirs();
        
        String fileName = session.getId() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(UPLOAD_DIR + fileName);
        Files.write(filePath, file.getBytes());

        // 3. Update DB
        session.setVideoUrl(filePath.toString());
        session.setStatus(InterviewSession.SessionStatus.PROCESSING);
        repository.save(session);

        // 4. Send Message to Analysis Service
        // FIX: We must pass the 'question' here so the AI knows what was asked!
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.EXCHANGE, 
            RabbitMQConfig.ROUTING_KEY, 
            new VideoMessage(
                session.getId().toString(), 
                filePath.toString(), 
                email, 
                question,
                category// <--- ADDED THIS BACK
            )
        );

        return session;
    }
    public void saveAnalysis(UUID sessionId, String transcript, String feedback, int score) {
        InterviewSession session = repository.findById(sessionId)
            .orElseThrow(() -> new RuntimeException("Session not found"));

        //session.setUserAnswer(transcript);
        //session.setAiFeedback(feedback);
        //session.setScore(score);
        //session.setStatus(InterviewSession.SessionStatus.COMPLETED); // Mark as done

        repository.save(session);
    }
    
    public List<InterviewSession> getUserHistory(String email) {
        // We only want finished interviews that have feedback/scores
        return repository.findByUserEmailAndStatusOrderByCreatedAtDesc(
            email, 
            SessionStatus.COMPLETED
        );
    }
    
    public InterviewSession findById(UUID sessionId) {
    	return repository.getById(sessionId);
    	
    }
}