# SOFTWARE REQUIREMENTS SPECIFICATION (SRS)
## Online Course Registration and Student Enrollment Analysis System

---

**Institution:** Velalar College of Engineering and Technology, Erode - 12  
**Department:** Information Technology  
**Academic Year:** 2026-27 (ODD) | **Class:** 25IT3A  
**Assessment:** Activity - Project-Based Assessment  
**Scenario:** 17  
**Author / Primary Submitter:** Dhanush .K (**Register No:** 732925ITR017)  
**Team Register Numbers:** 17, 47, 77, 107  
**Course Mappings:**  
- 25ITT33: Software Engineering  
- 25ITT31: Database Management Systems  
- 25ITT32: Java Programming  
- 25ITC31: Foundations of Data Science  

---

## 1. INTRODUCTION

### 1.1 Purpose
This Software Requirements Specification (SRS) defines the functional and non-functional requirements for the **Online Course Registration and Student Enrollment Analysis System**. The document provides an unambiguous blueprint for system developers, software testers, database designers, and academic evaluators.

### 1.2 Document Conventions
- **IEEE Std 830-1998** standard structure is adopted.
- Functional requirements are identified with prefix **FR-[ID]**.
- Non-functional requirements are identified with prefix **NFR-[ID]**.
- High-priority requirements are marked as **[MANDATORY]**.

### 1.3 Intended Audience and Reading Suggestions
- **Academic Evaluators & Instructors:** Review compliance with Scenario 17 requirements and course syllabi.
- **System Developers:** Reference architectural designs, domain models, and service interfaces.
- **QA Engineers / Testers:** Validate test cases against functional criteria.

### 1.4 Project Scope
The educational institution offers diverse certificate courses, skill development programs, and technical training sessions. Managing student registrations manually leads to:
1. Duplicate registrations for the same student-course pair.
2. Inaccurate tracking of course seat limits and over-subscription.
3. Lack of unified student participation, assessment, and completion records.
4. Absence of data-driven insights on course popularity and departmental demand.

The proposed software solves these challenges by providing:
- An object-oriented Java business logic engine with strict constraint validation.
- A normalized relational database schema (SQLite) preventing duplicate entries via compound unique keys.
- Real-time seat availability and quota enforcement algorithms.
- A Python analytics and visualization pipeline leveraging NumPy, Pandas, Matplotlib, and Seaborn.

---

## 2. OVERALL DESCRIPTION

### 2.1 Product Perspective
The system operates as an integrated multi-tier software solution:
```
+-------------------------------------------------------+
|                   Presentation Layer                  |
|    - Java Interactive CLI with Formatted Tables       |
|    - Python Matplotlib / Seaborn Visualizations       |
+---------------------------+---------------------------+
                            |
+---------------------------v---------------------------+
|                    Business Logic                     |
|    - StudentService & CourseService                   |
|    - EnrollmentService (Duplicate & Capacity Control) |
|    - ReportService                                    |
+---------------------------+---------------------------+
                            |
+---------------------------v---------------------------+
|                Data Access & Persistence              |
|    - JDBC Driver (SQLite)                             |
|    - DAO Layer (StudentDAO, CourseDAO, EnrollmentDAO) |
|    - Relational Storage: course_enrollment.db         |
+-------------------------------------------------------+
```

### 2.2 Product Functions
1. **Student Record Management:** Maintain unique student profiles (ID, name, department, year of study, email).
2. **Course Catalog Management:** Maintain course offerings (ID, title, category, duration, max capacity).
3. **Course Registration with Integrity Controls:**
   - Verify course seat availability prior to booking.
   - Enforce compound uniqueness `(student_id, course_id)` to block duplicate enrollment attempts.
4. **Participation & Completion Tracking:** Track attendance percentage, assessment scores, and completion status (`Enrolled`, `In-Progress`, `Completed`, `Cancelled`).
5. **Analytical Reporting:** Generate real-time reports on seat utilization, completion rates, and department enrollment distributions.
6. **Data Preprocessing & Visualization:** Retrieve database records into Pandas, clean/impute data, and generate publication-quality visual charts.

### 2.3 User Classes and Characteristics
- **Student / Candidate:** Registers for available courses, views enrollment status.
- **Course Coordinator / Faculty:** Updates attendance, enters assessment scores, and marks completion.
- **Academic Administrator:** Manages course quotas, views capacity reports, and evaluates course popularity.
- **Data Analyst:** Executes Python pipelines to extract trends, department participation, and performance metrics.

### 2.4 Operating Environment
- **Operating System:** Windows 10/11, Linux, or macOS.
- **Java Runtime:** Java Development Kit (JDK) 21 LTS or higher.
- **Python Runtime:** Python 3.10+ with Pandas, NumPy, Matplotlib, Seaborn.
- **Database Engine:** SQLite 3 (self-contained, serverless, file-based).

### 2.5 Design and Implementation Constraints
- **Zero Third-Party Database Server Requirement:** SQLite JDBC driver is bundled locally in `lib/` so evaluators require zero database installation.
- **Atomic Operations:** Registration and participation records must be created within an atomic database transaction.
- **Thread Safety:** Capacity checks and enrollment insertions use synchronized blocks to prevent race conditions during concurrent bookings.

---

## 3. SYSTEM FEATURES AND FUNCTIONAL REQUIREMENTS

### 3.1 Functional Requirement 1: Student Details Management
- **FR-1.1:** The system shall store student ID, full name, department, and year of study (1 to 4).
- **FR-1.2:** The system shall validate that `student_id` is unique and formatted according to institution conventions (e.g., `732925ITR017`).
- **FR-1.3:** The system shall allow searching student profiles and listing all active enrollments for a given student.

### 3.2 Functional Requirement 2: Course Details Management
- **FR-2.1:** The system shall maintain course ID, course title, category, duration in weeks, and maximum enrollment capacity.
- **FR-2.2:** Supported course categories shall be restricted to:
  - `Certificate Course`
  - `Skill Development`
  - `Technical Training`
- **FR-2.3:** The system shall enforce that `duration_weeks > 0` and `max_capacity > 0`.

### 3.3 Functional Requirement 3: Course Registration & Duplicate Prevention
- **FR-3.1:** The system shall allow registered students to enroll in available courses.
- **FR-3.2:** If a student attempts to enroll in a course they are already actively registered for, the system **MUST** block the operation and throw a `DuplicateEnrollmentException`.
- **FR-3.3:** The database schema shall enforce a `UNIQUE (student_id, course_id)` table constraint as a second layer of defense.

### 3.4 Functional Requirement 4: Capacity Verification & Seat Management
- **FR-4.1:** The system shall calculate available seats using:
  $$\text{Available Seats} = \text{Max Capacity} - \text{Active Enrolled Count}$$
- **FR-4.2:** If $\text{Available Seats} \le 0$, registration **MUST** be rejected, throwing a `CourseCapacityFullException`.
- **FR-4.3:** The system shall classify seat status into:
  - `AVAILABLE` (more than 2 seats remaining)
  - `FILLING FAST` (1 or 2 seats remaining)
  - `FULL` (0 seats remaining)

### 3.5 Functional Requirement 5: Participation & Completion Tracking
- **FR-5.1:** Upon successful registration, the system shall initialize a corresponding `participation_records` entry with 0% attendance and `Incomplete` status.
- **FR-5.2:** Faculty can update attendance percentage (0.0 to 100.0) and assessment score (0.0 to 100.0).
- **FR-5.3:** When status is updated to `Completed`, the system shall record the completion date and synchronize the parent enrollment status.

### 3.6 Functional Requirement 6: Reporting and Query Processing
- **FR-6.1:** Course Enrollment and Available Seats Report (table showing capacity, enrolled, remaining seats, fill percentage).
- **FR-6.2:** Course Completion and Academic Performance Summary (table showing completion rate %, average attendance, average score).
- **FR-6.3:** Department-wise Enrollment Distribution Report.

### 3.7 Functional Requirement 7: Python Data Retrieval & Visualization
- **FR-7.1:** The system shall connect to SQLite and extract raw tables via Pandas `read_sql_query`.
- **FR-7.2:** Cleanse and preprocess data: handle null scores, compute derived KPIs.
- **FR-7.3:** Generate visual charts saved to disk:
  1. Horizontal Bar Chart: Course popularity.
  2. Grouped Bar Chart: Max capacity vs enrolled seats.
  3. Donut Chart: Department distribution.
  4. Count Plot: Completion status across categories.
  5. Heatmap: Category vs duration enrollment density.
  6. Box Plot: Assessment score distribution.

---

## 4. EXTERNAL INTERFACE REQUIREMENTS

### 4.1 User Interfaces
- An interactive Console Terminal CLI featuring ANSI borders, dynamic padding, and numbered menu selections.
- Clean formatted ASCII tables for listing students, courses, enrollments, and analytical summaries.

### 4.2 Software Interfaces
- **JDBC Driver:** `org.sqlite.JDBC` v3.45.1.0.
- **Python Libraries:** `pandas 3.x`, `matplotlib 3.x`, `seaborn 0.13.x`, `numpy 2.x`.

---

## 5. NON-FUNCTIONAL REQUIREMENTS

### 5.1 Performance Requirements
- Query execution and table rendering for up to 10,000 records within $< 200\text{ ms}$.
- Python chart generation completed within $< 3\text{ seconds}$.

### 5.2 Reliability and Data Integrity
- Referential integrity enforced through foreign keys:
  - `enrollments.student_id -> students.student_id ON DELETE CASCADE`
  - `enrollments.course_id -> courses.course_id ON DELETE CASCADE`
  - `participation_records.enrollment_id -> enrollments.enrollment_id ON DELETE CASCADE`

### 5.3 Maintainability and Extensibility
- Clean Separation of Concerns (SoC) using Model-DAO-Service-Controller/UI layered architecture.
- Modular Python scripts and Jupyter Notebook allowing independent execution.
