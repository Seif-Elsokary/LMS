package com.question.learning_management_system.request.StudentRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UpdateStudentRequest {

    private String name;
    private int age;
    private String gender;
    private String email;
    private String phoneNumber;
}
