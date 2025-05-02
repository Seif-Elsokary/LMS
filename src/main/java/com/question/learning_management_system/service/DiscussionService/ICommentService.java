package com.question.learning_management_system.service.DiscussionService;

import com.question.learning_management_system.dto.CommentDto;

import java.util.List;
import java.util.Optional;

public interface ICommentService {

    CommentDto addComment(CommentDto discussionOrCommentDto);

    CommentDto updateComment(Long id, CommentDto discussionOrCommentDto);

    void deleteComment(Long id);

    List<CommentDto> getCommentsByCourse(Long courseId);

    Optional<CommentDto> CommentById(Long id);

    List<CommentDto> getCommentsByStudentId(Long studentId);

    List<CommentDto> getCommentsByStudentIdAndCourseId(Long studentId, Long courseId);
}
