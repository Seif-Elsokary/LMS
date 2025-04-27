package com.question.learning_management_system.repository;

import com.question.learning_management_system.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {

    Course findByTitleIgnoreCase(String title);

    List<Course> findByInstructor_NameIgnoreCase(String name);

    List<Course> findByCategoryId(Long categoryId);

    List<Course> findByInstructorId(Long instructorId);

    @Query("SELECT c FROM Course c WHERE LOWER(REPLACE(c.title, ' ', '')) = LOWER(:title)")
    Course findByTitleMergedIgnoreCase(String title);

    @Query("SELECT c FROM Course c WHERE LOWER(REPLACE(c.instructor.name, ' ', '')) = LOWER(:instructorName)")
    List<Course> findByInstructorNameMergedIgnoreCase(String instructorName);
}
