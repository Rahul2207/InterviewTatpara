package com.InterviewTatpara.interrview_service_it.repository;

import com.InterviewTatpara.interrview_service_it.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    // Custom finder to get questions by category
    List<Question> findByCategory(Question.Category category);
}