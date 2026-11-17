package com.vcet.courseenrollment.model;

import java.time.LocalDateTime;

/**
 * Model representing a Course entity.
 * Categories: 'Certificate Course', 'Skill Development', 'Technical Training'
 * 
 * @author Dhanush .K (Reg No: 732925ITR017)
 */
public class Course {
    private String courseId;
    private String courseName;
    private String category;
    private int durationWeeks;
    private int maxCapacity;
    private LocalDateTime createdAt;

    public Course() {
    }

    public Course(String courseId, String courseName, String category, int durationWeeks, int maxCapacity) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.category = category;
        this.durationWeeks = durationWeeks;
        this.maxCapacity = maxCapacity;
    }

    public Course(String courseId, String courseName, String category, int durationWeeks, int maxCapacity, LocalDateTime createdAt) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.category = category;
        this.durationWeeks = durationWeeks;
        this.maxCapacity = maxCapacity;
        this.createdAt = createdAt;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return String.format("Course[ID=%s, Name=%s, Category=%s, Duration=%d weeks, Capacity=%d]",
                courseId, courseName, category, durationWeeks, maxCapacity);
    }
}
