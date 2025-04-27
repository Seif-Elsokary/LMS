package com.question.learning_management_system.controller;

import com.question.learning_management_system.dto.CourseDto;
import com.question.learning_management_system.request.CourseRequest.CreateCourseRequest;
import com.question.learning_management_system.request.CourseRequest.UpdateCourseRequest;
import com.question.learning_management_system.service.CourseService.ICourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final ICourseService courseService;

    @GetMapping
    public ResponseEntity<List<CourseDto>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDto> getCourseById(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.getCourseById(id));
    }

    @GetMapping("/title")
    public ResponseEntity<CourseDto> getCourseByTitle(@RequestParam String title) {
        return ResponseEntity.ok(courseService.getCourseByTitle(title));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<CourseDto>> getCoursesByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(courseService.getCoursesByCategory(categoryId));
    }

    @GetMapping("/instructor/{instructorId}")
    public ResponseEntity<List<CourseDto>> getCoursesByInstructor(@PathVariable Long instructorId) {
        return ResponseEntity.ok(courseService.getCoursesByInstructor(instructorId));
    }

    @PostMapping
    public ResponseEntity<CourseDto> createCourse(@RequestBody CreateCourseRequest request) {
        return ResponseEntity.ok(courseService.createCourse(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseDto> updateCourse(@RequestBody UpdateCourseRequest request, @PathVariable Long id) {
        return ResponseEntity.ok(courseService.updateCourse(request, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<CourseDto>> searchCourses(@RequestParam String query) {
        return ResponseEntity.ok(courseService.searchCoursesByMergedName(query));
    }
}
