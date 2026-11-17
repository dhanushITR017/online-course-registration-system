package com.vcet.courseenrollment.model;

/**
 * DTO / Model for Course Enrollment & Seat Availability Reporting.
 * 
 * @author Dhanush .K (Reg No: 732925ITR017)
 */
public class CourseReport {
    private String courseId;
    private String courseName;
    private String category;
    private int durationWeeks;
    private int maxCapacity;
    private int currentEnrolled;
    private int availableSeats;
    private double seatUtilizationPct;
    private String enrollmentStatus; // 'AVAILABLE', 'FILLING FAST', 'FULL'

    public CourseReport() {
    }

    public CourseReport(String courseId, String courseName, String category, int durationWeeks,
                        int maxCapacity, int currentEnrolled, int availableSeats,
                        double seatUtilizationPct, String enrollmentStatus) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.category = category;
        this.durationWeeks = durationWeeks;
        this.maxCapacity = maxCapacity;
        this.currentEnrolled = currentEnrolled;
        this.availableSeats = availableSeats;
        this.seatUtilizationPct = seatUtilizationPct;
        this.enrollmentStatus = enrollmentStatus;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getDurationWeeks() {
        return durationWeeks;
    }

    public void setDurationWeeks(int durationWeeks) {
        this.durationWeeks = durationWeeks;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public int getCurrentEnrolled() {
        return currentEnrolled;
    }

    public void setCurrentEnrolled(int currentEnrolled) {
        this.currentEnrolled = currentEnrolled;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public double getSeatUtilizationPct() {
        return seatUtilizationPct;
    }

    public void setSeatUtilizationPct(double seatUtilizationPct) {
        this.seatUtilizationPct = seatUtilizationPct;
    }

    public String getEnrollmentStatus() {
        return enrollmentStatus;
    }

    public void setEnrollmentStatus(String enrollmentStatus) {
        this.enrollmentStatus = enrollmentStatus;
    }
}
