package com.vcet.courseenrollment.exception;

/**
 * Thrown when a student attempts to register for a course that has reached maximum capacity.
 * Satisfies Functional Requirement 4: "Check course capacity before confirming a registration".
 * 
 * @author Dhanush .K (Reg No: 732925ITR017)
 */
public class CourseCapacityFullException extends CourseEnrollmentException {
    public CourseCapacityFullException(String message) {
        super(message);
    }
}
