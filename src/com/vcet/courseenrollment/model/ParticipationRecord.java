package com.vcet.courseenrollment.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Model representing a Student Participation & Completion Record.
 * 
 * @author Dhanush .K (Reg No: 732925ITR017)
 */
public class ParticipationRecord {
    private int recordId;
    private int enrollmentId;
    private double attendancePercentage;
    private Double assessmentScore; // nullable
    private String completionStatus; // 'Completed', 'Incomplete', 'Withdrawn'
    private LocalDate completionDate; // nullable
    private String remarks;
    private LocalDateTime updatedAt;

    // Joined fields for display
    private String studentId;
    private String studentName;
    private String courseId;
    private String courseName;

    public ParticipationRecord() {
    }

    public ParticipationRecord(int enrollmentId, double attendancePercentage, Double assessmentScore,
                               String completionStatus, LocalDate completionDate, String remarks) {
        this.enrollmentId = enrollmentId;
        this.attendancePercentage = attendancePercentage;
        this.assessmentScore = assessmentScore;
        this.completionStatus = completionStatus;
        this.completionDate = completionDate;
        this.remarks = remarks;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public int getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(int enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public double getAttendancePercentage() {
        return attendancePercentage;
    }

    public void setAttendancePercentage(double attendancePercentage) {
        this.attendancePercentage = attendancePercentage;
    }

    public Double getAssessmentScore() {
        return assessmentScore;
    }

    public void setAssessmentScore(Double assessmentScore) {
        this.assessmentScore = assessmentScore;
    }

    public String getCompletionStatus() {
        return completionStatus;
    }

    public void setCompletionStatus(String completionStatus) {
        this.completionStatus = completionStatus;
    }

    public LocalDate getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(LocalDate completionDate) {
        this.completionDate = completionDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}
