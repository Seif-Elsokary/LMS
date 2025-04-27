package com.question.learning_management_system.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CourseDto {
    private Long id;
    private String title;
    private String description;
    private String courseUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long instructorId;
    private String instructorName;
    private String categoryName;
}
