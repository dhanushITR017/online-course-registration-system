package com.vcet.courseenrollment.dao;

import com.vcet.courseenrollment.model.CourseReport;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Data Access Object for generating analytical reports.
 * Satisfies Functional Requirement 6: "Generate reports on course enrollment, available seats, and student course completion".
 * 
 * @author Dhanush .K (Reg No: 732925ITR017)
 */
public class ReportDAO {

    public List<CourseReport> getCourseEnrollmentAndSeatReport() throws SQLException {
        List<CourseReport> reports = new ArrayList<>();
        String sql = "SELECT " +
                "    c.course_id, " +
                "    c.course_name, " +
                "    c.category, " +
                "    c.duration_weeks, " +
                "    c.max_capacity, " +
                "    COUNT(CASE WHEN e.status IN ('Enrolled', 'In-Progress', 'Completed') THEN e.enrollment_id END) AS current_enrolled, " +
                "    (c.max_capacity - COUNT(CASE WHEN e.status IN ('Enrolled', 'In-Progress', 'Completed') THEN e.enrollment_id END)) AS available_seats " +
                "FROM courses c " +
                "LEFT JOIN enrollments e ON c.course_id = e.course_id " +
                "GROUP BY c.course_id, c.course_name, c.category, c.duration_weeks, c.max_capacity " +
                "ORDER BY current_enrolled DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String id = rs.getString("course_id");
                String name = rs.getString("course_name");
                String category = rs.getString("category");
                int duration = rs.getInt("duration_weeks");
                int maxCap = rs.getInt("max_capacity");
                int enrolled = rs.getInt("current_enrolled");
                int available = rs.getInt("available_seats");
                double utilization = maxCap > 0 ? (enrolled * 100.0) / maxCap : 0.0;

                String status = "AVAILABLE";
                if (available <= 0) {
                    status = "FULL";
                } else if (available <= 2) {
                    status = "FILLING FAST";
                }

                reports.add(new CourseReport(id, name, category, duration, maxCap, enrolled, available,
                        Math.round(utilization * 100.0) / 100.0, status));
            }
        }
        return reports;
    }

    public List<Map<String, Object>> getCourseCompletionSummary() throws SQLException {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = "SELECT " +
                "    c.course_id, " +
                "    c.course_name, " +
                "    COUNT(e.enrollment_id) AS total_enrolled, " +
                "    COUNT(CASE WHEN pr.completion_status = 'Completed' THEN 1 END) AS total_completed, " +
                "    ROUND((COUNT(CASE WHEN pr.completion_status = 'Completed' THEN 1 END) * 100.0) / NULLIF(COUNT(e.enrollment_id), 0), 2) AS completion_rate, " +
                "    ROUND(AVG(pr.attendance_percentage), 2) AS avg_attendance, " +
                "    ROUND(AVG(pr.assessment_score), 2) AS avg_score " +
                "FROM courses c " +
                "JOIN enrollments e ON c.course_id = e.course_id " +
                "LEFT JOIN participation_records pr ON e.enrollment_id = pr.enrollment_id " +
                "GROUP BY c.course_id, c.course_name " +
                "ORDER BY total_completed DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("course_id", rs.getString("course_id"));
                map.put("course_name", rs.getString("course_name"));
                map.put("total_enrolled", rs.getInt("total_enrolled"));
                map.put("total_completed", rs.getInt("total_completed"));
                map.put("completion_rate", rs.getDouble("completion_rate"));
                map.put("avg_attendance", rs.getDouble("avg_attendance"));
                map.put("avg_score", rs.getDouble("avg_score"));
                list.add(map);
            }
        }
        return list;
    }

    public List<Map<String, Object>> getDepartmentEnrollmentSummary() throws SQLException {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = "SELECT " +
                "    s.department, " +
                "    COUNT(DISTINCT s.student_id) AS total_students, " +
                "    COUNT(e.enrollment_id) AS total_enrollments, " +
                "    COUNT(CASE WHEN pr.completion_status = 'Completed' THEN 1 END) AS total_completions " +
                "FROM students s " +
                "LEFT JOIN enrollments e ON s.student_id = e.student_id " +
                "LEFT JOIN participation_records pr ON e.enrollment_id = pr.enrollment_id " +
                "GROUP BY s.department " +
                "ORDER BY total_enrollments DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("department", rs.getString("department"));
                map.put("total_students", rs.getInt("total_students"));
                map.put("total_enrollments", rs.getInt("total_enrollments"));
                map.put("total_completions", rs.getInt("total_completions"));
                list.add(map);
            }
        }
        return list;
    }
}
