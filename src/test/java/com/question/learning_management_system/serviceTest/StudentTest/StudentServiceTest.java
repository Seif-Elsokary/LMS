package com.question.learning_management_system.serviceTest.StudentTest;

import com.question.learning_management_system.dto.StudentDto;
import com.question.learning_management_system.entity.Student;
import com.question.learning_management_system.exception.StudentNotFoundException;
import com.question.learning_management_system.repository.CourseRepository;
import com.question.learning_management_system.repository.StudentRepository;
import com.question.learning_management_system.request.StudentRequest.CreateStudentRequest;
import com.question.learning_management_system.request.StudentRequest.UpdateStudentRequest;
import com.question.learning_management_system.service.StudentService.StudentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private StudentService studentService;



    @Test
    void testGetStudentByEmail_NotFound() {
        when(studentRepository.findByEmail("missing@example.com")).thenReturn(null);

        assertThrows(StudentNotFoundException.class, () -> studentService.getStudentByEmail("missing@example.com"));
    }


    @Test
    void testGetStudentsByName() {
        List<Student> students = Arrays.asList(
                Student.builder().id(1L).name("Ali").build(),
                Student.builder().id(2L).name("Ali").build()
        );

        when(studentRepository.findByName("Ali")).thenReturn(students);

        List<StudentDto> result = studentService.getStudentsByName("Ali");

        assertEquals(2, result.size());
    }

    @Test
    void testAddStudent() {
        CreateStudentRequest request = CreateStudentRequest.builder()
                .name("Ali")
                .email("ali@example.com")
                .phoneNumber("1234567890")
                .password("plainPassword")
                .age(20)
                .gender("MALE")
                .build();

        String encodedPassword = "encodedPassword";

        when(passwordEncoder.encode("plainPassword")).thenReturn(encodedPassword);

        Student savedStudent = Student.builder()
                .id(1L)
                .name("Ali")
                .email("ali@example.com")
                .phoneNumber("1234567890")
                .password(encodedPassword)
                .age(20)
                .gender("MALE")
                .build();

        when(studentRepository.save(any(Student.class))).thenReturn(savedStudent);

        StudentDto expectedDto = StudentDto.builder()
                .id(1L)
                .name("Ali")
                .email("ali@example.com")
                .phoneNumber("1234567890")
                .age(20)
                .gender("MALE")
                .build();

        when(modelMapper.map(savedStudent, StudentDto.class)).thenReturn(expectedDto);

        StudentDto result = studentService.addStudent(request);

        assertEquals("Ali", result.getName());
        assertEquals("ali@example.com", result.getEmail());
    }

    @Test
    void testUpdateStudent_Success() {
        UpdateStudentRequest request = UpdateStudentRequest.builder()
                .name("Ali Updated")
                .age(22)
                .gender("MALE")
                .email("updated@example.com")
                .phoneNumber("9876543210")
                .build();

        Student existingStudent = Student.builder().id(1L).name("Old Name").build();

        when(studentRepository.findById(1L)).thenReturn(Optional.of(existingStudent));
        when(studentRepository.save(any(Student.class))).thenReturn(existingStudent);

        StudentDto expectedDto = StudentDto.builder()
                .id(1L)
                .name("Ali Updated")
                .email("updated@example.com")
                .phoneNumber("9876543210")
                .age(22)
                .gender("MALE")
                .build();

        when(modelMapper.map(existingStudent, StudentDto.class)).thenReturn(expectedDto);

        StudentDto result = studentService.updateStudent(request, 1L);

        assertEquals("Ali Updated", result.getName());
        assertEquals("updated@example.com", result.getEmail());
    }

    @Test
    void testUpdateStudent_InvalidGender() {
        UpdateStudentRequest request = UpdateStudentRequest.builder()
                .gender("OTHER")
                .build();

        assertThrows(IllegalArgumentException.class,
                () -> studentService.updateStudent(request, 1L));
    }

    @Test
    void testDeleteStudentById_NotFound() {
        when(studentRepository.existsById(1L)).thenReturn(false);

        assertThrows(StudentNotFoundException.class, () -> studentService.deleteStudentById(1L));
    }

    @Test
    void testDeleteStudentById_Success() {
        when(studentRepository.existsById(1L)).thenReturn(true);

        studentService.deleteStudentById(1L);

        verify(studentRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteStudentByPhoneNumber_Success() {
        when(studentRepository.existsByPhoneNumber("1234567890")).thenReturn(true);

        studentService.deleteStudentByPhoneNumber("1234567890");

        verify(studentRepository, times(1)).deleteByPhoneNumber("1234567890");
    }

    @Test
    void testDeleteStudentByPhoneNumber_NotFound() {
        when(studentRepository.existsByPhoneNumber("123")).thenReturn(false);

        assertThrows(StudentNotFoundException.class, () -> studentService.deleteStudentByPhoneNumber("123"));
    }

    @Test
    void testIsExistsByEmail() {
        when(studentRepository.existsByEmail("test@example.com")).thenReturn(true);

        assertTrue(studentService.isExistsByEmail("test@example.com"));
    }

    @Test
    void testIsExistsByPhoneNumber() {
        when(studentRepository.existsByPhoneNumber("1234567890")).thenReturn(true);

        assertTrue(studentService.isExistsByPhoneNumber("1234567890"));
    }

    @Test
    void testIsExistsById() {
        when(studentRepository.existsById(1L)).thenReturn(true);

        assertTrue(studentService.isExistsById(1L));
    }

    @Test
    void testGetAllStudents() {
        List<Student> students = Arrays.asList(
                Student.builder().id(1L).name("Ali").build(),
                Student.builder().id(2L).name("Sara").build()
        );

        when(studentRepository.findAll()).thenReturn(students);

        List<StudentDto> result = studentService.getAllStudents();

        assertEquals(2, result.size());
    }
}
