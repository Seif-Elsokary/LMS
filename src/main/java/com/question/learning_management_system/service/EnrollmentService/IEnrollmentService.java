package com.question.learning_management_system.service.EnrollmentService;

import com.question.learning_management_system.dto.EnrollmentDto;

import java.util.List;
import java.util.Optional;

public interface IEnrollmentService {
    EnrollmentDto enrollStudentInCourse(Long studentId, Long courseId);
    void cancelEnrollment(Long enrollmentId);
    List<EnrollmentDto> getEnrollmentsByStudent(Long studentId);
    List<EnrollmentDto> getEnrollmentsByCourse(Long courseId);
    Optional<EnrollmentDto> getEnrollment(Long studentId, Long courseId);
}
