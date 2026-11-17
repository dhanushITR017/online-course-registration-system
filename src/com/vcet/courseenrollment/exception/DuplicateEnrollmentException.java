package com.vcet.courseenrollment.exception;

/**
 * Thrown when a student attempts to register for a course they are already enrolled in.
 * Satisfies Functional Requirement 3: "prevent duplicate registrations".
 * 
 * @author Dhanush .K (Reg No: 732925ITR017)
 */
public class DuplicateEnrollmentException extends CourseEnrollmentException {
    public DuplicateEnrollmentException(String message) {
        super(message);
    }
}
