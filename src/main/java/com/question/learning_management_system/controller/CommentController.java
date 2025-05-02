package com.question.learning_management_system.controller;

import com.question.learning_management_system.dto.CommentDto;
import com.question.learning_management_system.service.DiscussionService.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping
    public ResponseEntity<CommentDto> addComment(@RequestBody CommentDto commentDto) {
        CommentDto createdComment = commentService.addComment(commentDto);
        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PutMapping("/{id}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable Long id, @RequestBody CommentDto commentDto) {
        CommentDto updatedComment = commentService.updateComment(id, commentDto);
        return new ResponseEntity<>(updatedComment, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<CommentDto>> getCommentsByCourse(@PathVariable Long courseId) {
        List<CommentDto> comments = commentService.getCommentsByCourse(courseId);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<CommentDto>> getCommentsByStudent(@PathVariable Long studentId) {
        List<CommentDto> comments = commentService.getCommentsByStudentId(studentId);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/student/{studentId}/course/{courseId}")
    public ResponseEntity<List<CommentDto>> getCommentsByStudentAndCourse(@PathVariable Long studentId, @PathVariable Long courseId) {
        List<CommentDto> comments = commentService.getCommentsByStudentIdAndCourseId(studentId, courseId);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }
}
