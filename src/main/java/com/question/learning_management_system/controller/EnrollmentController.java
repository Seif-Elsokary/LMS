package com.question.learning_management_system.controller;

import com.question.learning_management_system.dto.EnrollmentDto;
import com.question.learning_management_system.service.EnrollmentService.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping("/enroll")
    public ResponseEntity<EnrollmentDto> enrollStudentInCourse(@RequestParam Long studentId, @RequestParam Long courseId) {
        try {
            EnrollmentDto enrollmentDto = enrollmentService.enrollStudentInCourse(studentId, courseId);
            return ResponseEntity.ok(enrollmentDto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @DeleteMapping("/{enrollmentId}")
    public ResponseEntity<Void> cancelEnrollment(@PathVariable Long enrollmentId) {
        try {
            enrollmentService.cancelEnrollment(enrollmentId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<EnrollmentDto>> getEnrollmentsByStudent(@PathVariable Long studentId) {
        List<EnrollmentDto> enrollments = enrollmentService.getEnrollmentsByStudent(studentId);
        return enrollments.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(enrollments);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<EnrollmentDto>> getEnrollmentsByCourse(@PathVariable Long courseId) {
        List<EnrollmentDto> enrollments = enrollmentService.getEnrollmentsByCourse(courseId);
        return enrollments.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(enrollments);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/student/{studentId}/course/{courseId}")
    public ResponseEntity<EnrollmentDto> getEnrollment(@PathVariable Long studentId, @PathVariable Long courseId) {
        Optional<EnrollmentDto> enrollmentDto = enrollmentService.getEnrollment(studentId, courseId);
        return enrollmentDto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
