package com.question.learning_management_system.serviceTest.EnrollmentTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.question.learning_management_system.controller.EnrollmentController;
import com.question.learning_management_system.dto.EnrollmentDto;
import com.question.learning_management_system.service.EnrollmentService.EnrollmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class EnrollmentControllerTest {

    @Mock
    private EnrollmentService enrollmentService;

    @InjectMocks
    private EnrollmentController enrollmentController;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(enrollmentController).build();
    }

    @Test
    public void testEnrollStudentInCourse_Success() throws Exception {
        EnrollmentDto enrollmentDto = EnrollmentDto.builder()
                .id(1L)
                .studentId(1L)
                .courseId(2L)
                .build();

        when(enrollmentService.enrollStudentInCourse(1L, 2L)).thenReturn(enrollmentDto);

        mockMvc.perform(post("/api/enrollments/enroll")
                        .param("studentId", "1")
                        .param("courseId", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studentId").value(1L))
                .andExpect(jsonPath("$.courseId").value(2L));
    }

    @Test
    public void testEnrollStudentInCourse_Failure() throws Exception {
        when(enrollmentService.enrollStudentInCourse(anyLong(), anyLong()))
                .thenThrow(new RuntimeException("Enrollment failed"));

        mockMvc.perform(post("/api/enrollments/enroll")
                        .param("studentId", "1")
                        .param("courseId", "2"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCancelEnrollment_Success() throws Exception {
        doNothing().when(enrollmentService).cancelEnrollment(1L);

        mockMvc.perform(delete("/api/enrollments/1"))
                .andExpect(status().isNoContent());

        verify(enrollmentService, times(1)).cancelEnrollment(1L);
    }

    @Test
    public void testCancelEnrollment_NotFound() throws Exception {
        doThrow(new RuntimeException("Not found")).when(enrollmentService).cancelEnrollment(1L);

        mockMvc.perform(delete("/api/enrollments/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetEnrollmentsByStudent_WithData() throws Exception {
        List<EnrollmentDto> enrollments = Arrays.asList(
                EnrollmentDto.builder().id(1L).studentId(1L).courseId(2L).build(),
                EnrollmentDto.builder().id(2L).studentId(1L).courseId(3L).build()
        );

        when(enrollmentService.getEnrollmentsByStudent(1L)).thenReturn(enrollments);

        mockMvc.perform(get("/api/enrollments/student/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].studentId").value(1L));
    }

    @Test
    public void testGetEnrollmentsByStudent_Empty() throws Exception {
        when(enrollmentService.getEnrollmentsByStudent(1L)).thenReturn(List.of());

        mockMvc.perform(get("/api/enrollments/student/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testGetEnrollmentsByCourse_WithData() throws Exception {
        List<EnrollmentDto> enrollments = Arrays.asList(
                EnrollmentDto.builder().id(1L).studentId(1L).courseId(2L).build(),
                EnrollmentDto.builder().id(2L).studentId(2L).courseId(2L).build()
        );

        when(enrollmentService.getEnrollmentsByCourse(2L)).thenReturn(enrollments);

        mockMvc.perform(get("/api/enrollments/course/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].courseId").value(2L));
    }

    @Test
    public void testGetEnrollmentsByCourse_Empty() throws Exception {
        when(enrollmentService.getEnrollmentsByCourse(2L)).thenReturn(List.of());

        mockMvc.perform(get("/api/enrollments/course/2"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testGetEnrollment_Found() throws Exception {
        EnrollmentDto enrollmentDto = EnrollmentDto.builder().id(1L).studentId(1L).courseId(2L).build();
        when(enrollmentService.getEnrollment(1L, 2L)).thenReturn(Optional.of(enrollmentDto));

        mockMvc.perform(get("/api/enrollments/student/1/course/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studentId").value(1L))
                .andExpect(jsonPath("$.courseId").value(2L));
    }

    @Test
    public void testGetEnrollment_NotFound() throws Exception {
        when(enrollmentService.getEnrollment(1L, 2L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/enrollments/student/1/course/2"))
                .andExpect(status().isNotFound());
    }
}
