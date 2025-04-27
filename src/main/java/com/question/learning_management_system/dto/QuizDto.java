package com.question.learning_management_system.dto;

import lombok.Data;

@Data
public class QuizDto {

    private Long id;

    private String title;

    private String description;

    private Integer numberOfQuestions;

    private Double totalMarks;

    private String difficulty;

    private Long courseId;

    private String courseTitle;

}
