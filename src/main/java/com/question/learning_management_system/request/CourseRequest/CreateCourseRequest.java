package com.question.learning_management_system.request.CourseRequest;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreateCourseRequest {
    private String title;
    private String description;
    private String courseUrl;
    private Long instructorId;
    private String categoryName;


}
