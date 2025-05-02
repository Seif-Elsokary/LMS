package com.question.learning_management_system.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CommentDto {

    private Long id;
    private String content;
    private boolean isDiscussion;
    private Long parentCommentId;
    private Long courseId;
    private Long studentId;
    private Long instructorId;

}