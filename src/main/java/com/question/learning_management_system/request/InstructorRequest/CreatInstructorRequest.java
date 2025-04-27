package com.question.learning_management_system.request.InstructorRequest;

import lombok.*;

@Data
@Builder
public class CreatInstructorRequest {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private String bio;
}
