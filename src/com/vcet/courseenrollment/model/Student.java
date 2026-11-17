package com.vcet.courseenrollment.model;

import java.time.LocalDateTime;

/**
 * Model representing a Student entity.
 * 
 * @author Dhanush .K (Reg No: 732925ITR017)
 */
public class Student {
    private String studentId;
    private String name;
    private String department;
    private int yearOfStudy;
    private String email;
    private LocalDateTime createdAt;

    public Student() {
    }

    public Student(String studentId, String name, String department, int yearOfStudy, String email) {
        this.studentId = studentId;
        this.name = name;
        this.department = department;
        this.yearOfStudy = yearOfStudy;
        this.email = email;
    }

    public Student(String studentId, String name, String department, int yearOfStudy, String email, LocalDateTime createdAt) {
        this.studentId = studentId;
        this.name = name;
        this.department = department;
        this.yearOfStudy = yearOfStudy;
        this.email = email;
        this.createdAt = createdAt;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getYearOfStudy() {
        return yearOfStudy;
    }

    public void setYearOfStudy(int yearOfStudy) {
        this.yearOfStudy = yearOfStudy;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return String.format("Student[ID=%s, Name=%s, Dept=%s, Year=%d, Email=%s]",
                studentId, name, department, yearOfStudy, email);
    }
}
