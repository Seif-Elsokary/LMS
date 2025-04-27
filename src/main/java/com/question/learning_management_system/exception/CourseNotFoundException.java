package com.question.learning_management_system.exception;

public class CourseNotFoundException extends RuntimeException {
    public CourseNotFoundException(String s) {
        super(s);
    }
}
