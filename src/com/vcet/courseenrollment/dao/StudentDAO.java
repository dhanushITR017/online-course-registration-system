package com.vcet.courseenrollment.dao;

import com.vcet.courseenrollment.model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object for Student entity.
 * 
 * @author Dhanush .K (Reg No: 732925ITR017)
 */
public class StudentDAO {

    public boolean addStudent(Student student) throws SQLException {
        String sql = "INSERT INTO students (student_id, name, department, year_of_study, email) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, student.getStudentId());
            pstmt.setString(2, student.getName());
            pstmt.setString(3, student.getDepartment());
            pstmt.setInt(4, student.getYearOfStudy());
            pstmt.setString(5, student.getEmail());
            return pstmt.executeUpdate() > 0;
        }
    }

    public Optional<Student> getStudentById(String studentId) throws SQLException {
        String sql = "SELECT student_id, name, department, year_of_study, email, created_at FROM students WHERE student_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Student s = new Student(
                            rs.getString("student_id"),
                            rs.getString("name"),
                            rs.getString("department"),
                            rs.getInt("year_of_study"),
                            rs.getString("email")
                    );
                    return Optional.of(s);
                }
            }
        }
        return Optional.empty();
    }

    public List<Student> getAllStudents() throws SQLException {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT student_id, name, department, year_of_study, email FROM students ORDER BY student_id";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Student(
                        rs.getString("student_id"),
                        rs.getString("name"),
                        rs.getString("department"),
                        rs.getInt("year_of_study"),
                        rs.getString("email")
                ));
            }
        }
        return list;
    }

    public boolean existsById(String studentId) throws SQLException {
        String sql = "SELECT 1 FROM students WHERE student_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }
}
