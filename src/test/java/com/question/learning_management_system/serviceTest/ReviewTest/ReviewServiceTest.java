package com.question.learning_management_system.serviceTest.ReviewTest;

import com.question.learning_management_system.dto.MailDetailsDto;
import com.question.learning_management_system.dto.ReviewDto;
import com.question.learning_management_system.entity.Course;
import com.question.learning_management_system.entity.Instructor;
import com.question.learning_management_system.entity.Review;
import com.question.learning_management_system.entity.Student;
import com.question.learning_management_system.repository.CourseRepository;
import com.question.learning_management_system.repository.ReviewRepository;
import com.question.learning_management_system.repository.StudentRepository;
import com.question.learning_management_system.request.ReviewRequest.CreateReviewRequest;
import com.question.learning_management_system.request.ReviewRequest.UpdateReviewRequest;
import com.question.learning_management_system.service.EmailService.IMailService;
import com.question.learning_management_system.service.ReviewService.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private IMailService mailService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ReviewService reviewService;

    private Course course;
    private Student student;
    private Review review;

    @BeforeEach
    void setUp() {
        Instructor instructor = Instructor.builder()
                .id(1L)
                .name("Dr John")
                .email("john@example.com")
                .build();

        course = Course.builder()
                .id(1L)
                .title("Java 101")
                .instructor(instructor)
                .courseUrl("http://course-url.com")
                .build();

        student = Student.builder()
                .id(1L)
                .name("Alice")
                .build();

        review = Review.builder()
                .id(1L)
                .comment("Great!")
                .rating(4.5)
                .course(course)
                .student(student)
                .build();

        when(modelMapper.map(any(Review.class), eq(ReviewDto.class))).thenAnswer(invocation -> {
            Review source = invocation.getArgument(0);
            ReviewDto dto = new ReviewDto();
            dto.setId(source.getId());
            dto.setComment(source.getComment());
            dto.setRating(source.getRating());
            dto.setCourseName(source.getCourse().getTitle());
            dto.setInstructorName(source.getCourse().getInstructor().getName());
            dto.setStudentName(source.getStudent().getName());
            dto.setCourseUrl(source.getCourse().getCourseUrl());
            return dto;
        });
    }

    @Test
    void testCreateReview() {
        CreateReviewRequest request = CreateReviewRequest.builder()
                .courseId(1L)
                .studentId(1L)
                .comment("Great!")
                .rating(4.5)
                .build();

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(reviewRepository.save(any(Review.class))).thenReturn(review);
        when(reviewRepository.findAverageRatingByInstructorName(anyString())).thenReturn(4.5);

        ReviewDto result = reviewService.createReview(request);

        assertEquals("Great!", result.getComment());
        assertEquals(4.5, result.getRating());
        assertEquals("Java 101", result.getCourseName());
        assertEquals("Alice", result.getStudentName());
        assertEquals("Dr John", result.getInstructorName());

        verify(mailService, times(1)).sendMail(any(MailDetailsDto.class));
        verify(reviewRepository, times(1)).save(any(Review.class));
    }



    @Test
    void testGetAllReviewsByCourseId() {
        when(reviewRepository.findByCourseId(1L)).thenReturn(List.of(review));
        List<ReviewDto> result = reviewService.getAllReviewsByCourseId(1L);
        assertEquals(1, result.size());
    }

    @Test
    void testGetAllReviewsByStudentId() {
        when(reviewRepository.findByStudentId(1L)).thenReturn(List.of(review));
        List<ReviewDto> result = reviewService.getAllReviewsByStudentId(1L);
        assertEquals(1, result.size());
    }

    @Test
    void testGetAllReviews() {
        when(reviewRepository.findAll()).thenReturn(List.of(review));
        List<ReviewDto> result = reviewService.getAllReviews();
        assertEquals(1, result.size());
    }

    @Test
    void testGetReviewsByInstructorNameAndCategoryName() {
        when(reviewRepository.findByCourseInstructorNameIgnoreCaseAndCourseCategoryNameIgnoreCase(any(), any()))
                .thenReturn(List.of(review));
        List<ReviewDto> result = reviewService.getReviewsByInstructorNameAndCategoryName("DrJohn", "Java");
        assertEquals(1, result.size());
    }

    @Test
    void testGetAllReviewsByStudentAndCourseName() {
        when(courseRepository.findByTitleIgnoreCase("Java 101")).thenReturn(course);
        when(reviewRepository.findByStudentIdAndCourseId(1L, 1L)).thenReturn(List.of(review));
        List<ReviewDto> result = reviewService.getAllReviewsByStudentAndCourseName(1L, "Java 101");
        assertEquals(1, result.size());
    }


}
