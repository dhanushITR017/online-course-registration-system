package com.vcet.courseenrollment.exception;

/**
 * Thrown when a queried student, course, or enrollment record does not exist.
 * 
 * @author Dhanush .K (Reg No: 732925ITR017)
 */
public class RecordNotFoundException extends CourseEnrollmentException {
    public RecordNotFoundException(String message) {
        super(message);
    }
}
