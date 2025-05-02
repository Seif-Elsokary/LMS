package com.question.learning_management_system.serviceTest.StudentTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.question.learning_management_system.controller.StudentController;
import com.question.learning_management_system.dto.StudentDto;
import com.question.learning_management_system.request.StudentRequest.CreateStudentRequest;
import com.question.learning_management_system.request.StudentRequest.UpdateStudentRequest;
import com.question.learning_management_system.service.StudentService.IStudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class StudentControllerTest {

    @Mock
    private IStudentService studentService;

    @InjectMocks
    private StudentController studentController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private StudentDto studentDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();
        objectMapper = new ObjectMapper();

        studentDto = StudentDto.builder()
                .id(1L)
                .name("Ali")
                .email("ali@example.com")
                .phoneNumber("123456789")
                .age(20)
                .gender("MALE")
                .build();
    }

    @Test
    void testGetById() throws Exception {
        when(studentService.getStudentById(1L)).thenReturn(studentDto);
        mockMvc.perform(get("/api/students/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Ali"));
    }

    @Test
    void testGetByEmail() throws Exception {
        when(studentService.getStudentByEmail("ali@example.com")).thenReturn(studentDto);
        mockMvc.perform(get("/api/students/email").param("email", "ali@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.email").value("ali@example.com"));
    }

    @Test
    void testGetByPhoneNumber() throws Exception {
        when(studentService.getStudentByPhoneNumber("123456789")).thenReturn(studentDto);
        mockMvc.perform(get("/api/students/phone").param("phoneNumber", "123456789"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.phoneNumber").value("123456789"));
    }

    @Test
    void testGetByName() throws Exception {
        when(studentService.getStudentsByName("Ali")).thenReturn(List.of(studentDto));
        mockMvc.perform(get("/api/students/name").param("name", "Ali"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].name").value("Ali"));
    }

    @Test
    void testGetByAge() throws Exception {
        when(studentService.getStudentsByAge(20)).thenReturn(List.of(studentDto));
        mockMvc.perform(get("/api/students/age").param("age", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].age").value(20));
    }

    @Test
    void testGetByNameAndAge() throws Exception {
        when(studentService.getStudentsByNameAndAge("Ali", 20)).thenReturn(List.of(studentDto));
        mockMvc.perform(get("/api/students/name-age").param("name", "Ali").param("age", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].name").value("Ali"));
    }

    @Test
    void testGetByGender() throws Exception {
        when(studentService.getStudentsByGender("MALE")).thenReturn(List.of(studentDto));
        mockMvc.perform(get("/api/students/gender").param("gender", "MALE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].gender").value("MALE"));
    }

    @Test
    void testGetByAgeAndGender() throws Exception {
        when(studentService.getStudentsByAgeAndGender(20, "MALE")).thenReturn(List.of(studentDto));
        mockMvc.perform(get("/api/students/age-gender")
                        .param("age", "20")
                        .param("gender", "MALE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].gender").value("MALE"));
    }

    @Test
    void testGetAllStudents() throws Exception {
        when(studentService.getAllStudents()).thenReturn(List.of(studentDto));
        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].name").value("Ali"));
    }

    @Test
    void testAddStudent() throws Exception {
        CreateStudentRequest request = CreateStudentRequest.builder()
                .name("Ali")
                .email("ali@example.com")
                .phoneNumber("123456789")
                .build();

        when(studentService.addStudent(any())).thenReturn(studentDto);

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Ali"));
    }

    @Test
    void testUpdateStudent() throws Exception {
        UpdateStudentRequest request = UpdateStudentRequest.builder()
                .name("Ali Updated")
                .email("ali_updated@example.com")
                .phoneNumber("123456789")
                .age(21)
                .gender("MALE")
                .build();

        studentDto.setName("Ali Updated");

        when(studentService.updateStudent(any(), eq(1L))).thenReturn(studentDto);

        mockMvc.perform(put("/api/students/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Ali Updated"));
    }

    @Test
    void testDeleteById() throws Exception {
        doNothing().when(studentService).deleteStudentById(1L);
        mockMvc.perform(delete("/api/students/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Student deleted successfully by ID."));
    }

    @Test
    void testDeleteByPhoneNumber() throws Exception {
        doNothing().when(studentService).deleteStudentByPhoneNumber("123456789");
        mockMvc.perform(delete("/api/students/phone").param("phoneNumber", "123456789"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Student deleted successfully by phone number."));
    }
}
