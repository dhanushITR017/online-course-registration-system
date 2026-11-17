package com.vcet.courseenrollment.exception;

/**
 * Base custom unchecked exception for Course Enrollment System.
 * 
 * @author Dhanush .K (Reg No: 732925ITR017)
 */
public class CourseEnrollmentException extends RuntimeException {
    public CourseEnrollmentException(String message) {
        super(message);
    }

    public CourseEnrollmentException(String message, Throwable cause) {
        super(message, cause);
    }
}
