package com.question.learning_management_system.controller;

import com.question.learning_management_system.dto.CourseDto;
import com.question.learning_management_system.request.CourseRequest.CreateCourseRequest;
import com.question.learning_management_system.request.CourseRequest.UpdateCourseRequest;
import com.question.learning_management_system.service.CourseService.ICourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final ICourseService courseService;

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping
    public ResponseEntity<List<CourseDto>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/{id}")
    public ResponseEntity<CourseDto> getCourseById(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.getCourseById(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/title")
    public ResponseEntity<CourseDto> getCourseByTitle(@RequestParam String title) {
        return ResponseEntity.ok(courseService.getCourseByTitle(title));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<CourseDto>> getCoursesByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(courseService.getCoursesByCategory(categoryId));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/instructor/{instructorId}")
    public ResponseEntity<List<CourseDto>> getCoursesByInstructor(@PathVariable Long instructorId) {
        return ResponseEntity.ok(courseService.getCoursesByInstructor(instructorId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CourseDto> createCourse(@RequestBody CreateCourseRequest request) {
        return ResponseEntity.ok(courseService.createCourse(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<CourseDto> updateCourse(@RequestBody UpdateCourseRequest request, @PathVariable Long id) {
        return ResponseEntity.ok(courseService.updateCourse(request, id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/search")
    public ResponseEntity<List<CourseDto>> searchCourses(@RequestParam String query) {
        return ResponseEntity.ok(courseService.searchCoursesByMergedName(query));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/category-name/{categoryName}")
    public ResponseEntity<List<CourseDto>> getCoursesByCategoryName(@PathVariable String categoryName) {
        return ResponseEntity.ok(courseService.getCoursesByCategoryName(categoryName));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/highest-rated/category-name/{categoryName}")
    public ResponseEntity<CourseDto> getHighestRatedCourseByCategoryName(@PathVariable String categoryName) {
        return ResponseEntity.ok(courseService.getHighestRatedCourseByCategoryName(categoryName));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/search-merged")
    public ResponseEntity<List<CourseDto>> searchCoursesByMerged(@RequestParam String mergedInput) {
        return ResponseEntity.ok(courseService.searchCoursesByMergedName(mergedInput));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/highest-rated/category/{categoryId}")
    public ResponseEntity<CourseDto> getHighestRatedCourseByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(courseService.getHighestRatedCourseByCategory(categoryId));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/highest-rated/category/{categoryName}/instructor/{instructorName}")
    public ResponseEntity<List<CourseDto>> getHighestRatedCourseByCategoryAndInstructor(
            @PathVariable String categoryName, @PathVariable String instructorName) {
        return ResponseEntity.ok(courseService.getHighestRatedCourseByCategoryAndInstructorName(categoryName, instructorName));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/courses-sorted-by-rate")
    public ResponseEntity<List<CourseDto>> getCoursesSortedByRate() {
        return ResponseEntity.ok(courseService.getCoursesSortedByAverageRate());
    }
}
