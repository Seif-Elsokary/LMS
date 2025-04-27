package com.question.learning_management_system.repository;

import com.question.learning_management_system.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    List<Quiz> findByCourseId(Long courseId);

    List<Quiz> findByCourseIdInAndDifficultyIgnoreCase(List<Long> courseIds, String difficulty);

    List<Quiz> findByCourseIdInAndCourseCategoryNameIgnoreCase(List<Long> courseIds, String categoryName);

    @Query("SELECT q.course.id FROM Quiz q WHERE q.course.id IN (SELECT c.id FROM Course c JOIN c.students s WHERE s.id = :studentId)")
    List<Long> findCourseIdsByStudentId(Long studentId);
}

