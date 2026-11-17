-- =============================================================================
-- Seed Data: Online Course Registration and Student Enrollment Analysis System
-- Realistic sample records representing students, courses, enrollments, and participation
-- =============================================================================

-- Insert Students (including assigned register numbers: 17, 47, 77, 107)
INSERT OR IGNORE INTO students (student_id, name, department, year_of_study, email) VALUES
('732925ITR017', 'Dhanush K', 'Information Technology', 3, 'dhanush.k@vcet.ac.in'),
('732925ITR047', 'Kavya S', 'Information Technology', 3, 'kavya.s@vcet.ac.in'),
('732925ITR077', 'Pradeep M', 'Information Technology', 3, 'pradeep.m@vcet.ac.in'),
('732925ITR107', 'Vignesh R', 'Information Technology', 3, 'vignesh.r@vcet.ac.in'),
('732925CSR005', 'Aravind Swamy', 'Computer Science and Engineering', 2, 'aravind.s@vcet.ac.in'),
('732925CSR032', 'Harini B', 'Computer Science and Engineering', 3, 'harini.b@vcet.ac.in'),
('732925CSR089', 'Sanjay Kumar', 'Computer Science and Engineering', 4, 'sanjay.k@vcet.ac.in'),
('732925ECR012', 'Ananya G', 'Electronics and Communication Engineering', 2, 'ananya.g@vcet.ac.in'),
('732925ECR054', 'Manoj V', 'Electronics and Communication Engineering', 3, 'manoj.v@vcet.ac.in'),
('732925ECR081', 'Sneha P', 'Electronics and Communication Engineering', 4, 'sneha.p@vcet.ac.in'),
('732925ADR015', 'Deepak N', 'Artificial Intelligence and Data Science', 2, 'deepak.n@vcet.ac.in'),
('732925ADR042', 'Nithya S', 'Artificial Intelligence and Data Science', 3, 'nithya.s@vcet.ac.in'),
('732925MER021', 'Karthik T', 'Mechanical Engineering', 3, 'karthik.t@vcet.ac.in'),
('732925MER058', 'Rahul D', 'Mechanical Engineering', 2, 'rahul.d@vcet.ac.in'),
('732925ITR008', 'Balaji E', 'Information Technology', 2, 'balaji.e@vcet.ac.in'),
('732925ITR029', 'Gokul C', 'Information Technology', 3, 'gokul.c@vcet.ac.in');

-- Insert Courses across three categories: Certificate Course, Skill Development, Technical Training
INSERT OR IGNORE INTO courses (course_id, course_name, category, duration_weeks, max_capacity) VALUES
('CRS-101', 'Full Stack Web Development with React & Spring Boot', 'Technical Training', 8, 5),
('CRS-102', 'Applied Machine Learning and Predictive Analytics', 'Certificate Course', 10, 6),
('CRS-103', 'Cloud Computing & DevOps using AWS & Docker', 'Technical Training', 6, 5),
('CRS-104', 'Professional Communication & Corporate Soft Skills', 'Skill Development', 4, 8),
('CRS-105', 'Cybersecurity Fundamentals and Ethical Hacking', 'Certificate Course', 8, 4),
('CRS-106', 'Data Structures & Algorithms Mastery in Java', 'Skill Development', 12, 6),
('CRS-107', 'Embedded Systems and IoT with Raspberry Pi', 'Technical Training', 8, 4),
('CRS-108', 'Big Data Engineering with Apache Spark', 'Certificate Course', 6, 5);

-- Insert Enrollments (Unique constraint prevents duplicate student-course pairing)
INSERT OR IGNORE INTO enrollments (enrollment_id, student_id, course_id, enrollment_date, status) VALUES
(1,  '732925ITR017', 'CRS-101', '2026-08-01', 'Completed'),
(2,  '732925ITR017', 'CRS-102', '2026-08-05', 'In-Progress'),
(3,  '732925ITR047', 'CRS-101', '2026-08-02', 'Completed'),
(4,  '732925ITR047', 'CRS-104', '2026-08-03', 'Completed'),
(5,  '732925ITR077', 'CRS-102', '2026-08-06', 'In-Progress'),
(6,  '732925ITR077', 'CRS-103', '2026-08-07', 'Completed'),
(7,  '732925ITR107', 'CRS-101', '2026-08-02', 'Completed'),
(8,  '732925ITR107', 'CRS-105', '2026-08-10', 'In-Progress'),
(9,  '732925CSR005', 'CRS-106', '2026-08-04', 'In-Progress'),
(10, '732925CSR032', 'CRS-101', '2026-08-03', 'Completed'),
(11, '732925CSR032', 'CRS-102', '2026-08-08', 'In-Progress'),
(12, '732925CSR089', 'CRS-103', '2026-08-05', 'Completed'),
(13, '732925ECR012', 'CRS-107', '2026-08-09', 'Completed'),
(14, '732925ECR054', 'CRS-107', '2026-08-09', 'In-Progress'),
(15, '732925ECR081', 'CRS-104', '2026-08-11', 'Completed'),
(16, '732925ADR015', 'CRS-102', '2026-08-06', 'Completed'),
(17, '732925ADR015', 'CRS-108', '2026-08-12', 'In-Progress'),
(18, '732925ADR042', 'CRS-102', '2026-08-07', 'In-Progress'),
(19, '732925ADR042', 'CRS-106', '2026-08-05', 'In-Progress'),
(20, '732925MER021', 'CRS-104', '2026-08-12', 'Completed'),
(21, '732925MER058', 'CRS-104', '2026-08-13', 'Completed'),
(22, '732925ITR008', 'CRS-106', '2026-08-14', 'In-Progress'),
(23, '732925ITR029', 'CRS-103', '2026-08-15', 'Completed'),
(24, '732925ITR017', 'CRS-106', '2026-08-16', 'Enrolled');

-- Insert Participation Records (Attendance, score, completion status)
INSERT OR IGNORE INTO participation_records (record_id, enrollment_id, attendance_percentage, assessment_score, completion_status, completion_date, remarks) VALUES
(1,  1,  94.5, 92.0, 'Completed',   '2026-09-28', 'Outstanding full-stack project implementation'),
(2,  2,  88.0, NULL, 'Incomplete',  NULL,         'Mid-term coursework on schedule'),
(3,  3,  96.0, 95.5, 'Completed',   '2026-09-28', 'High distinction in capstone project'),
(4,  4,  90.0, 88.0, 'Completed',   '2026-08-31', 'Excellent presentation skills demonstrated'),
(5,  5,  82.5, NULL, 'Incomplete',  NULL,         'Model tuning phase underway'),
(6,  6,  91.0, 89.5, 'Completed',   '2026-09-18', 'Successfully configured CI/CD pipeline on AWS'),
(7,  7,  88.5, 84.0, 'Completed',   '2026-09-28', 'Proficient in backend REST APIs'),
(8,  8,  78.0, NULL, 'Incomplete',  NULL,         'Lab exercises in penetration testing in-progress'),
(9,  9,  85.0, NULL, 'Incomplete',  NULL,         'Binary search tree and dynamic programming modules active'),
(10, 10, 92.0, 91.0, 'Completed',   '2026-09-28', 'Very good component design'),
(11, 11, 80.0, NULL, 'Incomplete',  NULL,         'Feature engineering module underway'),
(12, 12, 95.0, 94.0, 'Completed',   '2026-09-18', 'Exceptional container orchestration setup'),
(13, 13, 93.0, 87.5, 'Completed',   '2026-10-04', 'Sensor interface hardware prototype completed'),
(14, 14, 76.5, NULL, 'Incomplete',  NULL,         'Assembling hardware modules'),
(15, 15, 98.0, 96.0, 'Completed',   '2026-08-31', 'Distinction in group discussions and mock interviews'),
(16, 16, 95.5, 93.5, 'Completed',   '2026-10-15', 'Completed deep neural network project'),
(17, 17, 84.0, NULL, 'Incomplete',  NULL,         'Spark streaming module underway'),
(18, 18, 79.0, NULL, 'Incomplete',  NULL,         'Data preprocessing phase in progress'),
(19, 19, 87.0, NULL, 'Incomplete',  NULL,         'Graph algorithms in progress'),
(20, 20, 92.0, 86.0, 'Completed',   '2026-08-31', 'Good verbal and written communication'),
(21, 21, 89.0, 85.0, 'Completed',   '2026-08-31', 'Active participant in workshops'),
(22, 22, 81.0, NULL, 'Incomplete',  NULL,         'Recursion and backtracking modules'),
(23, 23, 90.0, 88.0, 'Completed',   '2026-09-18', 'Dockerized microservice deployed'),
(24, 24, 0.0,  NULL, 'Incomplete',  NULL,         'Freshly enrolled student');
