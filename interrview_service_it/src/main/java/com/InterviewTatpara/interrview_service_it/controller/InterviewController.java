package com.InterviewTatpara.interrview_service_it.controller;

import com.InterviewTatpara.interrview_service_it.entity.InterviewSession;
import com.InterviewTatpara.interrview_service_it.service.InterviewService; // Import your Service

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/interview")
@CrossOrigin(origins = "*")
public class InterviewController {

    @Autowired
    private InterviewService interviewService; // Use the Service you already created!

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> uploadVideo( // <--- Change return type to Map
            @RequestParam("file") MultipartFile file,
            @RequestParam("email") String email,
            @RequestParam("question") String question,
            @RequestParam("category") String category
    ) {
        System.out.println("--- DEBUG: Request Received ---");

        try {
            // Delegate to Service
            InterviewSession session = interviewService.processVideo(file, email, question, category);

            // --- FIX: Return JSON format ---
            Map<String, String> response = new HashMap<>();
            response.put("id", session.getId().toString()); // Frontend looks for "id"
            response.put("status", "PROCESSING");
            response.put("message", "Video accepted for analysis");

            return ResponseEntity.accepted().body(response);

        } catch (Exception e) {
            System.err.println("!!! CONTROLLER ERROR !!!");
            e.printStackTrace();
            
            // Return JSON error too, so frontend can display it cleanly
            return ResponseEntity.internalServerError()
                    .body(Collections.singletonMap("error", "Upload Failed: " + e.getMessage()));
        }
    }
    
    @GetMapping("/history/{email}")
    public ResponseEntity<List<InterviewSession>> getHistory(@PathVariable String email) {
        return ResponseEntity.ok(interviewService.getUserHistory(email));
    }
    @GetMapping("/{sessionId}")
    public ResponseEntity<InterviewSession> getSession(@PathVariable UUID sessionId) {
        return ResponseEntity.ok(interviewService.findById(sessionId));
    }
}