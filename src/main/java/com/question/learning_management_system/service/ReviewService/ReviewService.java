package com.question.learning_management_system.service.ReviewService;

import com.question.learning_management_system.dto.MailDetailsDto;
import com.question.learning_management_system.dto.ReviewDto;
import com.question.learning_management_system.entity.Course;
import com.question.learning_management_system.entity.Instructor;
import com.question.learning_management_system.entity.Review;
import com.question.learning_management_system.entity.Student;
import com.question.learning_management_system.exception.ReviewNotFoundException;
import com.question.learning_management_system.exception.StudentNotFoundException;
import com.question.learning_management_system.repository.CourseRepository;
import com.question.learning_management_system.repository.ReviewRepository;
import com.question.learning_management_system.repository.StudentRepository;
import com.question.learning_management_system.request.ReviewRequest.CreateReviewRequest;
import com.question.learning_management_system.request.ReviewRequest.UpdateReviewRequest;
import com.question.learning_management_system.service.EmailService.IMailService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ReviewService implements IReviewService {

    private final ReviewRepository reviewRepository;
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final IMailService mailService;
    private final ModelMapper modelMapper;

    @Override
    public List<ReviewDto> getAllCommentByCourseId(Long courseId) {
        List<Review> reviews = reviewRepository.findByCourseId(courseId);
        return convertToDtoList(reviews);
    }

    @Override
    public ReviewDto createReview(CreateReviewRequest request) {
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ReviewNotFoundException("Course not found with id: " + request.getCourseId()));
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new StudentNotFoundException("Student not found with id: " + request.getStudentId()));



        Review review = Review.builder()
                .comment(request.getComment())
                .rating(request.getRating())
                .course(course)
                .student(student)
                .build();

        Review savedReview = reviewRepository.save(review);

        sendNewReviewEmailToInstructor(student, course, request.getComment());

        updateInstructorRating(course);

        return convertToDto(savedReview);
    }

    @Override
    public ReviewDto updateReview(UpdateReviewRequest request, Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("Review not found with id: " + reviewId));

        review.setComment(request.getComment());
        review.setRating(request.getRating());

        Review updatedReview = reviewRepository.save(review);

        if (updatedReview.getCourse() != null) {
            updateInstructorRating(updatedReview.getCourse());
        }

        return convertToDto(updatedReview);
    }

    @Override
    public void deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("Review not found with id: " + reviewId));
        Course course = review.getCourse();

        reviewRepository.delete(review);

        if (course != null) {
            updateInstructorRating(course);
        }
    }

    @Override
    public List<ReviewDto> getAllReviewsByCourseId(Long courseId) {
        List<Review> reviews = reviewRepository.findByCourseId(courseId);
        return convertToDtoList(reviews);
    }

    @Override
    public List<ReviewDto> getAllReviewsByStudentAndCourseName(Long studentId, String courseName) {
        Course course = courseRepository.findByTitleIgnoreCase(courseName);

        List<Review> reviews = reviewRepository.findByStudentIdAndCourseId(studentId, course.getId());

        if (reviews.isEmpty()) {
            throw new ReviewNotFoundException("No reviews found for student with ID: " + studentId + " for course: " + courseName);
        }

        return convertToDtoList(reviews);
    }

    @Override
    public List<ReviewDto> getAllReviewsByStudentId(Long studentId) {
        List<Review> reviews = reviewRepository.findByStudentId(studentId);
        return convertToDtoList(reviews);
    }

    @Override
    public List<ReviewDto> getAllReviews() {
        List<Review> reviews = reviewRepository.findAll();
        return convertToDtoList(reviews);
    }

    @Override
    public List<ReviewDto> getReviewsByInstructorNameAndCategoryName(String instructorName, String categoryName) {
        String[] parts = splitInstructorAndCategory(instructorName + categoryName);
        String instructor = parts[0];
        String category = parts.length > 1 ? parts[1] : "";

        List<Review> reviews = reviewRepository.findByCourseInstructorNameIgnoreCaseAndCourseCategoryNameIgnoreCase(instructor, category);
        return convertToDtoList(reviews);
    }

    @Override
    public double getAverageRatingByCourseId(Long courseId) {
        Double average = reviewRepository.findAverageRatingByCourseId(courseId);
        return average != null ? average : 0.0;
    }

    @Override
    public double getAverageRatingByInstructorName(String instructorName) {
        Double average = reviewRepository.findAverageRatingByInstructorName(instructorName);
        return average != null ? average : 0.0;
    }

    // ========================== HELPER METHODS ==========================



    private void sendNewReviewEmailToInstructor(Student student, Course course, String review) {
        MailDetailsDto mailDetailsDto = new MailDetailsDto();
        mailDetailsDto.setToMail(course.getInstructor().getEmail());
        mailDetailsDto.setSubject("🌟 New Review on Your Course");
        mailDetailsDto.setMessage(
                "<h2>New Review Alert!</h2>" +
                        "<p>Dear " + course.getInstructor().getName() + ",</p>" +
                        "<p>Student <strong>" + student.getName() + "</strong> has posted a new review for your course <strong>" + course.getTitle() + "</strong>.</p>" +
                        "<p><strong>Review:</strong></p>" +
                        "<p>" + review + "</p>" +
                        "<br><p>Feel free to respond to the student if needed.</p>"
        );
        mailDetailsDto.setContentType("html");

        mailService.sendMail(mailDetailsDto);
    }

    private void updateInstructorRating(Course course) {
        Instructor instructor = course.getInstructor();
        if (instructor != null) {
            Double newAverageRating = reviewRepository.findAverageRatingByInstructorName(instructor.getName());
            instructor.setRating(newAverageRating != null ? newAverageRating : 0.0);
            courseRepository.save(course);
        }
    }

    private ReviewDto convertToDto(Review review) {
        ReviewDto reviewDto = modelMapper.map(review, ReviewDto.class);

        if (review.getCourse() != null) {
            reviewDto.setCourseName(review.getCourse().getTitle());
            reviewDto.setCourseUrl(review.getCourse().getCourseUrl());
            if (review.getCourse().getInstructor() != null) {
                reviewDto.setInstructorName(review.getCourse().getInstructor().getName());
            }
        }

        if (review.getStudent() != null) {
            reviewDto.setStudentName(review.getStudent().getName());
        }

        return reviewDto;
    }

    private List<ReviewDto> convertToDtoList(List<Review> reviews) {
        return reviews.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private String[] splitInstructorAndCategory(String instructorCategory) {
        String instructor = instructorCategory.replaceAll("([a-z])([A-Z])", "$1 $2").trim();
        Pattern pattern = Pattern.compile("([a-z]+|[A-Z][a-z]*)");
        Matcher matcher = pattern.matcher(instructor);

        List<String> parts = matcher.results()
                .map(MatchResult::group)
                .collect(Collectors.toList());

        if (parts.size() >= 2) {
            return new String[]{parts.get(0), parts.get(1)};
        } else {
            return new String[]{parts.get(0), ""};
        }
    }
}
