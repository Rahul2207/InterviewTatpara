package com.InterviewTatpara.interrview_service_it.controller;

import com.InterviewTatpara.interrview_service_it.entity.Question;
import com.InterviewTatpara.interrview_service_it.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
@CrossOrigin(origins = "*") // Allow Angular
public class QuestionController {

    @Autowired
    private QuestionRepository repo;

    // GET /api/questions/JAVA
    @GetMapping("/{category}")
    public List<Question> getQuestions(@PathVariable String category) {
        try {
            // Convert String "java" to Enum JAVA
            Question.Category cat = Question.Category.valueOf(category.toUpperCase());
            return repo.findByCategory(cat);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid Category: " + category);
        }
    }
}