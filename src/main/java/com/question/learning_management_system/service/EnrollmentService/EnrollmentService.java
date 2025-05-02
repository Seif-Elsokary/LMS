package com.question.learning_management_system.service.EnrollmentService;

import com.question.learning_management_system.dto.EnrollmentDto;
import com.question.learning_management_system.dto.MailDetailsDto;
import com.question.learning_management_system.entity.Course;
import com.question.learning_management_system.entity.Enrollment;
import com.question.learning_management_system.entity.Student;
import com.question.learning_management_system.exception.AlreadyExistsException;
import com.question.learning_management_system.repository.CourseRepository;
import com.question.learning_management_system.repository.EnrollmentRepository;
import com.question.learning_management_system.repository.StudentRepository;
import com.question.learning_management_system.service.EmailService.IMailService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class EnrollmentService implements IEnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final ModelMapper modelMapper;
    private final IMailService mailService;

    @Override
    public EnrollmentDto enrollStudentInCourse(Long studentId, Long courseId) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Optional<Enrollment> existingEnrollment = enrollmentRepository.findByStudentAndCourse(student, course);
        if (existingEnrollment.isPresent()) {
            throw new AlreadyExistsException("Student is already enrolled in this course");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setEnrollmentDate(LocalDateTime.now());

        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);

        // Send Welcome Email to Student
        sendWelcomeEmailToStudent(student, course);

        // Notify Instructor about New Student
        sendNotificationEmailToInstructor(student, course);

        return convertToDto(savedEnrollment);
    }

    @Override
    public List<EnrollmentDto> getEnrollmentsByStudent(Long studentId) {
        List<Enrollment> enrollments = enrollmentRepository.findByStudentId(studentId);
        return convertToDtoList(enrollments);
    }

    @Override
    public List<EnrollmentDto> getEnrollmentsByCourse(Long courseId) {
        List<Enrollment> enrollments = enrollmentRepository.findByCourseId(courseId);
        return convertToDtoList(enrollments);
    }

    @Override
    public Optional<EnrollmentDto> getEnrollment(Long studentId, Long courseId) {
        Optional<Enrollment> enrollment = enrollmentRepository.findByStudentIdAndCourseId(studentId, courseId);
        return enrollment.map(this::convertToDto);
    }


    // ================================================================ HELPER METHODS ===========================================================



    private void sendWelcomeEmailToStudent(Student student, Course course) {
        MailDetailsDto mailDetailsDto = new MailDetailsDto();
        mailDetailsDto.setToMail(student.getEmail());
        mailDetailsDto.setSubject("🎉 Welcome to " + course.getTitle() + "!");
        mailDetailsDto.setMessage(
                "<h2>Welcome, " + student.getName() + "!</h2>" +
                        "<p>We're excited to have you in the <strong>" + course.getTitle() + "</strong> course.</p>" +
                        "<p>Your instructor, <strong>" + course.getInstructor().getName() + "</strong>, looks forward to guiding you.</p>" +
                        "<br><p>Enjoy your learning journey! 🚀</p>"
        );
        mailDetailsDto.setContentType("html");

        mailService.sendMail(mailDetailsDto);
    }

    private void sendNotificationEmailToInstructor(Student student, Course course) {
        MailDetailsDto mailDetailsDto = new MailDetailsDto();
        mailDetailsDto.setToMail(course.getInstructor().getEmail());
        mailDetailsDto.setSubject("📢 New Student Enrollment");
        mailDetailsDto.setMessage(
                "<h2>New Enrollment Alert!</h2>" +
                        "<p>Dear " + course.getInstructor().getName() + ",</p>" +
                        "<p>Student <strong>" + student.getName() + "</strong> has enrolled in your course <strong>" + course.getTitle() + "</strong>.</p>" +
                        "<br><p>Keep inspiring learners! ✨</p>"
        );
        mailDetailsDto.setContentType("html");

        mailService.sendMail(mailDetailsDto);
    }

    private void sendCancellationEmailToInstructor(Student student, Course course) {
        MailDetailsDto mailDetailsDto = new MailDetailsDto();
        mailDetailsDto.setToMail(course.getInstructor().getEmail());
        mailDetailsDto.setSubject("❌ Student Unenrolled");
        mailDetailsDto.setMessage(
                "<h2>Unenrollment Alert!</h2>" +
                        "<p>Dear " + course.getInstructor().getName() + ",</p>" +
                        "<p>Student <strong>" + student.getName() + "</strong> has unenrolled from your course <strong>" + course.getTitle() + "</strong>.</p>" +
                        "<br><p>Feel free to reach out if you have any questions.</p>"
        );
        mailDetailsDto.setContentType("html");

        mailService.sendMail(mailDetailsDto);
    }

    @Override
    public void cancelEnrollment(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));

        Course course = enrollment.getCourse();
        Student student = enrollment.getStudent();

        enrollmentRepository.delete(enrollment);

        sendCancellationEmailToInstructor(student, course);
    }





    private EnrollmentDto convertToDto(Enrollment enrollment) {
        EnrollmentDto dto = modelMapper.map(enrollment, EnrollmentDto.class);

        dto.setCourseId(enrollment.getCourse().getId());
        dto.setCourseName(enrollment.getCourse().getTitle());
        dto.setStudentId(enrollment.getStudent().getId());
        dto.setStudentName(enrollment.getStudent().getName());
        dto.setEnrollmentDate(enrollment.getEnrollmentDate());

        return dto;
    }

    private List<EnrollmentDto> convertToDtoList(List<Enrollment> enrollments) {
        return enrollments.stream()
                .map(this::convertToDto)
                .toList();
    }
}
