package com.question.learning_management_system.service.CourseService;

import com.question.learning_management_system.dto.CourseDto;
import com.question.learning_management_system.request.CourseRequest.CreateCourseRequest;
import com.question.learning_management_system.request.CourseRequest.UpdateCourseRequest;

import java.util.List;

public interface ICourseService {
    CourseDto updateCourse(UpdateCourseRequest request, long courseId);

    CourseDto getCourseById(Long id);

    CourseDto getCourseByTitle(String title);

    List<CourseDto> getAllCourses();

    CourseDto createCourse(CreateCourseRequest request);

    void deleteCourse(Long id);

    List<CourseDto> getCoursesByInstructorName(String name);

    List<CourseDto> getCoursesByCategory(Long categoryId);

    List<CourseDto> getCoursesByInstructor(Long instructorId);

    List<CourseDto> searchCoursesByMergedName(String mergedInput);

    List<CourseDto> getCoursesSortedByAverageRate();

    CourseDto getHighestRatedCourseByCategory(Long categoryId);

    CourseDto getHighestRatedCourseByCategoryAndInstructorName(Long categoryId, String instructorName);

    List<CourseDto> getHighestRatedCourseByCategoryAndInstructorName(String categoryName, String instructorName);

    List<CourseDto> getCoursesByCategoryName(String categoryName);

    CourseDto getHighestRatedCourseByCategoryName(String categoryName);
}
