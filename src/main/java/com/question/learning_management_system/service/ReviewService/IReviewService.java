package com.question.learning_management_system.service.ReviewService;

import com.question.learning_management_system.dto.ReviewDto;
import com.question.learning_management_system.request.ReviewRequest.CreateReviewRequest;
import com.question.learning_management_system.request.ReviewRequest.UpdateReviewRequest;

import java.util.List;

public interface IReviewService {
    ReviewDto createReview(CreateReviewRequest request);
    ReviewDto updateReview(UpdateReviewRequest request , Long reviewId);
    void deleteReview(Long reviewId);
    List<ReviewDto> getAllReviewsByCourseId(Long courseId);
    List<ReviewDto> getAllReviewsByStudentAndCourseName(Long studentId , String courseName);
    List<ReviewDto> getAllReviewsByStudentId(Long studentId);
    List<ReviewDto> getAllReviews();
    List<ReviewDto> getReviewsByInstructorNameAndCategoryName(String instructorName , String categoryName);
    double getAverageRatingByCourseId(Long courseId);

    double getAverageRatingByInstructorName(String instructorName);
    List<ReviewDto> getAllCommentByCourseId(Long courseId);
}
