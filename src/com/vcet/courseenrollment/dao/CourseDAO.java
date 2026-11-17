package com.vcet.courseenrollment.dao;

import com.vcet.courseenrollment.model.Course;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object for Course entity.
 * 
 * @author Dhanush .K (Reg No: 732925ITR017)
 */
public class CourseDAO {

    public boolean addCourse(Course course) throws SQLException {
        String sql = "INSERT INTO courses (course_id, course_name, category, duration_weeks, max_capacity) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, course.getCourseId());
            pstmt.setString(2, course.getCourseName());
            pstmt.setString(3, course.getCategory());
            pstmt.setInt(4, course.getDurationWeeks());
            pstmt.setInt(5, course.getMaxCapacity());
            return pstmt.executeUpdate() > 0;
        }
    }

    public Optional<Course> getCourseById(String courseId) throws SQLException {
        String sql = "SELECT course_id, course_name, category, duration_weeks, max_capacity FROM courses WHERE course_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, courseId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Course c = new Course(
                            rs.getString("course_id"),
                            rs.getString("course_name"),
                            rs.getString("category"),
                            rs.getInt("duration_weeks"),
                            rs.getInt("max_capacity")
                    );
                    return Optional.of(c);
                }
            }
        }
        return Optional.empty();
    }

    public List<Course> getAllCourses() throws SQLException {
        List<Course> list = new ArrayList<>();
        String sql = "SELECT course_id, course_name, category, duration_weeks, max_capacity FROM courses ORDER BY course_id";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Course(
                        rs.getString("course_id"),
                        rs.getString("course_name"),
                        rs.getString("category"),
                        rs.getInt("duration_weeks"),
                        rs.getInt("max_capacity")
                ));
            }
        }
        return list;
    }

    public int getCurrentEnrolledCount(String courseId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM enrollments WHERE course_id = ? AND status IN ('Enrolled', 'In-Progress', 'Completed')";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, courseId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    public boolean existsById(String courseId) throws SQLException {
        String sql = "SELECT 1 FROM courses WHERE course_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, courseId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }
}
