-- =============================================================================
-- Database Schema: Online Course Registration and Student Enrollment Analysis System
-- Institution: Velalar College of Engineering and Technology, Erode - 12
-- Department of Information Technology (Class: 25IT3A, Scenario 17)
-- Author: Dhanush .K (Reg No: 732925ITR017)
-- =============================================================================

PRAGMA foreign_keys = ON;

-- -----------------------------------------------------------------------------
-- Table 1: students
-- Stores basic demographic and academic details of students
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS students (
    student_id VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    department VARCHAR(50) NOT NULL,
    year_of_study INTEGER NOT NULL CHECK (year_of_study BETWEEN 1 AND 4),
    email VARCHAR(100) UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- -----------------------------------------------------------------------------
-- Table 2: courses
-- Stores course offerings, categories, durations, and enrollment limits
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS courses (
    course_id VARCHAR(20) PRIMARY KEY,
    course_name VARCHAR(150) NOT NULL,
    category VARCHAR(50) NOT NULL CHECK (category IN ('Certificate Course', 'Skill Development', 'Technical Training')),
    duration_weeks INTEGER NOT NULL CHECK (duration_weeks > 0),
    max_capacity INTEGER NOT NULL CHECK (max_capacity > 0),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- -----------------------------------------------------------------------------
-- Table 3: enrollments
-- Tracks student course registrations, enforcing unique enrollment per course
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS enrollments (
    enrollment_id INTEGER PRIMARY KEY AUTOINCREMENT,
    student_id VARCHAR(20) NOT NULL,
    course_id VARCHAR(20) NOT NULL,
    enrollment_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'Enrolled' CHECK (status IN ('Enrolled', 'In-Progress', 'Completed', 'Cancelled')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE,
    UNIQUE (student_id, course_id) -- Prevents duplicate registration for the same course
);

-- -----------------------------------------------------------------------------
-- Table 4: participation_records
-- Maintains attendance, performance scores, and completion status
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS participation_records (
    record_id INTEGER PRIMARY KEY AUTOINCREMENT,
    enrollment_id INTEGER NOT NULL UNIQUE,
    attendance_percentage REAL DEFAULT 0.0 CHECK (attendance_percentage BETWEEN 0 AND 100),
    assessment_score REAL DEFAULT NULL CHECK (assessment_score IS NULL OR (assessment_score BETWEEN 0 AND 100)),
    completion_status VARCHAR(20) NOT NULL DEFAULT 'Incomplete' CHECK (completion_status IN ('Completed', 'Incomplete', 'Withdrawn')),
    completion_date DATE DEFAULT NULL,
    remarks TEXT,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (enrollment_id) REFERENCES enrollments(enrollment_id) ON DELETE CASCADE
);

-- -----------------------------------------------------------------------------
-- Indexes for Query Performance and Relational Integrity
-- -----------------------------------------------------------------------------
CREATE INDEX IF NOT EXISTS idx_enrollment_student ON enrollments(student_id);
CREATE INDEX IF NOT EXISTS idx_enrollment_course ON enrollments(course_id);
CREATE INDEX IF NOT EXISTS idx_participation_enrollment ON participation_records(enrollment_id);
CREATE INDEX IF NOT EXISTS idx_student_department ON students(department);
CREATE INDEX IF NOT EXISTS idx_course_category ON courses(category);
