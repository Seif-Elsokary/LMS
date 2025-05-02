package com.question.learning_management_system.serviceTest.EnrollmentTest;

import com.question.learning_management_system.dto.EnrollmentDto;
import com.question.learning_management_system.entity.Course;
import com.question.learning_management_system.entity.Enrollment;
import com.question.learning_management_system.entity.Instructor;
import com.question.learning_management_system.entity.Student;
import com.question.learning_management_system.exception.AlreadyExistsException;
import com.question.learning_management_system.repository.CourseRepository;
import com.question.learning_management_system.repository.EnrollmentRepository;
import com.question.learning_management_system.repository.StudentRepository;
import com.question.learning_management_system.service.EmailService.IMailService;
import com.question.learning_management_system.service.EnrollmentService.EnrollmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EnrollmentServiceTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private IMailService mailService;

    @InjectMocks
    private EnrollmentService enrollmentService;

    private Student student;
    private Course course;
    private Instructor instructor;
    private Enrollment enrollment;

    @BeforeEach
    void setUp() {
        instructor = Instructor.builder()
                .id(1L)
                .name("Instructor 1")
                .email("inst@example.com")
                .build();

        student = Student.builder()
                .id(1L)
                .name("John").
                email("john@example.com")
                .build();

        course = Course.builder()
                .id(1L)
                .title("Java")
                .instructor(instructor)
                .build();

        enrollment = Enrollment.builder()
                .id(1L)
                .student(student)
                .course(course)
                .enrollmentDate(LocalDateTime.now())
                .build();
    }

    @Test
    void testEnrollStudentInCourse_Success() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(enrollmentRepository.findByStudentAndCourse(student, course)).thenReturn(Optional.empty());
        when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(enrollment);

        EnrollmentDto enrollmentDto = EnrollmentDto.builder()
                .courseId(course.getId())
                .courseName(course.getTitle())
                .studentId(student.getId())
                .studentName(student.getName())
                .build();

        when(modelMapper.map(any(Enrollment.class), eq(EnrollmentDto.class))).thenReturn(enrollmentDto);

        EnrollmentDto result = enrollmentService.enrollStudentInCourse(1L, 1L);

        assertNotNull(result);
        assertEquals("Java", result.getCourseName());
        verify(mailService, times(2)).sendMail(any());
    }

    @Test
    void testEnrollStudentInCourse_AlreadyEnrolled() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(enrollmentRepository.findByStudentAndCourse(student, course)).thenReturn(Optional.of(enrollment));

        assertThrows(AlreadyExistsException.class, () -> {
            enrollmentService.enrollStudentInCourse(1L, 1L);
        });

        verify(enrollmentRepository, never()).save(any());
    }

    @Test
    void testEnrollStudentInCourse_StudentNotFound() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            enrollmentService.enrollStudentInCourse(1L, 1L);
        });

        assertEquals("Student not found", exception.getMessage());
    }

    @Test
    void testEnrollStudentInCourse_CourseNotFound() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            enrollmentService.enrollStudentInCourse(1L, 1L);
        });

        assertEquals("Course not found", exception.getMessage());
    }
}
