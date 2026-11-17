# SOFTWARE TESTING TECHNIQUES AND TEST CASES REPORT
## Online Course Registration and Student Enrollment Analysis System

---

**Institution:** Velalar College of Engineering and Technology, Erode - 12  
**Department:** Information Technology  
**Academic Year:** 2026-27 (ODD) | **Class:** 25IT3A  
**Course Code:** 25ITT33 - Software Engineering  
**Scenario:** 17  
**Author / Primary Submitter:** Dhanush .K (**Register No:** 732925ITR017)  
**Assigned Register Numbers:** 17, 47, 77, 107  

---

## 1. Testing Strategy and Objectives

The objective of the testing phase is to verify and validate that the **Online Course Registration and Student Enrollment Analysis System** satisfies all requirements stated in the Software Requirements Specification (SRS), prevents data inconsistencies, and handles unexpected inputs gracefully.

### 1.1 Verification vs. Validation
- **Verification:** "Are we building the product right?" Ensured via static analysis, code walkthroughs, design pattern validation (DAO and Service separation), and SQL constraint checking.
- **Validation:** "Are we building the right product?" Ensured through dynamic execution of functional test scenarios reflecting real-world student course enrollment and institutional quota monitoring.

### 1.2 Levels of Testing
1. **Unit Testing:**
   - Testing individual methods and business rules within `StudentService`, `CourseService`, `EnrollmentService`, and `InputValidator`.
   - Verifying regex patterns, range checks (attendance $0-100\%$, year $1-4$), and calculated fields.
2. **Integration Testing:**
   - Validating interactions between `EnrollmentService`, `EnrollmentDAO`, and the underlying SQLite database.
   - Verifying foreign key cascade behavior and atomic transaction rollbacks upon exceptions.
3. **System Testing:**
   - Validating end-to-end execution through the Java interactive CLI menu, database persistence, and Python visualization script generation.

---

## 2. Test Design Techniques

### 2.1 Black-Box Testing Techniques

#### A. Equivalence Class Partitioning (ECP)
Equivalence Partitioning divides the input domain into classes of valid and invalid values:

| Input Field | Valid Equivalence Class | Invalid Equivalence Class |
|---|---|---|
| **Student ID** | Format: `7329` + 2 digits + `IT` + 1 letter + 3 digits (e.g., `732925ITR017`) | Empty string, letters only, symbols, wrong length (`IT001`, `ABC`) |
| **Year of Study** | Integer $\in [1, 4]$ | Integer $< 1$ (e.g. `0`, `-1`), Integer $> 4$ (e.g. `5`, `6`), non-numeric (`"Third"`) |
| **Course Category** | Exactly `'Certificate Course'`, `'Skill Development'`, `'Technical Training'` | Arbitrary categories like `'Hobby'`, `'Elective'`, empty string |
| **Max Capacity** | Positive integer $\ge 1$ | Zero (`0`), negative numbers (`-5`), non-integers |
| **Attendance %** | Real number $\in [0.0, 100.0]$ | Negative (`-10.5`), over-range (`105.0`), non-numeric |
| **Assessment Score** | Real number $\in [0.0, 100.0]$ or `NULL` | Negative (`-1.0`), exceeding 100 (`120.0`) |

#### B. Boundary Value Analysis (BVA)
Boundary Value Analysis targets edge cases where software failure is statistically most probable:

| Parameter | Minimum Boundary | Nominal Value | Maximum Boundary | Invalid Boundary Values |
|---|---|---|---|---|
| **Year of Study** | `1` | `2`, `3` | `4` | `0` (Just below min), `5` (Just above max) |
| **Available Seats** | `1` seat remaining | Multi-seat | Full (`0` seats) | Negative seats (attempt to oversubscribe) |
| **Attendance %** | `0.0%` | `75.5%` | `100.0%` | `-0.1%`, `100.1%` |
| **Assessment Score**| `0.0` | `85.0` | `100.0` | `-0.5`, `100.5` |

---

### 2.2 White-Box Testing Techniques

1. **Statement Coverage:** Ensuring every line of code across services, DAOs, and exception handlers is executed at least once.
2. **Branch/Decision Coverage:** Evaluating all conditional branches (`if (availableSeats <= 0)`, `if (isAlreadyEnrolled)`, `try ... catch`).
3. **Exception Handling Path Testing:** Explicitly triggering `DuplicateEnrollmentException`, `CourseCapacityFullException`, `RecordNotFoundException`, and `ValidationException` to verify meaningful error messaging and transaction rollbacks.

---

## 3. Comprehensive Test Cases and Execution Results

| Test ID | Module / Feature | Test Description | Test Inputs | Expected Result | Actual Result | Status |
|:---:|---|---|---|---|---|:---:|
| **TC-01** | Student Management | Register a valid new student profile | ID: `732925ITR099`<br>Name: `M. Sangeetha`<br>Dept: `IT`<br>Year: `3`<br>Email: `sangeetha.m@vcet.edu` | Student record saved successfully in DB | Record stored; Student ID `732925ITR099` queryable | **PASS** |
| **TC-02** | Student Validation | Attempt student creation with invalid year of study | ID: `732925ITR100`<br>Year: `5` | `ValidationException` thrown: Year must be between 1 and 4 | Caught exception: "Year of study must be between 1 and 4." | **PASS** |
| **TC-03** | Course Management | Add a valid course offering | ID: `CS501`<br>Name: `Deep Learning with PyTorch`<br>Category: `Technical Training`<br>Duration: `8`<br>Capacity: `25` | Course created and visible in catalog with 25 available seats | Course created; seat availability shows 25/25 | **PASS** |
| **TC-04** | Course Validation | Reject invalid course category | ID: `CS502`<br>Category: `General Sports` | `ValidationException` thrown: Category not recognized | Rejection: "Category must be one of: Certificate Course, Skill Development, Technical Training." | **PASS** |
| **TC-05** | Enrollment Portal | Enroll student in available course | Student: `732925ITR017`<br>Course: `CS101` (Python for Data Science) | Enrollment confirmed; enrollment record and initial participation record created | Enrollment ID generated; participation record initialized | **PASS** |
| **TC-06** | Duplicate Control | **Prevent duplicate course enrollment for same student** | Student: `732925ITR017`<br>Course: `CS101` | System intercepts duplicate attempt; throws `DuplicateEnrollmentException` | Blocked with: `[ERROR] Duplicate Enrollment: Student 732925ITR017 is already registered for course CS101` | **PASS** |
| **TC-07** | Capacity Check | **Prevent enrollment when course capacity is full** | Course: `CS103` (Max capacity 2, current enrolled 2)<br>Student: `732925ITR077` | System blocks registration; throws `CourseCapacityFullException` | Blocked with: `[ERROR] Capacity Full: Course CS103 has reached its maximum capacity of 2 seats` | **PASS** |
| **TC-08** | Participation | Update attendance percentage and assessment score | Enrollment ID: `1`<br>Attendance: `92.5%`<br>Score: `95.0` | Participation record updated in DB | Attendance and score updated successfully | **PASS** |
| **TC-09** | Completion Status | Mark course as completed | Enrollment ID: `1`<br>Status: `Completed`<br>Completion Date: `2026-11-15` | Status set to `Completed`; enrollment record synchronized | Both participation and enrollment status reflect `Completed` | **PASS** |
| **TC-10** | Validation | Reject out-of-range attendance value | Enrollment ID: `2`<br>Attendance: `115.0%` | System rejects with `ValidationException`: Attendance must be $\le 100\%$ | Blocked with error: "Attendance percentage must be between 0 and 100" | **PASS** |
| **TC-11** | Analytical Report | Generate Available Seats and Utilization Report | Menu Option: `[5] -> [1]` | Formatted table showing course capacity, enrolled count, available seats, utilization %, and status tag | Displays formatted ANSI table with accurate counts and status tags | **PASS** |
| **TC-12** | Analytical Report | Generate Department Distribution Report | Menu Option: `[5] -> [3]` | Grouped count of students and total registrations per engineering department | Correctly displays IT (9), CSE (6), AIDS (4), ECE (3), etc. | **PASS** |
| **TC-13** | Python Analytics | Database extraction and visual chart generation | Run `python python/enrollment_analysis.py` | 6 PNG visual charts generated in `python/visualizations/` without errors | All 6 high-resolution visualization charts generated successfully | **PASS** |

---

## 4. Test Summary and Quality Metrics

- **Total Test Cases Planned:** 13
- **Total Test Cases Executed:** 13
- **Passed:** 13 (100%)
- **Failed:** 0 (0%)
- **Critical Defects:** 0
- **Defect Density:** 0 per KLOC

### Conclusion
The test results demonstrate that the application enforces all institutional constraints, handles edge cases cleanly, prevents data integrity violations, and delivers accurate statistical summaries.
