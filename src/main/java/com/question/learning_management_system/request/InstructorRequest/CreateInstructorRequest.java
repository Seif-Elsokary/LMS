package com.question.learning_management_system.request.InstructorRequest;

import lombok.*;

@Data
@Builder
public class CreateInstructorRequest {
    private String name;
    private String email;
    private String phoneNumber;
    private String bio;
    private String gender;
    private int age;
    private String password;
}
