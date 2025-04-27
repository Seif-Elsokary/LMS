package com.question.learning_management_system.request.QuizRequest;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateQuizRequest {

    private String title;

    private String description;

    private Integer numberOfQuestions;

    private Double totalMarks;

    private String difficulty;

}
