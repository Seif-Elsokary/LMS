package com.question.learning_management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDto {
    private Long id;
    private String comment;
    private Double rating;
    private Long courseId;
    private String courseName;
    private String courseUrl;
    private String instructorName;
    private Long studentId;
    private String studentName;
}

