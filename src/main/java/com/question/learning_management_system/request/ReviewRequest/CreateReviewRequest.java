package com.question.learning_management_system.request.ReviewRequest;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateReviewRequest {
    private String comment;
    private Double rating;
    private Long courseId;
    private Long studentId;
}
