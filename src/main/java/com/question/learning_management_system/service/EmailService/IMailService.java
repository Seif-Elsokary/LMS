package com.question.learning_management_system.service.EmailService;


import com.question.learning_management_system.dto.MailDetailsDto;

// Interface defining the methods for sending mail
public interface IMailService {

    // Method to send a simple email
    String sendMail(MailDetailsDto mailDetailsDto);

    String sendHtmlMail(MailDetailsDto mailDetailsDto);

    // Method to send an email with attachment
    String sendMailWithAttachment(MailDetailsDto mailDetailsDto);
}


