package com.vcet.courseenrollment.service;

import com.vcet.courseenrollment.dao.ReportDAO;
import com.vcet.courseenrollment.model.CourseReport;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Service for analytical report generation and metrics calculation.
 * Satisfies Requirement 6.
 * 
 * @author Dhanush .K (Reg No: 732925ITR017)
 */
public class ReportService {
    private final ReportDAO reportDAO;

    public ReportService() {
        this.reportDAO = new ReportDAO();
    }

    public ReportService(ReportDAO reportDAO) {
        this.reportDAO = reportDAO;
    }

    public List<CourseReport> getSeatAvailabilityReport() throws SQLException {
        return reportDAO.getCourseEnrollmentAndSeatReport();
    }

    public List<Map<String, Object>> getCourseCompletionReport() throws SQLException {
        return reportDAO.getCourseCompletionSummary();
    }

    public List<Map<String, Object>> getDepartmentDistributionReport() throws SQLException {
        return reportDAO.getDepartmentEnrollmentSummary();
    }
}
