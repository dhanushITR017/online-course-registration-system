package com.vcet.courseenrollment.dao;

import com.vcet.courseenrollment.model.ParticipationRecord;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object for ParticipationRecord entity.
 * 
 * @author Dhanush .K (Reg No: 732925ITR017)
 */
public class ParticipationDAO {

    public Optional<ParticipationRecord> getByEnrollmentId(int enrollmentId) throws SQLException {
        String sql = "SELECT pr.record_id, pr.enrollment_id, pr.attendance_percentage, pr.assessment_score, " +
                     "pr.completion_status, pr.completion_date, pr.remarks, " +
                     "e.student_id, s.name AS student_name, e.course_id, c.course_name " +
                     "FROM participation_records pr " +
                     "JOIN enrollments e ON pr.enrollment_id = e.enrollment_id " +
                     "JOIN students s ON e.student_id = s.student_id " +
                     "JOIN courses c ON e.course_id = c.course_id " +
                     "WHERE pr.enrollment_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, enrollmentId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRecord(rs));
                }
            }
        }
        return Optional.empty();
    }

    public List<ParticipationRecord> getAllRecords() throws SQLException {
        List<ParticipationRecord> list = new ArrayList<>();
        String sql = "SELECT pr.record_id, pr.enrollment_id, pr.attendance_percentage, pr.assessment_score, " +
                     "pr.completion_status, pr.completion_date, pr.remarks, " +
                     "e.student_id, s.name AS student_name, e.course_id, c.course_name " +
                     "FROM participation_records pr " +
                     "JOIN enrollments e ON pr.enrollment_id = e.enrollment_id " +
                     "JOIN students s ON e.student_id = s.student_id " +
                     "JOIN courses c ON e.course_id = c.course_id " +
                     "ORDER BY pr.record_id DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRecord(rs));
            }
        }
        return list;
    }

    public boolean updateParticipation(int enrollmentId, double attendancePct, Double score,
                                       String completionStatus, LocalDate completionDate, String remarks) throws SQLException {
        String updatePartSql = "UPDATE participation_records SET " +
                "attendance_percentage = ?, " +
                "assessment_score = ?, " +
                "completion_status = ?, " +
                "completion_date = ?, " +
                "remarks = ?, " +
                "updated_at = CURRENT_TIMESTAMP " +
                "WHERE enrollment_id = ?";
        
        String updateEnrollSql = "UPDATE enrollments SET status = ? WHERE enrollment_id = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                try (PreparedStatement pstmt = conn.prepareStatement(updatePartSql)) {
                    pstmt.setDouble(1, attendancePct);
                    if (score != null) {
                        pstmt.setDouble(2, score);
                    } else {
                        pstmt.setNull(2, Types.DOUBLE);
                    }
                    pstmt.setString(3, completionStatus);
                    if (completionDate != null) {
                        pstmt.setString(4, completionDate.toString());
                    } else {
                        pstmt.setNull(4, Types.VARCHAR);
                    }
                    pstmt.setString(5, remarks);
                    pstmt.setInt(6, enrollmentId);
                    pstmt.executeUpdate();
                }

                // Synchronize status in enrollments table
                String mappedEnrollStatus = "In-Progress";
                if ("Completed".equalsIgnoreCase(completionStatus)) {
                    mappedEnrollStatus = "Completed";
                } else if ("Withdrawn".equalsIgnoreCase(completionStatus)) {
                    mappedEnrollStatus = "Cancelled";
                }

                try (PreparedStatement enrollStmt = conn.prepareStatement(updateEnrollSql)) {
                    enrollStmt.setString(1, mappedEnrollStatus);
                    enrollStmt.setInt(2, enrollmentId);
                    enrollStmt.executeUpdate();
                }

                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    private ParticipationRecord mapRecord(ResultSet rs) throws SQLException {
        ParticipationRecord p = new ParticipationRecord();
        p.setRecordId(rs.getInt("record_id"));
        p.setEnrollmentId(rs.getInt("enrollment_id"));
        p.setAttendancePercentage(rs.getDouble("attendance_percentage"));
        
        double score = rs.getDouble("assessment_score");
        if (!rs.wasNull()) {
            p.setAssessmentScore(score);
        } else {
            p.setAssessmentScore(null);
        }

        p.setCompletionStatus(rs.getString("completion_status"));
        String dateStr = rs.getString("completion_date");
        if (dateStr != null && !dateStr.isEmpty()) {
            p.setCompletionDate(LocalDate.parse(dateStr));
        }

        p.setRemarks(rs.getString("remarks"));
        p.setStudentId(rs.getString("student_id"));
        p.setStudentName(rs.getString("student_name"));
        p.setCourseId(rs.getString("course_id"));
        p.setCourseName(rs.getString("course_name"));
        return p;
    }
}
