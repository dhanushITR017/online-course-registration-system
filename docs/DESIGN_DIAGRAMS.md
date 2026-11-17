# SYSTEM DESIGN AND ARCHITECTURAL DIAGRAMS
## Online Course Registration and Student Enrollment Analysis System

---

**Institution:** Velalar College of Engineering and Technology, Erode - 12  
**Department:** Information Technology  
**Academic Year:** 2026-27 (ODD) | **Class:** 25IT3A  
**Assessment:** Activity - Project-Based Assessment  
**Scenario:** 17  
**Author / Primary Submitter:** Dhanush .K (**Register No:** 732925ITR017)  
**Assigned Register Numbers:** 17, 47, 77, 107  
**Relevant Courses:**
- 25ITT33: Software Engineering (Architecture, Use Case, Class Diagrams)
- 25ITT32: Java Programming (OOP, Layered Architecture, Exceptions)
- 25ITT31: Database Management Systems (Relational Model, Persistence)
- 25ITC31: Foundations of Data Science (Data Retrieval & Visualization)

---

## 1. Overall System Architecture

The system follows a multi-tiered, decoupled Layered Architecture conforming to the Separation of Concerns (SoC) principle.

```mermaid
graph TD
    subgraph UI_Layer ["Presentation & Interaction Layer"]
        CLI["Java Interactive CLI\n(ANSI Tables & Menus)"]
        PY_VIZ["Python Visualizations\n(Matplotlib & Seaborn Charts)"]
    end

    subgraph Service_Layer ["Application & Business Logic Layer"]
        SS["StudentService\n(Validations & Profile Search)"]
        CS["CourseService\n(Capacity & Category Constraints)"]
        ES["EnrollmentService\n(Duplicate & Seat Control Logic)"]
        RS["ReportService\n(Aggregations & Analytical Summaries)"]
    end

    subgraph DAO_Layer ["Data Access Object (DAO) Layer"]
        SDAO["StudentDAO"]
        CDAO["CourseDAO"]
        EDAO["EnrollmentDAO"]
        PDAO["ParticipationDAO"]
        RDAO["ReportDAO"]
        DBC["DatabaseConnection\n(Connection Pool & Singleton)"]
    end

    subgraph DB_Layer ["Database & Persistence Layer"]
        SQLITE[("SQLite Database\n(course_enrollment.db)\nACID Transactions & Foreign Keys")]
    end

    CLI --> SS
    CLI --> CS
    CLI --> ES
    CLI --> RS

    SS --> SDAO
    CS --> CDAO
    ES --> EDAO
    ES --> CDAO
    ES --> PDAO
    RS --> RDAO

    SDAO --> DBC
    CDAO --> DBC
    EDAO --> DBC
    PDAO --> DBC
    RDAO --> DBC

    DBC --> SQLITE
    PY_VIZ --> SQLITE
```

### Architectural Layer Responsibilities
1. **Presentation Layer:**
   - **Java CLI (`com.vcet.courseenrollment.Main`):** Provides an interactive, menu-driven command-line interface featuring formatted tabular displays via `TablePrinter`.
   - **Python Analytics Pipeline (`python/enrollment_analysis.py`):** Independently reads the persistence store to generate statistical graphics and summary plots.
2. **Business Logic Layer:**
   - Implements business constraints: duplicate registration blocking, course capacity calculation, student ID validation, and transaction isolation.
3. **Data Access Object (DAO) Layer:**
   - Abstract CRUD operations away from business logic using parameterized `PreparedStatement` instances to guarantee SQL injection safety.
4. **Persistence Layer:**
   - SQLite relational database with strict schema integrity (`PRAGMA foreign_keys = ON;`, `CHECK` constraints, and composite unique indexes).

---

## 2. Use Case Diagram

The Use Case Diagram depicts the system boundary and the interactions between the primary actors (Student, Course Coordinator / Faculty, Academic Administrator, Data Analyst) and system capabilities.

```mermaid
graph LR
    subgraph SystemBoundary ["Online Course Registration & Enrollment System"]
        UC1(["Register for Course"])
        UC2(["View Available Courses & Seats"])
        UC3(["View Student Enrollment History"])
        UC4(["Add / Update Student Profile"])
        UC5(["Add / Manage Course Catalog"])
        UC6(["Update Attendance & Assessment Score"])
        UC7(["Mark Course Completion Status"])
        UC8(["Generate Available Seats Report"])
        UC9(["Generate Completion & Performance Report"])
        UC10(["Generate Department Breakdown"])
        UC11(["Extract Data & Generate Visualizations"])

        UC1 -.->|"«include»"| UC2
        UC1 -.->|"«include»"| UC12(["Check Course Capacity"])
        UC1 -.->|"«include»"| UC13(["Verify Duplicate Registration"])
    end

    Student["👤 Student"] --> UC1
    Student --> UC2
    Student --> UC3

    Faculty["👨‍🏫 Course Coordinator / Faculty"] --> UC6
    Faculty --> UC7
    Faculty --> UC9

    Admin["🏛️ Academic Administrator"] --> UC4
    Admin --> UC5
    Admin --> UC8
    Admin --> UC9
    Admin --> UC10

    Analyst["📊 Data Analyst"] --> UC11
```

### Actor Descriptions and Permissions
- **Student:** Can browse open course offerings, view available seats, enroll in courses, and review personal participation and course completion progress.
- **Course Coordinator / Faculty:** Records student attendance percentages, enters evaluation scores, and updates completion status (`Incomplete` $\to$ `Completed`).
- **Academic Administrator:** Configures course capacities, registers new students, monitors seat saturation (`AVAILABLE`, `FILLING FAST`, `FULL`), and reviews department-level engagement.
- **Data Analyst:** Executes Python pipelines to extract SQLite records, perform descriptive analytics, and render multi-dimensional charts.

---

## 3. Class Diagram

The Class Diagram illustrates the object-oriented structure, class attributes, method signatures, inheritance, and dependency relationships implemented in `com.vcet.courseenrollment`.

```mermaid
classDiagram
    class Student {
        -String studentId
        -String name
        -String department
        -int yearOfStudy
        -String email
        +getStudentId() String
        +getName() String
        +getDepartment() String
        +getYearOfStudy() int
        +getEmail() String
    }

    class Course {
        -String courseId
        -String courseName
        -String category
        -int durationWeeks
        -int maxCapacity
        +getCourseId() String
        +getCourseName() String
        +getCategory() String
        +getDurationWeeks() int
        +getMaxCapacity() int
    }

    class Enrollment {
        -int enrollmentId
        -String studentId
        -String courseId
        -LocalDate enrollmentDate
        -String status
        +getEnrollmentId() int
        +getStudentId() String
        +getCourseId() String
        +getEnrollmentDate() LocalDate
        +getStatus() String
    }

    class ParticipationRecord {
        -int recordId
        -int enrollmentId
        -double attendancePercentage
        -Double assessmentScore
        -String completionStatus
        -LocalDate completionDate
        -String remarks
        +getRecordId() int
        +getEnrollmentId() int
        +getAttendancePercentage() double
        +getAssessmentScore() Double
        +getCompletionStatus() String
        +getCompletionDate() LocalDate
    }

    class CourseReport {
        -String courseId
        -String courseName
        -String category
        -int durationWeeks
        -int maxCapacity
        -int currentEnrolled
        -int availableSeats
        -double seatUtilizationPct
        -String enrollmentStatus
        +getCourseId() String
        +getAvailableSeats() int
        +getSeatUtilizationPct() double
    }

    class CourseEnrollmentException {
        <<Exception>>
        +CourseEnrollmentException(String message)
        +CourseEnrollmentException(String message, Throwable cause)
    }

    class DuplicateEnrollmentException {
        <<Exception>>
        +DuplicateEnrollmentException(String studentId, String courseId)
    }

    class CourseCapacityFullException {
        <<Exception>>
        +CourseCapacityFullException(String courseId, int maxCapacity)
    }

    class ValidationException {
        <<Exception>>
        +ValidationException(String message)
    }

    class RecordNotFoundException {
        <<Exception>>
        +RecordNotFoundException(String message)
    }

    CourseEnrollmentException <|-- DuplicateEnrollmentException
    CourseEnrollmentException <|-- CourseCapacityFullException
    CourseEnrollmentException <|-- ValidationException
    CourseEnrollmentException <|-- RecordNotFoundException

    class EnrollmentService {
        -EnrollmentDAO enrollmentDAO
        -CourseDAO courseDAO
        -ParticipationDAO participationDAO
        +registerStudent(String studentId, String courseId) Enrollment
        +cancelEnrollment(int enrollmentId) boolean
        +getEnrollmentsByStudent(String studentId) List~Enrollment~
    }

    class CourseService {
        -CourseDAO courseDAO
        +addCourse(Course course) boolean
        +getAllCourses() List~Course~
        +getCourseById(String courseId) Course
        +getAvailableSeats(String courseId) int
    }

    class StudentService {
        -StudentDAO studentDAO
        +addStudent(Student student) boolean
        +getAllStudents() List~Student~
        +getStudentById(String studentId) Student
    }

    EnrollmentService --> Enrollment
    EnrollmentService --> CourseCapacityFullException
    EnrollmentService --> DuplicateEnrollmentException
    CourseService --> Course
    StudentService --> Student
    Enrollment "1" *-- "1" ParticipationRecord : tracks
    Student "1" -- "0..*" Enrollment : enrolls
    Course "1" -- "0..*" Enrollment : admits
```

---

## 4. Sequence Diagram: Course Registration Workflow

This diagram shows the sequence of interactions and atomic database operations when a student registers for a course, demonstrating business rule validation, capacity enforcement, and duplicate prevention.

```mermaid
sequenceDiagram
    autonumber
    actor User as Student / Administrator
    participant Main as Main (CLI Presentation)
    participant ES as EnrollmentService
    participant CDAO as CourseDAO
    participant EDAO as EnrollmentDAO
    participant PDAO as ParticipationDAO
    participant DB as SQLite Database

    User->>Main: Request course registration (studentId, courseId)
    Main->>ES: registerStudent(studentId, courseId)
    
    ES->>EDAO: isStudentEnrolledInCourse(studentId, courseId)
    EDAO->>DB: SELECT COUNT(*) FROM enrollments WHERE student_id=? AND course_id=? AND status != 'Cancelled'
    DB-->>EDAO: Return count
    alt Count > 0 (Duplicate Found)
        EDAO-->>ES: true
        ES-->>Main: Throw DuplicateEnrollmentException
        Main-->>User: Display "[ERROR] Student is already enrolled in this course!"
    else Count == 0 (Unique Registration)
        EDAO-->>ES: false
        ES->>CDAO: getAvailableSeats(courseId)
        CDAO->>DB: Query max_capacity - current active enrollments
        DB-->>CDAO: Return availableSeats
        alt availableSeats <= 0 (Capacity Full)
            CDAO-->>ES: 0
            ES-->>Main: Throw CourseCapacityFullException
            Main-->>User: Display "[ERROR] Course capacity is FULL! No seats available."
        else availableSeats > 0 (Seats Available)
            CDAO-->>ES: Available seats count
            Note over ES,DB: Begin Atomic Database Transaction
            ES->>EDAO: insertEnrollment(studentId, courseId, 'Enrolled')
            EDAO->>DB: INSERT INTO enrollments (...) VALUES (...)
            DB-->>EDAO: Generated enrollmentId
            ES->>PDAO: createInitialRecord(enrollmentId)
            PDAO->>DB: INSERT INTO participation_records (enrollment_id, attendance=0, status='Incomplete')
            DB-->>PDAO: OK
            Note over ES,DB: Commit Transaction
            ES-->>Main: Return Enrollment object
            Main-->>User: Display "Registration Successful! Seat Confirmed."
        end
    end
```
