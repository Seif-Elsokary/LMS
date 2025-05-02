package com.question.learning_management_system.dto;

import lombok.*;

@Data
public class MailDetailsDto {
    private String toMail;
    private String message;
    private String subject;
    private String contentType;
}
