package com.question.learning_management_system.controller;

import com.question.learning_management_system.dto.ReviewDto;
import com.question.learning_management_system.request.ReviewRequest.CreateReviewRequest;
import com.question.learning_management_system.request.ReviewRequest.UpdateReviewRequest;
import com.question.learning_management_system.service.ReviewService.IReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final IReviewService reviewService;

    @GetMapping("/comments/course/{courseId}")
    public List<ReviewDto> getAllCommentsByCourseId(@PathVariable Long courseId) {
        return reviewService.getAllCommentByCourseId(courseId);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<ReviewDto>> getAllReviewsByCourseId(@PathVariable Long courseId) {
        return ResponseEntity.ok(reviewService.getAllReviewsByCourseId(courseId));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<ReviewDto>> getAllReviewsByStudentId(@PathVariable Long studentId) {
        return ResponseEntity.ok(reviewService.getAllReviewsByStudentId(studentId));
    }

    @GetMapping("/student/{studentId}/course/{courseName}")
    public ResponseEntity<List<ReviewDto>> getAllReviewsByStudentAndCourseName(@PathVariable Long studentId, @PathVariable String courseName) {
        return ResponseEntity.ok(reviewService.getAllReviewsByStudentAndCourseName(studentId, courseName));
    }

    @GetMapping
    public ResponseEntity<List<ReviewDto>> getAllReviews() {
        return ResponseEntity.ok(reviewService.getAllReviews());
    }

    @PostMapping
    public ResponseEntity<ReviewDto> createReview(@RequestBody CreateReviewRequest request) {
        return ResponseEntity.ok(reviewService.createReview(request));
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewDto> updateReview(@RequestBody UpdateReviewRequest request, @PathVariable Long reviewId) {
        return ResponseEntity.ok(reviewService.updateReview(request, reviewId));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/instructor/{instructorName}/category/{categoryName}")
    public ResponseEntity<List<ReviewDto>> getReviewsByInstructorNameAndCategoryName(@PathVariable String instructorName, @PathVariable String categoryName) {
        return ResponseEntity.ok(reviewService.getReviewsByInstructorNameAndCategoryName(instructorName, categoryName));
    }

    @GetMapping("/course/{courseId}/average")
    public ResponseEntity<Double> getAverageRatingByCourseId(@PathVariable Long courseId) {
        return ResponseEntity.ok(reviewService.getAverageRatingByCourseId(courseId));
    }

    @GetMapping("/instructor/{instructorName}/average")
    public ResponseEntity<Double> getAverageRatingByInstructorName(@PathVariable String instructorName) {
        return ResponseEntity.ok(reviewService.getAverageRatingByInstructorName(instructorName));
    }
}
