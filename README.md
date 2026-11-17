# Online Course Registration and Student Enrollment Analysis System
### Scenario 17 | Project-Based Assessment

[![Java](https://img.shields.io/badge/Java-21%20LTS-orange.svg)](https://www.oracle.com/java/)
[![SQLite](https://img.shields.io/badge/Database-SQLite%203-blue.svg)](https://www.sqlite.org/)
[![Python](https://img.shields.io/badge/Python-3.10%2B-brightgreen.svg)](https://www.python.org/)
[![Status](https://img.shields.io/badge/Build-Passing-success.svg)]()
[![Institution](https://img.shields.io/badge/Institution-VCET%20Erode-maroon.svg)](https://velalarengg.ac.in/)

---

## 🏛️ Academic Details

- **Institution:** Velalar College of Engineering and Technology (Autonomous), Erode - 638 012
- **Department:** Department of Information Technology
- **Academic Year:** 2026 - 2027 (ODD Semester)
- **Class:** 25IT3A & 25IT3B
- **Assessment Type:** Activity - Project-Based Assessment
- **Assigned Scenario:** **Scenario 17: Online Course Registration and Student Enrollment Analysis System**
- **Author / Primary Submitter:** **Dhanush .K** (Register No: **732925ITR017**)
- **Assigned Team Register Numbers:** **17, 47, 77, 107**

### Course Syllabi Integrated
| Course Code | Course Title | Contribution to Project |
|---|---|---|
| **25ITT31** | **Database Management Systems** | Relational schema design, normalization to BCNF, ACID integrity, and analytical SQL queries. |
| **25ITT32** | **Java Programming** | Object-Oriented design, multi-tier DAO-Service pattern, custom exceptions, JDBC integration, console UI. |
| **25ITT33** | **Software Engineering** | IEEE SRS specification, Use Case / Class / Architectural diagrams, ECP/BVA test design and test cases. |
| **25ITC31** | **Foundations of Data Science** | SQLite data extraction, preprocessing via Pandas/NumPy, statistical aggregation, Matplotlib/Seaborn visualizations. |

---

## 📋 Problem Statement & Objectives

An educational institution offers various certificate courses, skill development programs, and technical training sessions. Managing course details, student registrations, enrollment limits, and completion records manually causes:
1. **Duplicate Registrations:** Students registering multiple times for identical courses.
2. **Capacity Over-subscription:** Difficulty tracking remaining course seats in real time.
3. **Inconsistent Records:** Lack of synchronization between attendance, marks, and completion status.
4. **Lack of Analytical Insights:** Delays in identifying popular courses and measuring departmental participation.

This system provides a full-stack, enterprise-grade solution comprising:
- A robust **Java Core Application** with object-oriented constraint validation and transaction management.
- A **BCNF Normalized Relational Database** (SQLite) enforcing referential and unique integrity.
- A **Python Data Science Pipeline** delivering statistical summaries and 6 multi-dimensional charts.

---

## ⚙️ Functional Requirements Implementation Checklist

- [x] **FR 1: Student Details Management:** Maintain student ID, full name, department, year of study (1 to 4), and institutional email.
- [x] **FR 2: Course Details Management:** Maintain course ID, course name, category (`Certificate Course`, `Skill Development`, `Technical Training`), duration in weeks, and maximum enrollment capacity.
- [x] **FR 3: Course Registration & Duplicate Prevention:** Allow students to enroll in courses while strictly blocking duplicate registrations via application exception `DuplicateEnrollmentException` and database constraint `UNIQUE(student_id, course_id)`.
- [x] **FR 4: Capacity Verification & Seat Tracking:** Real-time computation of available seats ($\text{Capacity} - \text{Active Enrolled}$) before confirming registration. Throws `CourseCapacityFullException` when capacity is full.
- [x] **FR 5: Participation & Completion Tracking:** Track attendance percentage, assessment scores, and completion status (`Enrolled`, `In-Progress`, `Completed`, `Cancelled`).
- [x] **FR 6: Analytical Reports Generation:** Real-time SQL reports for:
  - Course enrollment and available seats
  - Course completion and performance summary
  - Department-wise enrollment distribution
- [x] **FR 7: Python Data Retrieval & Visualizations:** Connect to SQLite via Pandas, preprocess data, and generate publication-quality visual plots.

---

## 🏗️ System Architecture & Database Design

### High-Level Architecture
```
+-------------------------------------------------------------------------+
|                           Presentation Layer                            |
|    - Java Interactive Console CLI (Formatted ANSI Tables & Menus)       |
|    - Python Visualization Graphics (Matplotlib & Seaborn Charts)        |
+------------------------------------+------------------------------------+
                                     |
+------------------------------------v------------------------------------+
|                         Business Logic Layer                            |
|    - StudentService, CourseService, EnrollmentService, ReportService    |
|    - Custom Exception Handling & Transaction Integrity                  |
+------------------------------------+------------------------------------+
                                     |
+------------------------------------v------------------------------------+
|                       Data Access Object (DAO)                          |
|    - StudentDAO, CourseDAO, EnrollmentDAO, ParticipationDAO, ReportDAO  |
|    - Parameterized JDBC PreparedStatements (Zero SQL Injection)         |
+------------------------------------+------------------------------------+
                                     |
+------------------------------------v------------------------------------+
|                     Persistence Layer (SQLite DB)                       |
|    - course_enrollment.db (BCNF Normalized Relational Schema)           |
+-------------------------------------------------------------------------+
```

### Relational Schema (Boyce-Codd Normal Form)
- **`students`** (`student_id` PK, `name`, `department`, `year_of_study`, `email` UNIQUE)
- **`courses`** (`course_id` PK, `course_name`, `category`, `duration_weeks`, `max_capacity`)
- **`enrollments`** (`enrollment_id` PK, `student_id` FK, `course_id` FK, `enrollment_date`, `status`, `UNIQUE(student_id, course_id)`)
- **`participation_records`** (`record_id` PK, `enrollment_id` FK UNIQUE, `attendance_percentage`, `assessment_score`, `completion_status`, `completion_date`, `remarks`)

> Detailed documentation available in [DATABASE_DESIGN.md](file:///d:/project 2/docs/DATABASE_DESIGN.md) and [DESIGN_DIAGRAMS.md](file:///d:/project 2/docs/DESIGN_DIAGRAMS.md).

---

## 📁 Directory Structure

```
d:/project 2/
├── README.md                      # Primary project documentation and execution guide
├── database/                      # SQL schema, seeds, and analytical queries
│   ├── course_enrollment.db       # Active SQLite database file
│   ├── schema.sql                 # Table definitions, constraints, indexes
│   ├── seed_data.sql              # Initial academic test dataset
│   └── queries.sql                # Analytical SQL reporting queries
├── docs/                          # Academic documentation & software engineering artifacts
│   ├── SRS.md                     # Software Requirements Specification (IEEE 830)
│   ├── DESIGN_DIAGRAMS.md         # Use Case, Class, Architecture & Sequence diagrams
│   ├── DATABASE_DESIGN.md         # ER diagram, relational model & BCNF normalization
│   ├── TESTING.md                 # Testing techniques (ECP, BVA) & test execution log
│   └── SAMPLE_OUTPUTS.md          # Sample inputs and CLI execution transcripts
├── lib/                           # Bundled standalone libraries
│   ├── sqlite-jdbc.jar            # SQLite JDBC Driver (v3.45.1.0)
│   ├── slf4j-api.jar              # SLF4J API
│   └── slf4j-simple.jar           # SLF4J Simple Logger
├── python/                        # Data science & visualization pipeline
│   ├── enrollment_analysis.py     # Main Python script for data extraction & charts
│   ├── enrollment_analysis.ipynb  # Interactive Jupyter Notebook
│   ├── requirements.txt           # Python dependencies (pandas, matplotlib, seaborn)
│   └── visualizations/            # Generated high-resolution visualization charts
│       ├── 01_course_popularity.png
│       ├── 02_seat_capacity_utilization.png
│       ├── 03_department_enrollment_donut.png
│       ├── 04_completion_status_breakdown.png
│       ├── 05_category_duration_heatmap.png
│       └── 06_attendance_score_boxplot.png
├── web/                           # Interactive Web Portal (Flask & Bootstrap 5)
│   ├── app.py                     # Flask Web Server & REST API
│   └── templates/
│       └── index.html             # Single-Page Web Dashboard & Architecture Viewer
├── run_website.bat                # 1-Click Windows Launcher for Web Portal
└── src/                           # Java source code
    └── com/vcet/courseenrollment/
        ├── Main.java              # Console entry point & interactive menu
        ├── model/                 # Entity POJOs (Student, Course, Enrollment, etc.)
        ├── dao/                   # Data Access Objects & JDBC Connection Manager
        ├── service/               # Core business logic services
        ├── exception/             # Custom domain exceptions
        └── util/                  # Input validation and tabular printing utilities
```

---

## 🚀 Execution & Setup Guide

### 1. Prerequisites
- **Java:** JDK 17 or higher (tested on JDK 21).
- **Python:** Python 3.9+ with `pip`.
- No database installation is required (SQLite is self-contained and pre-bundled).

---

### 2. Compiling and Running the Java Application

From the project root directory (`d:\project 2`):

#### On Windows (PowerShell / Command Prompt):
```powershell
# Step 1: Compile all Java source files into bin directory
$sources = (Get-ChildItem -Recurse -Filter *.java src).FullName
javac -cp "lib/*" -d bin $sources

# Step 2: Run the Java Interactive Application
java -cp "bin;lib/*" com.vcet.courseenrollment.Main
```

#### On Linux / macOS:
```bash
# Compile
javac -cp "lib/*" -d bin $(find src -name "*.java")

# Run
java -cp "bin:lib/*" com.vcet.courseenrollment.Main
```

---

### 3. Running the Python Analytics Pipeline

#### Install Python Dependencies:
```bash
pip install -r python/requirements.txt
```

#### Execute the Data Science Script:
```bash
python python/enrollment_analysis.py
```
This script will:
1. Connect to `database/course_enrollment.db`.
2. Extract all students, courses, enrollments, and participation data into Pandas DataFrames.
3. Clean and impute missing assessment scores.
4. Print statistical KPIs in the terminal.
5. Generate and save **6 high-resolution charts** in `python/visualizations/`.

#### Interactive Jupyter Notebook:
You can also launch Jupyter Notebook:
```bash
jupyter notebook python/enrollment_analysis.ipynb
```

---

### 4. Running the Interactive Web Portal & Dashboard

You can run the web dashboard with a single command:

#### Option A: 1-Click Windows Batch Script
Simply double-click `run_website.bat` or run:
```powershell
.\run_website.bat
```

#### Option B: Python Command
```powershell
python web/app.py
```

Then open your browser and navigate to:
👉 **[http://127.0.0.1:5000](http://127.0.0.1:5000)**

**Web Portal Features:**
- 📊 **Live Dashboard Overview:** Real-time KPI counters (Students, Courses, Active Registrations, Completed, Attendance %, Average Marks).
- 📝 **Course Registration Portal:** Real-time duplicate registration protection & seat capacity validation banner.
- 📚 **Course Catalog:** Live available seats status tags (`AVAILABLE`, `FILLING FAST`, `FULL`) and capacity progress meters.
- 👥 **Student Directory:** Searchable student list with department filters and Add Student modal.
- 🎯 **Participation & Performance Tracker:** Update attendance percentages and assessment marks with immediate database synchronization.
- 📈 **Analytical Reports:** Three live analytical reports with utilization and completion percentages.
- 🖼️ **Python Visualizations Gallery:** Embedded high-resolution plots with one-click re-execution of the Python analytics pipeline.
- 📑 **Architecture & Testing Viewer:** Architectural layers, BCNF normalization documentation, and the 13/13 passing software test cases.

---

## 📊 Visualizations Overview

| Figure | Chart Type | Analytical Insight |
|---|---|---|
| **01** | Horizontal Bar Chart | **Course Popularity Ranking:** Shows total enrollments per course offering. |
| **02** | Grouped Bar Chart | **Seat Capacity Utilization:** Compares Maximum Intake Capacity against Active Registrations. |
| **03** | Donut Chart | **Department Distribution:** Visualizes student enrollment share across IT, CSE, AIDS, ECE, MECH. |
| **04** | Count Plot | **Completion Status Breakdown:** Displays completion ratios across Certificate, Skill, and Technical tracks. |
| **05** | Heatmap | **Category vs Duration Matrix:** Density of registrations based on course length (weeks). |
| **06** | Box & Whisker Plot | **Assessment Score Dispersion:** Distribution, median, and variance of grades across categories. |

---

## 🧪 Testing Summary

- **Testing Methodology:** Black-Box (Equivalence Partitioning, Boundary Value Analysis) & White-Box (Branch & Statement Coverage).
- **Key Test Cases:**
  - Duplicate Registration Interception (`DuplicateEnrollmentException`) - **PASS**
  - Course Capacity Limit Enforcement (`CourseCapacityFullException`) - **PASS**
  - Attendance Range Validation ($0-100\%$) (`ValidationException`) - **PASS**
  - Foreign Key Cascade on Record Updates - **PASS**
- **Test Pass Rate:** **100% (13/13 Test Cases Passed)**.
- Full testing details documented in [TESTING.md](file:///d:/project 2/docs/TESTING.md).

---

## 📦 GitHub Submission Deliverables Checklist

- [x] **1. SRS, Design Diagrams, and Testing Documents:**
  - `docs/SRS.md` (IEEE Std 830 compliant SRS)
  - `docs/DESIGN_DIAGRAMS.md` (Use Case, Class, Architecture, Sequence diagrams)
  - `docs/TESTING.md` (Testing strategies, ECP/BVA, 13 detailed test cases)
- [x] **2. Java Source Code, Sample Inputs, and Outputs:**
  - `src/` (Clean layered architecture with models, DAOs, services, exceptions)
  - `docs/SAMPLE_OUTPUTS.md` (Step-by-step CLI execution logs)
- [x] **3. ER Diagram, Relational Model, SQL Scripts, and Normalization Details:**
  - `docs/DATABASE_DESIGN.md` (ER diagram, schema specifications, BCNF normalization proof)
  - `database/schema.sql`, `database/seed_data.sql`, `database/queries.sql`
- [x] **4. Python Code for Database Retrieval, Preprocessing, and Visualization:**
  - `python/enrollment_analysis.py` & `python/enrollment_analysis.ipynb`
  - `python/visualizations/` (6 generated PNG figures)
- [x] **5. Comprehensive README.md File:**
  - Complete execution instructions, academic details, and project breakdown.

---
*Developed for Project-Based Assessment, Department of Information Technology, Velalar College of Engineering and Technology, Erode.*
