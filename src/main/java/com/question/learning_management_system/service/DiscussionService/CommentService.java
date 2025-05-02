package com.question.learning_management_system.service.DiscussionService;

import com.question.learning_management_system.dto.CommentDto;
import com.question.learning_management_system.entity.Comment;
import com.question.learning_management_system.entity.Course;
import com.question.learning_management_system.entity.Instructor;
import com.question.learning_management_system.entity.Student;
import com.question.learning_management_system.exception.AlreadyExistsException;
import com.question.learning_management_system.exception.CourseNotFoundException;
import com.question.learning_management_system.exception.InstructorNotFoundException;
import com.question.learning_management_system.exception.StudentNotFoundException;
import com.question.learning_management_system.repository.CommentRepository;
import com.question.learning_management_system.repository.CourseRepository;
import com.question.learning_management_system.repository.InstructorRepository;
import com.question.learning_management_system.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService implements ICommentService {

    private final CommentRepository commentRepository;
    private final InstructorRepository instructorRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final ModelMapper modelMapper;

    @Override
    public CommentDto addComment(CommentDto commentDto) {

        Optional<Comment> existingComment = commentRepository
                .findCommentByStudentIdAndCourseId(commentDto.getStudentId(), commentDto.getCourseId());

        if (existingComment.isPresent()) {
            throw new AlreadyExistsException("Comment already exists!");
        }

        Comment comment = modelMapper.map(commentDto, Comment.class);

        // 🔥 هذا المهم: نربط الكائنات المرتبطة بشكل صحيح

        if (commentDto.getInstructorId() != null) {
            Instructor instructor = instructorRepository.findById(commentDto.getInstructorId())
                    .orElseThrow(() -> new InstructorNotFoundException("Instructor not found"));
            comment.setInstructor(instructor);
        }

        if (commentDto.getStudentId() != null) {
            Student student = studentRepository.findById(commentDto.getStudentId())
                    .orElseThrow(() -> new StudentNotFoundException("Student not found"));
            comment.setStudent(student);
        }

        if (commentDto.getCourseId() != null) {
            Course course = courseRepository.findById(commentDto.getCourseId())
                    .orElseThrow(() -> new CourseNotFoundException("Course not found"));
            comment.setCourse(course);
        }

        comment = commentRepository.save(comment);
        return modelMapper.map(comment, CommentDto.class);
    }

    @Override
    public CommentDto updateComment(Long id, CommentDto commentDto) {

        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        comment.setContent(commentDto.getContent());
        comment.setDiscussion(commentDto.isDiscussion());
        comment = commentRepository.save(comment);

        return modelMapper.map(comment, CommentDto.class);
    }

    @Override
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }

    @Override
    public List<CommentDto> getCommentsByCourse(Long courseId) {
        List<Comment> comments = commentRepository.findByCourseId(courseId);
        return comments.stream()
                .map(comment -> modelMapper.map(comment, CommentDto.class))
                .toList();
    }

    @Override
    public Optional<CommentDto> CommentById(Long id) {
        return commentRepository.findById(id)
                .map(comment -> modelMapper.map(comment, CommentDto.class));
    }

    @Override
    public List<CommentDto> getCommentsByStudentId(Long studentId) {
        List<Comment> comments = commentRepository.findByStudentId(studentId);
        return comments.stream()
                .map(comment -> modelMapper.map(comment, CommentDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentDto> getCommentsByStudentIdAndCourseId(Long studentId, Long courseId) {
        List<Comment> comments = commentRepository.findByStudentIdAndCourseId(studentId, courseId);
        return comments.stream()
                .map(comment -> modelMapper.map(comment, CommentDto.class))
                .collect(Collectors.toList());
    }

}
