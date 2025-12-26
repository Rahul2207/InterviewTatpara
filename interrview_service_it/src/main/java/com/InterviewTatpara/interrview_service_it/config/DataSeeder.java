package com.InterviewTatpara.interrview_service_it.config;

import com.InterviewTatpara.interrview_service_it.entity.Question;
import com.InterviewTatpara.interrview_service_it.repository.QuestionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final QuestionRepository repo;

    public DataSeeder(QuestionRepository repo) {
        this.repo = repo;
    }

    @Override
    public void run(String... args) {
        // Only load if DB is empty
        if (repo.count() == 0) {
            
            // JAVA Questions
            save("Explain the difference between JDK, JRE, and JVM.", Question.Category.JAVA);
            save("What are the main features of Java 8?", Question.Category.JAVA);
            save("Explain the difference between Abstract Class and Interface.", Question.Category.JAVA);
            save("How does a HashMap work internally?", Question.Category.JAVA);

            // HR Questions
            save("Tell me about yourself.", Question.Category.HR);
            save("Why do you want to join our company?", Question.Category.HR);
            save("Where do you see yourself in 5 years?", Question.Category.HR);

            // SQL Questions
            save("What is the difference between INNER JOIN and LEFT JOIN?", Question.Category.SQL);

            System.out.println("--- 🚀 Question Bank Loaded Successfully ---");
        }
    }

    private void save(String text, Question.Category cat) {
        repo.save(new Question(null, text, cat));
    }
}