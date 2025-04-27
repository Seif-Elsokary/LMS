package com.question.learning_management_system.request.StudentRequest;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Data
public class CreateStudentRequest {

    private String name;
    private int age;
    private String gender;
    private String email;
    private String phoneNumber;
    private Long courseId;

}
