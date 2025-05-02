package com.question.learning_management_system.repository;

import com.question.learning_management_system.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {

    Course findByTitleIgnoreCase(String title);

    List<Course> findByInstructor_NameIgnoreCase(String name);

    List<Course> findByCategoryId(Long categoryId);

    List<Course> findByInstructorId(Long instructorId);

    @Query("SELECT c FROM Course c WHERE LOWER(REPLACE(c.title, ' ', '')) = LOWER(:title)")
    Course findByTitleMergedIgnoreCase(String title);

    @Query("SELECT c FROM Course c WHERE LOWER(REPLACE(c.instructor.name, ' ', '')) = LOWER(:instructorName)")
    List<Course> findByInstructorNameMergedIgnoreCase(String instructorName);

    @Query("SELECT c FROM Course c ORDER BY c.averageRate DESC")
    List<Course> findAllCoursesSortedByAverageRate();

    @Query("SELECT c FROM Course c WHERE c.category.id = :categoryId ORDER BY c.averageRate DESC LIMIT 1")
    Optional<Course> findTopCourseByCategoryOrderByAverageRateDesc(Long categoryId);

    @Query("SELECT c FROM Course c WHERE c.category.id = :categoryId " +
            "AND LOWER(c.instructor.name) = LOWER(:instructorName) ORDER BY c.averageRate DESC LIMIT 1")
    Course findTopCourseByCategoryAndInstructorNameIgnoreCase(Long categoryId, String instructorName);

    @Query("SELECT c FROM Course c WHERE c.category.name = :categoryName " +
            "AND LOWER(c.instructor.name) = LOWER(:instructorName) ORDER BY c.averageRate DESC")
    List<Course> findTopCourseByCategoryNameAndInstructorNameIgnoreCase(String categoryName, String instructorName);

    @Query("SELECT c FROM Course c WHERE LOWER(c.category.name) = LOWER(:categoryName)")
    List<Course> findByCategoryNameIgnoreCase(String categoryName);

    @Query("SELECT c FROM Course c WHERE c.category.id = :categoryId ORDER BY c.averageRate DESC limit 1")
    Course findCourseWithTopRatedByCategoryId(Long categoryId);

    @Query("SELECT  c from Course c WHERE c.category.name=:categoryName " +
            "AND LOWER(c.category.name) = LOWER(:categoryName) order by c.averageRate desc limit 1")
    Course findCourseWithTopRatedByCategoryNameIgnoreCase(String categoryName);

}
