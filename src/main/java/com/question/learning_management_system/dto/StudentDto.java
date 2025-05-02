package com.question.learning_management_system.dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class StudentDto {

    private Long id;
    private String name;
    private int age;
    private String gender;
    @Email
    private String email;
    private String phoneNumber;
    private List<CourseDto> courses;
    private List<CommentDto> comments;
    private List<ReviewDto> reviews;
}
