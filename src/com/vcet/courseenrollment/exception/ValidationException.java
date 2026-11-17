package com.vcet.courseenrollment.exception;

/**
 * Thrown when an input fails domain validation rules (e.g. invalid year of study, empty name, invalid score range).
 * 
 * @author Dhanush .K (Reg No: 732925ITR017)
 */
public class ValidationException extends CourseEnrollmentException {
    public ValidationException(String message) {
        super(message);
    }
}
