package com.question.learning_management_system.serviceTest.ReviewTest;

import com.question.learning_management_system.dto.ReviewDto;
import com.question.learning_management_system.request.ReviewRequest.CreateReviewRequest;
import com.question.learning_management_system.request.ReviewRequest.UpdateReviewRequest;
import com.question.learning_management_system.service.ReviewService.IReviewService;
import com.question.learning_management_system.controller.ReviewController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReviewControllerTest {

    @Mock
    private IReviewService reviewService;

    @InjectMocks
    private ReviewController reviewController;

    private ReviewDto reviewDto;
    private CreateReviewRequest createReviewRequest;
    private UpdateReviewRequest updateReviewRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Using Builder to create ReviewDto
        reviewDto = ReviewDto.builder()
                .id(1L)
                .comment("Great course!")
                .rating(5.0)
                .build();

        // Using Builder for CreateReviewRequest
        createReviewRequest = CreateReviewRequest.builder()
                .comment("Great course!")
                .rating(5.0)
                .build();

        // Using Builder for UpdateReviewRequest
        updateReviewRequest = UpdateReviewRequest.builder()
                .comment("Updated review content")
                .rating(4.0)
                .build();
    }

    @Test
    public void testGetAllCommentsByCourseId() {
        Long courseId = 1L;
        List<ReviewDto> reviews = Arrays.asList(reviewDto);
        when(reviewService.getAllCommentByCourseId(courseId)).thenReturn(reviews);

        List<ReviewDto> result = reviewController.getAllCommentsByCourseId(courseId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Great course!", result.get(0).getComment());
        verify(reviewService, times(1)).getAllCommentByCourseId(courseId);
    }

    @Test
    public void testGetAllReviewsByCourseId() {
        Long courseId = 1L;
        List<ReviewDto> reviews = Arrays.asList(reviewDto);
        when(reviewService.getAllReviewsByCourseId(courseId)).thenReturn(reviews);

        ResponseEntity<List<ReviewDto>> response = reviewController.getAllReviewsByCourseId(courseId);

        assertNotNull(response);
        assertEquals(1, response.getBody().size());
        assertEquals("Great course!", response.getBody().get(0).getComment());
        verify(reviewService, times(1)).getAllReviewsByCourseId(courseId);
    }

    @Test
    public void testCreateReview() {
        when(reviewService.createReview(createReviewRequest)).thenReturn(reviewDto);

        ResponseEntity<ReviewDto> response = reviewController.createReview(createReviewRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Great course!", response.getBody().getComment());
        verify(reviewService, times(1)).createReview(createReviewRequest);
    }

    @Test
    public void testUpdateReview() {
        Long reviewId = 1L;
        when(reviewService.updateReview(updateReviewRequest, reviewId)).thenReturn(reviewDto);

        ResponseEntity<ReviewDto> response = reviewController.updateReview(updateReviewRequest, reviewId);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Great course!", response.getBody().getComment());
        verify(reviewService, times(1)).updateReview(updateReviewRequest, reviewId);
    }

    @Test
    public void testDeleteReview() {
        Long reviewId = 1L;

        ResponseEntity<Void> response = reviewController.deleteReview(reviewId);

        assertEquals(204, response.getStatusCodeValue());
        verify(reviewService, times(1)).deleteReview(reviewId);
    }

    @Test
    public void testGetAverageRatingByCourseId() {
        Long courseId = 1L;
        Double averageRating = 4.5;
        when(reviewService.getAverageRatingByCourseId(courseId)).thenReturn(averageRating);

        ResponseEntity<Double> response = reviewController.getAverageRatingByCourseId(courseId);

        assertNotNull(response);
        assertEquals(4.5, response.getBody());
        verify(reviewService, times(1)).getAverageRatingByCourseId(courseId);
    }

    @Test
    public void testGetAverageRatingByInstructorName() {
        String instructorName = "John Doe";
        Double averageRating = 4.7;
        when(reviewService.getAverageRatingByInstructorName(instructorName)).thenReturn(averageRating);

        ResponseEntity<Double> response = reviewController.getAverageRatingByInstructorName(instructorName);

        assertNotNull(response);
        assertEquals(4.7, response.getBody());
        verify(reviewService, times(1)).getAverageRatingByInstructorName(instructorName);
    }
}
