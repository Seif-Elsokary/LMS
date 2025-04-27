package com.question.learning_management_system.repository;

import com.question.learning_management_system.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByCourseId(Long courseId);

    List<Review> findByStudentId(Long studentId);

    @Query("SELECT r FROM Review r WHERE LOWER(r.course.category.name) = LOWER(:categoryName)")
    List<Review> findByCourseCategoryName(String categoryName);

    @Query("SELECT r FROM Review r WHERE LOWER(r.course.instructor.name) = LOWER(:instructorName) AND LOWER(r.course.category.name) = LOWER(:categoryName)")
    List<Review> findByCourseInstructorNameAndCourseCategoryName(String instructorName, String categoryName);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.course.id = :courseId")
    Double findAverageRatingByCourseId(Long courseId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE LOWER(r.course.instructor.name) = LOWER(:instructorName)")
    Double findAverageRatingByInstructorName(String instructorName);

    // البحث عن التقييمات بناءً على studentId و courseId
    @Query("SELECT r FROM Review r WHERE r.student.id = :studentId AND r.course.id = :courseId")
    List<Review> findByStudentIdAndCourseId(Long studentId, Long courseId);

    @Query("SELECT r FROM Review r WHERE LOWER(r.course.instructor.name) = LOWER(:instructor) AND LOWER(r.course.category.name) = LOWER(:category)")
    List<Review> findByCourseInstructorNameIgnoreCaseAndCourseCategoryNameIgnoreCase(String instructor, String category);
}
