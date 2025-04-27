package com.question.learning_management_system.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class InstructorDto {
    private Long id;
    private String name;
    private String email;
    private String bio;
    private String phoneNumber;
    private Double rating;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<CourseDto> courses;
}
