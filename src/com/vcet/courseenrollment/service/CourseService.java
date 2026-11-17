package com.vcet.courseenrollment.service;

import com.vcet.courseenrollment.dao.CourseDAO;
import com.vcet.courseenrollment.exception.RecordNotFoundException;
import com.vcet.courseenrollment.exception.ValidationException;
import com.vcet.courseenrollment.model.Course;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 * Service class handling Course catalog and capacity management.
 * 
 * @author Dhanush .K (Reg No: 732925ITR017)
 */
public class CourseService {
    private final CourseDAO courseDAO;
    private static final List<String> VALID_CATEGORIES = Arrays.asList(
            "Certificate Course", "Skill Development", "Technical Training"
    );

    public CourseService() {
        this.courseDAO = new CourseDAO();
    }

    public CourseService(CourseDAO courseDAO) {
        this.courseDAO = courseDAO;
    }

    public void createCourse(Course course) throws SQLException {
        validateCourse(course);
        if (courseDAO.existsById(course.getCourseId())) {
            throw new ValidationException("Course with ID '" + course.getCourseId() + "' already exists.");
        }
        courseDAO.addCourse(course);
    }

    public Course getCourse(String courseId) throws SQLException {
        if (courseId == null || courseId.trim().isEmpty()) {
            throw new ValidationException("Course ID cannot be empty.");
        }
        return courseDAO.getCourseById(courseId.trim())
                .orElseThrow(() -> new RecordNotFoundException("Course with ID '" + courseId + "' not found."));
    }

    public List<Course> getAllCourses() throws SQLException {
        return courseDAO.getAllCourses();
    }

    public int getAvailableSeats(String courseId) throws SQLException {
        Course course = getCourse(courseId);
        int enrolled = courseDAO.getCurrentEnrolledCount(courseId);
        return Math.max(0, course.getMaxCapacity() - enrolled);
    }

    public int getCurrentEnrolledCount(String courseId) throws SQLException {
        return courseDAO.getCurrentEnrolledCount(courseId);
    }

    private void validateCourse(Course c) {
        if (c == null) {
            throw new ValidationException("Course cannot be null.");
        }
        if (c.getCourseId() == null || c.getCourseId().trim().isEmpty()) {
            throw new ValidationException("Course ID is mandatory.");
        }
        if (c.getCourseName() == null || c.getCourseName().trim().isEmpty()) {
            throw new ValidationException("Course Name is mandatory.");
        }
        if (c.getCategory() == null || !VALID_CATEGORIES.contains(c.getCategory().trim())) {
            throw new ValidationException("Category must be one of: " + VALID_CATEGORIES);
        }
        if (c.getDurationWeeks() <= 0) {
            throw new ValidationException("Duration must be greater than 0 weeks.");
        }
        if (c.getMaxCapacity() <= 0) {
            throw new ValidationException("Maximum capacity must be greater than 0.");
        }
    }
}
