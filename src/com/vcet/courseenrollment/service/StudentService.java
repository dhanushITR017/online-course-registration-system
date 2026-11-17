package com.vcet.courseenrollment.service;

import com.vcet.courseenrollment.dao.StudentDAO;
import com.vcet.courseenrollment.exception.RecordNotFoundException;
import com.vcet.courseenrollment.exception.ValidationException;
import com.vcet.courseenrollment.model.Student;

import java.sql.SQLException;
import java.util.List;

/**
 * Service class handling Student business logic and validations.
 * 
 * @author Dhanush .K (Reg No: 732925ITR017)
 */
public class StudentService {
    private final StudentDAO studentDAO;

    public StudentService() {
        this.studentDAO = new StudentDAO();
    }

    public StudentService(StudentDAO studentDAO) {
        this.studentDAO = studentDAO;
    }

    public void registerNewStudent(Student student) throws SQLException {
        validateStudent(student);
        if (studentDAO.existsById(student.getStudentId())) {
            throw new ValidationException("Student with ID '" + student.getStudentId() + "' already exists.");
        }
        studentDAO.addStudent(student);
    }

    public Student getStudent(String studentId) throws SQLException {
        if (studentId == null || studentId.trim().isEmpty()) {
            throw new ValidationException("Student ID cannot be empty.");
        }
        return studentDAO.getStudentById(studentId.trim())
                .orElseThrow(() -> new RecordNotFoundException("Student with ID '" + studentId + "' not found."));
    }

    public List<Student> getAllStudents() throws SQLException {
        return studentDAO.getAllStudents();
    }

    public boolean studentExists(String studentId) throws SQLException {
        return studentDAO.existsById(studentId);
    }

    private void validateStudent(Student s) {
        if (s == null) {
            throw new ValidationException("Student cannot be null.");
        }
        if (s.getStudentId() == null || s.getStudentId().trim().isEmpty()) {
            throw new ValidationException("Student ID is mandatory.");
        }
        if (s.getName() == null || s.getName().trim().isEmpty()) {
            throw new ValidationException("Student Name is mandatory.");
        }
        if (s.getDepartment() == null || s.getDepartment().trim().isEmpty()) {
            throw new ValidationException("Department is mandatory.");
        }
        if (s.getYearOfStudy() < 1 || s.getYearOfStudy() > 4) {
            throw new ValidationException("Year of study must be between 1 and 4.");
        }
        if (s.getEmail() != null && !s.getEmail().trim().isEmpty()) {
            if (!s.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                throw new ValidationException("Invalid email format: " + s.getEmail());
            }
        }
    }
}
