package com.question.learning_management_system.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private Integer numberOfQuestions;

    private Double totalMarks;

    private String difficulty;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToMany(mappedBy = "quizzes")
    private List<Student> students;



}
