package com.vcet.courseenrollment.service;

import com.vcet.courseenrollment.dao.CourseDAO;
import com.vcet.courseenrollment.dao.EnrollmentDAO;
import com.vcet.courseenrollment.dao.ParticipationDAO;
import com.vcet.courseenrollment.dao.StudentDAO;
import com.vcet.courseenrollment.exception.CourseCapacityFullException;
import com.vcet.courseenrollment.exception.DuplicateEnrollmentException;
import com.vcet.courseenrollment.exception.RecordNotFoundException;
import com.vcet.courseenrollment.exception.ValidationException;
import com.vcet.courseenrollment.model.Course;
import com.vcet.courseenrollment.model.Enrollment;
import com.vcet.courseenrollment.model.ParticipationRecord;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Core business service orchestrating course registrations, capacity checks,
 * duplicate prevention, and participation tracking.
 * 
 * @author Dhanush .K (Reg No: 732925ITR017)
 */
public class EnrollmentService {
    private final EnrollmentDAO enrollmentDAO;
    private final CourseDAO courseDAO;
    private final StudentDAO studentDAO;
    private final ParticipationDAO participationDAO;

    public EnrollmentService() {
        this.enrollmentDAO = new EnrollmentDAO();
        this.courseDAO = new CourseDAO();
        this.studentDAO = new StudentDAO();
        this.participationDAO = new ParticipationDAO();
    }

    public EnrollmentService(EnrollmentDAO enrollmentDAO, CourseDAO courseDAO,
                             StudentDAO studentDAO, ParticipationDAO participationDAO) {
        this.enrollmentDAO = enrollmentDAO;
        this.courseDAO = courseDAO;
        this.studentDAO = studentDAO;
        this.participationDAO = participationDAO;
    }

    /**
     * Registers a student into a course after verifying existence, duplicate prevention, and capacity availability.
     * 
     * @param studentId ID of the student
     * @param courseId ID of the course
     * @param enrollmentDate Date of registration
     * @return Generated enrollment ID
     */
    public synchronized int registerStudent(String studentId, String courseId, LocalDate enrollmentDate) throws SQLException {
        if (studentId == null || studentId.trim().isEmpty()) {
            throw new ValidationException("Student ID cannot be empty.");
        }
        if (courseId == null || courseId.trim().isEmpty()) {
            throw new ValidationException("Course ID cannot be empty.");
        }
        if (enrollmentDate == null) {
            enrollmentDate = LocalDate.now();
        }

        // 1. Verify student exists
        if (!studentDAO.existsById(studentId.trim())) {
            throw new RecordNotFoundException("Student with ID '" + studentId + "' does not exist in records.");
        }

        // 2. Verify course exists
        Course course = courseDAO.getCourseById(courseId.trim())
                .orElseThrow(() -> new RecordNotFoundException("Course with ID '" + courseId + "' does not exist in catalog."));

        // 3. Functional Requirement 3: Prevent duplicate registrations
        if (enrollmentDAO.isAlreadyEnrolled(studentId.trim(), courseId.trim())) {
            throw new DuplicateEnrollmentException(
                    String.format("Registration rejected: Student '%s' is already registered for course '%s' (%s).",
                            studentId, courseId, course.getCourseName())
            );
        }

        // 4. Functional Requirement 4: Check course capacity before confirming registration
        int currentEnrolled = courseDAO.getCurrentEnrolledCount(courseId.trim());
        if (currentEnrolled >= course.getMaxCapacity()) {
            throw new CourseCapacityFullException(
                    String.format("Registration rejected: Course '%s' (%s) has reached maximum capacity (%d/%d seats filled).",
                            courseId, course.getCourseName(), currentEnrolled, course.getMaxCapacity())
            );
        }

        // 5. Confirm registration
        return enrollmentDAO.registerStudent(studentId.trim(), courseId.trim(), enrollmentDate);
    }

    public List<Enrollment> getStudentEnrollments(String studentId) throws SQLException {
        if (!studentDAO.existsById(studentId)) {
            throw new RecordNotFoundException("Student with ID '" + studentId + "' not found.");
        }
        return enrollmentDAO.getEnrollmentsByStudent(studentId);
    }

    public List<Enrollment> getAllEnrollments() throws SQLException {
        return enrollmentDAO.getAllEnrollments();
    }

    public List<Enrollment> getEnrollmentsByStatus(String status) throws SQLException {
        return enrollmentDAO.getAllEnrollments().stream()
                .filter(e -> e.getStatus().equalsIgnoreCase(status))
                .collect(Collectors.toList());
    }

    public boolean updateParticipationAndCompletion(int enrollmentId, double attendancePct, Double score,
                                                    String completionStatus, LocalDate completionDate, String remarks) throws SQLException {
        if (attendancePct < 0.0 || attendancePct > 100.0) {
            throw new ValidationException("Attendance percentage must be between 0.0 and 100.0.");
        }
        if (score != null && (score < 0.0 || score > 100.0)) {
            throw new ValidationException("Assessment score must be between 0.0 and 100.0.");
        }
        if (completionStatus == null || (!completionStatus.equals("Completed") &&
                !completionStatus.equals("Incomplete") && !completionStatus.equals("Withdrawn"))) {
            throw new ValidationException("Completion status must be 'Completed', 'Incomplete', or 'Withdrawn'.");
        }

        if ("Completed".equalsIgnoreCase(completionStatus) && completionDate == null) {
            completionDate = LocalDate.now();
        }

        return participationDAO.updateParticipation(enrollmentId, attendancePct, score, completionStatus, completionDate, remarks);
    }

    public ParticipationRecord getParticipationRecord(int enrollmentId) throws SQLException {
        return participationDAO.getByEnrollmentId(enrollmentId)
                .orElseThrow(() -> new RecordNotFoundException("No participation record found for enrollment ID: " + enrollmentId));
    }

    public List<ParticipationRecord> getAllParticipationRecords() throws SQLException {
        return participationDAO.getAllRecords();
    }
}
