# SAMPLE INPUTS AND SYSTEM OUTPUT EXECUTION LOGS
## Online Course Registration and Student Enrollment Analysis System

---

**Institution:** Velalar College of Engineering and Technology, Erode - 12  
**Department:** Information Technology  
**Academic Year:** 2026-27 (ODD) | **Class:** 25IT3A  
**Scenario:** 17  
**Author / Primary Submitter:** Dhanush .K (**Register No:** 732925ITR017)  
**Assigned Register Numbers:** 17, 47, 77, 107  

---

## 1. System Startup Banner & Main Menu

```
==========================================================================================
               VELALAR COLLEGE OF ENGINEERING AND TECHNOLOGY, ERODE - 12                  
                          DEPARTMENT OF INFORMATION TECHNOLOGY                            
                   Activity: Project-Based Assessment | Class: 25IT3A                     
         SCENARIO 17: Online Course Registration and Student Enrollment Analysis System    
               Author: Dhanush .K | Reg No: 732925ITR017 | Team: 17, 47, 77, 107         
==========================================================================================

----------------------------- MAIN MENU -----------------------------
  [1] Course Registration Portal (Register Student & Check Capacity)
  [2] Student Management (Add Student, View All, Search)
  [3] Course Catalog Management (Add Course, View Catalog & Seats)
  [4] Enrollment & Participation Records (Track & Update Status)
  [5] Analytical Reports (Available Seats, Completions, Department Stats)
  [6] Run Python Data Analytics & Visualizations (Matplotlib/Seaborn)
  [7] Reset / Re-seed Database
  [0] Exit
---------------------------------------------------------------------
```

---

## 2. Sample Execution: Course Catalog & Real-Time Seat Availability

**User Action:** Selects `[3] Course Catalog Management` $\to$ `[2] View Course Catalog & Seat Availability`

**System Output:**
```
+-----------+-----------------------------------+--------------------+----------+----------+----------+-----------+-------------+
| Course ID | Course Name                       | Category           | Duration | Capacity | Enrolled | Available | Status      |
+-----------+-----------------------------------+--------------------+----------+----------+----------+-----------+-------------+
| CS101     | Python for Data Science           | Skill Development  |  6 weeks |        5 |        4 |         1 | FILLING FAST|
| CS102     | Full Stack Java Development       | Technical Training |  8 weeks |        4 |        4 |         0 | FULL        |
| CS103     | Machine Learning Fundamentals     | Technical Training |  6 weeks |        3 |        3 |         0 | FULL        |
| CS104     | Cloud Computing with AWS          | Certificate Course |  4 weeks |        4 |        3 |         1 | FILLING FAST|
| CS105     | Cybersecurity Essentials          | Certificate Course |  4 weeks |        5 |        3 |         2 | FILLING FAST|
| CS106     | Database Systems and SQL Mastery  | Skill Development  |  6 weeks |        6 |        4 |         2 | FILLING FAST|
| CS107     | DevOps & CI/CD Pipeline Automation| Technical Training |  8 weeks |        3 |        2 |         1 | FILLING FAST|
| CS108     | Business Intelligence & Tableau   | Skill Development  |  4 weeks |        5 |        3 |         2 | FILLING FAST|
+-----------+-----------------------------------+--------------------+----------+----------+----------+-----------+-------------+
```

---

## 3. Sample Execution: Course Registration & Duplicate Prevention

### Scenario 3.1: Successful Enrollment
**Input:**
```
Enter Student ID: 732925ITR017
Enter Course ID: CS106
```
**System Output:**
```
>> Checking seat capacity for Course CS106...
>> Seats Available: 2 / 6
>> Validating registration uniqueness for Student 732925ITR017...
>> [SUCCESS] Registration Confirmed!
   Enrollment ID : 27
   Student ID    : 732925ITR017 (Dhanush .K - IT, Year 3)
   Course ID     : CS106 (Database Systems and SQL Mastery)
   Date          : 2026-11-17
   Status        : Enrolled
   Participation : Initialized (Attendance: 0.0%, Status: Incomplete)
```

### Scenario 3.2: Duplicate Enrollment Interception
**Input:**
```
Enter Student ID: 732925ITR017
Enter Course ID: CS106
```
**System Output:**
```
>> [ERROR] DuplicateEnrollmentException: Student '732925ITR017' is already enrolled in Course 'CS106' (Status: Enrolled).
   Duplicate registrations are strictly prohibited by institutional policy.
```

### Scenario 3.3: Capacity Full Rejection
**Input:**
```
Enter Student ID: 732925ITR017
Enter Course ID: CS102
```
**System Output:**
```
>> Checking seat capacity for Course CS102...
>> [ERROR] CourseCapacityFullException: Course 'CS102' (Full Stack Java Development) has reached its maximum enrollment capacity of 4 seats.
   Available Seats: 0. Registration cannot be processed.
```

---

## 4. Sample Execution: Participation and Completion Tracking

**User Action:** Selects `[4] Enrollment & Participation Records` $\to$ `[3] Update Attendance and Assessment Score`

**Input:**
```
Enter Enrollment ID: 27
Enter Attendance Percentage [0.0 - 100.0]: 94.5
Enter Assessment Score [0.0 - 100.0]: 92.0
Enter Completion Status [Completed / Incomplete / Withdrawn]: Completed
Enter Remarks: Outstanding performance in final SQL practical capstone project.
```
**System Output:**
```
>> [SUCCESS] Participation record for Enrollment ID #27 updated successfully!
   Attendance  : 94.5%
   Score       : 92.0 / 100
   Status      : Completed
   Completion  : 2026-11-17
   Remarks     : Outstanding performance in final SQL practical capstone project.
>> Synced parent enrollment status to 'Completed'.
```

---

## 5. Sample Execution: Analytical Reports

### Report 5.1: Course Completion & Performance Summary
```
+-----------+-----------------------------------+----------+-----------+------------+------------+-----------+
| Course ID | Course Name                       | Enrolled | Completed | Comp. Rate | Avg Attend | Avg Score |
+-----------+-----------------------------------+----------+-----------+------------+------------+-----------+
| CS101     | Python for Data Science           |        4 |         3 |     75.00% |     88.25% |     91.33 |
| CS102     | Full Stack Java Development       |        4 |         3 |     75.00% |     86.50% |     89.67 |
| CS103     | Machine Learning Fundamentals     |        3 |         2 |     66.67% |     82.00% |     93.50 |
| CS104     | Cloud Computing with AWS          |        3 |         2 |     66.67% |     84.33% |     88.00 |
| CS105     | Cybersecurity Essentials          |        3 |         1 |     33.33% |     72.00% |     85.00 |
| CS106     | Database Systems and SQL Mastery  |        5 |         2 |     40.00% |     79.20% |     92.00 |
| CS107     | DevOps & CI/CD Pipeline Automation|        2 |         1 |     50.00% |     76.00% |     86.00 |
| CS108     | Business Intelligence & Tableau   |        3 |         1 |     33.33% |     68.33% |     84.00 |
+-----------+-----------------------------------+----------+-----------+------------+------------+-----------+
```

### Report 5.2: Department-wise Enrollment Distribution
```
+-----------------------------------+---------------------+-------------------+-------------------+-------------------+
| Department                        | Registered Students | Total Enrollments | Total Completions | Completion Rate % |
+-----------------------------------+---------------------+-------------------+-------------------+-------------------+
| Information Technology            |                   6 |                10 |                 6 |            60.00% |
| Computer Science and Engineering  |                   4 |                 7 |                 4 |            57.14% |
| Artificial Intelligence & DS      |                   3 |                 5 |                 2 |            40.00% |
| Electronics & Communication Engg  |                   2 |                 3 |                 2 |            66.67% |
| Mechanical Engineering            |                   1 |                 2 |                 1 |            50.00% |
+-----------------------------------+---------------------+-------------------+-------------------+-------------------+
```

---

## 6. Python Analytics & Visualizations Output

**Execution Command:** `python python/enrollment_analysis.py`

**Terminal Output:**
```
========================================================================================
  VCET - Online Course Registration and Student Enrollment Analysis (Python Pipeline)   
  Author: Dhanush .K (732925ITR017) | Class: 25IT3A                                    
========================================================================================
>> Retrieving datasets from SQLite database...
   [Loaded] Students: 16, Courses: 8, Enrollments: 26
>> Preprocessing and cleaning dataset...
   [Cleaned] Missing values handled and derived features created.

================================================================================
                 STUDENT ENROLLMENT STATISTICAL SUMMARY
================================================================================
Total Unique Students Enrolled : 16
Total Course Offerings         : 8
Total Enrollments Processed    : 26
Total Courses Completed        : 13 (50.0%)
Active / In-Progress Students  : 13
Average Attendance Rate        : 77.90%
Average Assessment Score       : 90.00/100 (Max: 96.0, Min: 84.0)
================================================================================

>> Generating data visualizations into: D:\project 2\python\visualizations
   [Saved] 01_course_popularity.png
   [Saved] 02_seat_capacity_utilization.png
   [Saved] 03_department_enrollment_donut.png
   [Saved] 04_completion_status_breakdown.png
   [Saved] 05_category_duration_heatmap.png
   [Saved] 06_attendance_score_boxplot.png
>> All analyses and visualizations completed successfully!
```

### Description of Visualizations Generated:
1. `01_course_popularity.png`: Horizontal bar chart ranking course demand by total student registrations.
2. `02_seat_capacity_utilization.png`: Side-by-side grouped bar chart comparing maximum course intake capacity vs active enrollments.
3. `03_department_enrollment_donut.png`: Donut chart presenting percentage breakdown of enrollments across IT, CSE, AIDS, ECE, and MECH departments.
4. `04_completion_status_breakdown.png`: Count plot analyzing completion status across course categories (`Certificate Course`, `Skill Development`, `Technical Training`).
5. `05_category_duration_heatmap.png`: Two-dimensional heat density matrix correlating course durations (in weeks) with categories.
6. `06_attendance_score_boxplot.png`: Statistical box-and-whisker plot displaying mean, median, IQR, and dispersion of assessment scores across categories.
