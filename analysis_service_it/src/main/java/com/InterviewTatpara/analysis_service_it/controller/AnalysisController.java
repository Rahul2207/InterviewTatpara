package com.InterviewTatpara.analysis_service_it.controller;

import com.InterviewTatpara.analysis_service_it.service.AiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/analysis")
// Allow Angular (localhost:4200) to access this directly
@CrossOrigin(origins = "http://localhost:4200") 
public class AnalysisController {

    @Autowired
    private AiService aiService;

    // Frontend calls this: POST /api/analysis/generate-questions
    // Body: { "category": "Java" }
    @PostMapping("/generate-questions")
    public ResponseEntity<Map<String, List<String>>> getQuestions(@RequestBody Map<String, String> request) {
        
        String category = request.getOrDefault("category", "General");
        
        // Call the new method in AiService
        List<String> questions = aiService.generateQuestions(category);
        
        // Return JSON: { "questions": ["Q1", "Q2", ...] }
        return ResponseEntity.ok(Collections.singletonMap("questions", questions));
    }
}