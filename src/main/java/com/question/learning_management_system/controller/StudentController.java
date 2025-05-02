package com.question.learning_management_system.controller;

import com.question.learning_management_system.response.ApiResponse;
import com.question.learning_management_system.dto.StudentDto;
import com.question.learning_management_system.request.StudentRequest.CreateStudentRequest;
import com.question.learning_management_system.request.StudentRequest.UpdateStudentRequest;
import com.question.learning_management_system.service.StudentService.IStudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final IStudentService studentService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<StudentDto>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>(true, studentService.getStudentById(id), "Student fetched successfully."));
    }

    @GetMapping("/email")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<StudentDto>> getByEmail(@RequestParam String email) {
        return ResponseEntity.ok(new ApiResponse<>(true, studentService.getStudentByEmail(email), "Student fetched by email."));
    }

    @GetMapping("/phone")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<StudentDto>> getByPhoneNumber(@RequestParam String phoneNumber) {
        return ResponseEntity.ok(new ApiResponse<>(true, studentService.getStudentByPhoneNumber(phoneNumber), "Student fetched by phone number."));
    }

    @GetMapping("/name")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<List<StudentDto>>> getByName(@RequestParam String name) {
        return ResponseEntity.ok(new ApiResponse<>(true, studentService.getStudentsByName(name), "Students fetched by name."));
    }

    @GetMapping("/age")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<List<StudentDto>>> getByAge(@RequestParam int age) {
        return ResponseEntity.ok(new ApiResponse<>(true, studentService.getStudentsByAge(age), "Students fetched by age."));
    }

    @GetMapping("/name-age")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<List<StudentDto>>> getByNameAndAge(@RequestParam String name, @RequestParam int age) {
        return ResponseEntity.ok(new ApiResponse<>(true, studentService.getStudentsByNameAndAge(name, age), "Students fetched by name and age."));
    }

    @GetMapping("/gender")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<List<StudentDto>>> getByGender(@RequestParam String gender) {
        return ResponseEntity.ok(new ApiResponse<>(true, studentService.getStudentsByGender(gender.toUpperCase()), "Students fetched by gender."));
    }

    @GetMapping("/age-gender")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<List<StudentDto>>> getByAgeAndGender(@RequestParam int age, @RequestParam String gender) {
        return ResponseEntity.ok(new ApiResponse<>(true, studentService.getStudentsByAgeAndGender(age, gender.toUpperCase()), "Students fetched by age and gender."));
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<List<StudentDto>>> getAllStudents() {
        return ResponseEntity.ok(new ApiResponse<>(true, studentService.getAllStudents(), "All students fetched."));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<StudentDto>> addStudent(@RequestBody CreateStudentRequest request) {
        return ResponseEntity.ok(new ApiResponse<>(true, studentService.addStudent(request), "Student added successfully."));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<StudentDto>> updateStudent(@RequestBody UpdateStudentRequest request, @PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>(true, studentService.updateStudent(request, id), "Student updated successfully."));
    }

    @PostMapping("/{studentId}/courses/{courseId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<String>> assignCourseToStudent(@PathVariable Long studentId, @PathVariable Long courseId) {
        return ResponseEntity.ok(new ApiResponse<>(true, null, "Course assigned to student successfully."));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<String>> deleteById(@PathVariable Long id) {
        studentService.deleteStudentById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, null, "Student deleted successfully by ID."));
    }

    @DeleteMapping("/phone")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<String>> deleteByPhoneNumber(@RequestParam String phoneNumber) {
        studentService.deleteStudentByPhoneNumber(phoneNumber);
        return ResponseEntity.ok(new ApiResponse<>(true, null, "Student deleted successfully by phone number."));
    }
}
