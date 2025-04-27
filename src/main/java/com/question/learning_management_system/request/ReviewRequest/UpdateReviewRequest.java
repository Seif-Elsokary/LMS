package com.question.learning_management_system.request.ReviewRequest;

import lombok.Data;

@Data
public class UpdateReviewRequest {
    private String comment;
    private Double rating;

}
