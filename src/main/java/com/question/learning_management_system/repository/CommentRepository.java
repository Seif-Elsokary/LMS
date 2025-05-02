package com.question.learning_management_system.repository;

import com.question.learning_management_system.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByStudentIdAndCourseId(Long studentId, Long courseId);
    Optional<Comment> findCommentByStudentIdAndCourseId(Long studentId, Long courseId);


    List<Comment> findByCourseId(Long courseId);

    List<Comment> findByStudentId(Long studentId);
}
