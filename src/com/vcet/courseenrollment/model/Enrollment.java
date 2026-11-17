package com.vcet.courseenrollment.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Model representing an Enrollment entity.
 * Status values: 'Enrolled', 'In-Progress', 'Completed', 'Cancelled'
 * 
 * @author Dhanush .K (Reg No: 732925ITR017)
 */
public class Enrollment {
    private int enrollmentId;
    private String studentId;
    private String courseId;
    private LocalDate enrollmentDate;
    private String status;
    private LocalDateTime createdAt;

    // Joined details for reporting
    private String studentName;
    private String department;
    private String courseName;
    private String category;

    public Enrollment() {
    }

    public Enrollment(String studentId, String courseId, LocalDate enrollmentDate, String status) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.enrollmentDate = enrollmentDate;
        this.status = status;
    }

    public Enrollment(int enrollmentId, String studentId, String courseId, LocalDate enrollmentDate, String status, LocalDateTime createdAt) {
        this.enrollmentId = enrollmentId;
        this.studentId = studentId;
        this.courseId = courseId;
        this.enrollmentDate = enrollmentDate;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(int enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(LocalDate enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return String.format("Enrollment[ID=%d, Student=%s, Course=%s, Date=%s, Status=%s]",
                enrollmentId, studentId, courseId, enrollmentDate, status);
    }
}
