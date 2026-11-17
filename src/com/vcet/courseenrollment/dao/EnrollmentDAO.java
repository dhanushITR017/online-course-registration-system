package com.vcet.courseenrollment.dao;

import com.vcet.courseenrollment.model.Enrollment;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Enrollment entity.
 * 
 * @author Dhanush .K (Reg No: 732925ITR017)
 */
public class EnrollmentDAO {

    public int registerStudent(String studentId, String courseId, LocalDate enrollmentDate) throws SQLException {
        String insertEnrollment = "INSERT INTO enrollments (student_id, course_id, enrollment_date, status) VALUES (?, ?, ?, 'Enrolled')";
        String insertParticipation = "INSERT INTO participation_records (enrollment_id, attendance_percentage, completion_status, remarks) VALUES (?, 0.0, 'Incomplete', 'Newly enrolled')";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement pstmt = conn.prepareStatement(insertEnrollment, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, studentId);
                pstmt.setString(2, courseId);
                pstmt.setString(3, enrollmentDate.toString());
                pstmt.executeUpdate();

                int enrollmentId = -1;
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        enrollmentId = rs.getInt(1);
                    }
                }

                // Automatically initialize participation record
                try (PreparedStatement partStmt = conn.prepareStatement(insertParticipation)) {
                    partStmt.setInt(1, enrollmentId);
                    partStmt.executeUpdate();
                }

                conn.commit();
                return enrollmentId;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    public boolean isAlreadyEnrolled(String studentId, String courseId) throws SQLException {
        String sql = "SELECT 1 FROM enrollments WHERE student_id = ? AND course_id = ? AND status != 'Cancelled'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentId);
            pstmt.setString(2, courseId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    public List<Enrollment> getEnrollmentsByStudent(String studentId) throws SQLException {
        List<Enrollment> list = new ArrayList<>();
        String sql = "SELECT e.enrollment_id, e.student_id, e.course_id, e.enrollment_date, e.status, " +
                     "s.name AS student_name, s.department, c.course_name, c.category " +
                     "FROM enrollments e " +
                     "JOIN students s ON e.student_id = s.student_id " +
                     "JOIN courses c ON e.course_id = c.course_id " +
                     "WHERE e.student_id = ? ORDER BY e.enrollment_date DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Enrollment e = mapEnrollment(rs);
                    list.add(e);
                }
            }
        }
        return list;
    }

    public List<Enrollment> getAllEnrollments() throws SQLException {
        List<Enrollment> list = new ArrayList<>();
        String sql = "SELECT e.enrollment_id, e.student_id, e.course_id, e.enrollment_date, e.status, " +
                     "s.name AS student_name, s.department, c.course_name, c.category " +
                     "FROM enrollments e " +
                     "JOIN students s ON e.student_id = s.student_id " +
                     "JOIN courses c ON e.course_id = c.course_id " +
                     "ORDER BY e.enrollment_id DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapEnrollment(rs));
            }
        }
        return list;
    }

    public boolean updateStatus(int enrollmentId, String status) throws SQLException {
        String sql = "UPDATE enrollments SET status = ? WHERE enrollment_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, enrollmentId);
            return pstmt.executeUpdate() > 0;
        }
    }

    private Enrollment mapEnrollment(ResultSet rs) throws SQLException {
        Enrollment e = new Enrollment(
                rs.getInt("enrollment_id"),
                rs.getString("student_id"),
                rs.getString("course_id"),
                LocalDate.parse(rs.getString("enrollment_date")),
                rs.getString("status"),
                null
        );
        e.setStudentName(rs.getString("student_name"));
        e.setDepartment(rs.getString("department"));
        e.setCourseName(rs.getString("course_name"));
        e.setCategory(rs.getString("category"));
        return e;
    }
}
