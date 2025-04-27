package com.question.learning_management_system.service.StudentService;

import com.question.learning_management_system.dto.StudentDto;
import com.question.learning_management_system.request.StudentRequest.CreateStudentRequest;
import com.question.learning_management_system.request.StudentRequest.UpdateStudentRequest;
import java.util.List;

public interface IStudentService {
    StudentDto getStudentById(Long id);
    StudentDto getStudentByEmail(String email);
    StudentDto getStudentByPhoneNumber(String phoneNumber);
    List<StudentDto> getStudentsByName(String name);
    List<StudentDto> getStudentsByAge(int age);
    List<StudentDto> getStudentsByNameAndAge(String name, int age);
    List<StudentDto> getStudentsByGender(String gender);
    List<StudentDto> getStudentsByAgeAndGender(int age, String gender);
    List<StudentDto> getAllStudents();
    StudentDto addStudent(CreateStudentRequest request);
    StudentDto updateStudent(UpdateStudentRequest request, Long id);
    void assignCourseToStudent(Long studentId, Long courseId);
    void deleteStudentById(Long id);
    void deleteStudentByPhoneNumber(String phoneNumber);
    boolean isExistsByEmail(String email);
    boolean isExistsByPhoneNumber(String phoneNumber);
}

