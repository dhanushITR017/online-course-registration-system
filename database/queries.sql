-- =============================================================================
-- Analytical Queries: Online Course Registration and Student Enrollment Analysis
-- Requirement 6: Generate reports on course enrollment, available seats, and student course completion
-- Requirement 7: Data retrieval for analysis and reporting
-- =============================================================================

-- -----------------------------------------------------------------------------
-- Query 1: Course Enrollment Summary & Seat Availability Report
-- Calculates current enrolled count, available seats, and utilization percentage
-- -----------------------------------------------------------------------------
SELECT 
    c.course_id,
    c.course_name,
    c.category,
    c.duration_weeks,
    c.max_capacity,
    COUNT(CASE WHEN e.status IN ('Enrolled', 'In-Progress', 'Completed') THEN e.enrollment_id END) AS current_enrolled,
    (c.max_capacity - COUNT(CASE WHEN e.status IN ('Enrolled', 'In-Progress', 'Completed') THEN e.enrollment_id END)) AS available_seats,
    ROUND(
        (COUNT(CASE WHEN e.status IN ('Enrolled', 'In-Progress', 'Completed') THEN e.enrollment_id END) * 100.0) / c.max_capacity, 
        2
    ) AS seat_utilization_pct,
    CASE 
        WHEN (c.max_capacity - COUNT(CASE WHEN e.status IN ('Enrolled', 'In-Progress', 'Completed') THEN e.enrollment_id END)) <= 0 THEN 'FULL'
        WHEN (c.max_capacity - COUNT(CASE WHEN e.status IN ('Enrolled', 'In-Progress', 'Completed') THEN e.enrollment_id END)) <= 2 THEN 'FILLING FAST'
        ELSE 'AVAILABLE'
    END AS enrollment_status
FROM courses c
LEFT JOIN enrollments e ON c.course_id = e.course_id
GROUP BY c.course_id, c.course_name, c.category, c.duration_weeks, c.max_capacity
ORDER BY current_enrolled DESC;

-- -----------------------------------------------------------------------------
-- Query 2: Course Completion and Student Performance Report
-- Evaluates completion count, completion rate, and average assessment score per course
-- -----------------------------------------------------------------------------
SELECT 
    c.course_id,
    c.course_name,
    COUNT(e.enrollment_id) AS total_enrolled,
    COUNT(CASE WHEN pr.completion_status = 'Completed' THEN 1 END) AS total_completed,
    ROUND(
        (COUNT(CASE WHEN pr.completion_status = 'Completed' THEN 1 END) * 100.0) / NULLIF(COUNT(e.enrollment_id), 0),
        2
    ) AS completion_rate_pct,
    ROUND(AVG(pr.attendance_percentage), 2) AS avg_attendance_pct,
    ROUND(AVG(pr.assessment_score), 2) AS avg_assessment_score
FROM courses c
JOIN enrollments e ON c.course_id = e.course_id
LEFT JOIN participation_records pr ON e.enrollment_id = pr.enrollment_id
GROUP BY c.course_id, c.course_name
ORDER BY total_completed DESC;

-- -----------------------------------------------------------------------------
-- Query 3: Student Registration History & Participation Records
-- Retrieves detailed course enrollment profile for each student
-- -----------------------------------------------------------------------------
SELECT 
    s.student_id,
    s.name AS student_name,
    s.department,
    s.year_of_study,
    c.course_id,
    c.course_name,
    c.category,
    e.enrollment_date,
    e.status AS enrollment_status,
    COALESCE(pr.attendance_percentage, 0.0) AS attendance_pct,
    COALESCE(pr.assessment_score, 0.0) AS assessment_score,
    COALESCE(pr.completion_status, 'N/A') AS completion_status,
    pr.completion_date
FROM students s
JOIN enrollments e ON s.student_id = e.student_id
JOIN courses c ON e.course_id = c.course_id
LEFT JOIN participation_records pr ON e.enrollment_id = pr.enrollment_id
ORDER BY s.student_id, e.enrollment_date;

-- -----------------------------------------------------------------------------
-- Query 4: Department-wise Course Enrollment Distribution
-- Shows how registrations are distributed across engineering departments
-- -----------------------------------------------------------------------------
SELECT 
    s.department,
    COUNT(DISTINCT s.student_id) AS registered_students,
    COUNT(e.enrollment_id) AS total_enrollments,
    COUNT(CASE WHEN pr.completion_status = 'Completed' THEN 1 END) AS total_completions,
    ROUND(
        (COUNT(CASE WHEN pr.completion_status = 'Completed' THEN 1 END) * 100.0) / NULLIF(COUNT(e.enrollment_id), 0),
        2
    ) AS department_completion_rate_pct
FROM students s
LEFT JOIN enrollments e ON s.student_id = e.student_id
LEFT JOIN participation_records pr ON e.enrollment_id = pr.enrollment_id
GROUP BY s.department
ORDER BY total_enrollments DESC;

-- -----------------------------------------------------------------------------
-- Query 5: Course Category Performance Breakdown
-- Compares Certificate Courses vs Skill Development vs Technical Training
-- -----------------------------------------------------------------------------
SELECT 
    c.category,
    COUNT(DISTINCT c.course_id) AS course_count,
    COUNT(e.enrollment_id) AS total_enrollments,
    ROUND(AVG(c.duration_weeks), 1) AS avg_duration_weeks,
    ROUND(AVG(pr.attendance_percentage), 2) AS avg_attendance_pct,
    ROUND(AVG(pr.assessment_score), 2) AS avg_score
FROM courses c
LEFT JOIN enrollments e ON c.course_id = e.course_id
LEFT JOIN participation_records pr ON e.enrollment_id = pr.enrollment_id
GROUP BY c.category
ORDER BY total_enrollments DESC;
