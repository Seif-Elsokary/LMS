package com.question.learning_management_system.request.InstructorRequest;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateInstructorRequest {
    private String name;
    private String email;
    private String phoneNumber;
    private String bio;
    private int age;
    private String password;
}
