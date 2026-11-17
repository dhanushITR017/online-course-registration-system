package com.vcet.courseenrollment;

import com.vcet.courseenrollment.dao.DatabaseInitializer;
import com.vcet.courseenrollment.exception.CourseCapacityFullException;
import com.vcet.courseenrollment.exception.CourseEnrollmentException;
import com.vcet.courseenrollment.exception.DuplicateEnrollmentException;
import com.vcet.courseenrollment.model.Course;
import com.vcet.courseenrollment.model.CourseReport;
import com.vcet.courseenrollment.model.Enrollment;
import com.vcet.courseenrollment.model.ParticipationRecord;
import com.vcet.courseenrollment.model.Student;
import com.vcet.courseenrollment.service.CourseService;
import com.vcet.courseenrollment.service.EnrollmentService;
import com.vcet.courseenrollment.service.ReportService;
import com.vcet.courseenrollment.service.StudentService;
import com.vcet.courseenrollment.util.InputValidator;
import com.vcet.courseenrollment.util.TablePrinter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Main Entry Point for Online Course Registration and Student Enrollment Analysis System.
 * 
 * Institution: Velalar College of Engineering and Technology, Erode - 12
 * Department: Information Technology | Class: 25IT3A
 * Author: Dhanush .K | Register No: 732925ITR017
 * Assigned Register Numbers: 17, 47, 77, 107
 * Scenario 17: Project-Based Assessment
 */
public class Main {
    private static final StudentService studentService = new StudentService();
    private static final CourseService courseService = new CourseService();
    private static final EnrollmentService enrollmentService = new EnrollmentService();
    private static final ReportService reportService = new ReportService();
    private static final Scanner scanner = InputValidator.getScanner();

    public static void main(String[] args) {
        printBanner();
        ensureDatabaseReady();

        boolean running = true;
        while (running) {
            printMainMenu();
            System.out.print("Enter your option [0-7]: ");
            if (!scanner.hasNextLine()) {
                break;
            }
            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
                    case "1":
                        handleCourseRegistration();
                        break;
                    case "2":
                        handleStudentManagement();
                        break;
                    case "3":
                        handleCourseManagement();
                        break;
                    case "4":
                        handleParticipationAndCompletion();
                        break;
                    case "5":
                        handleAnalyticalReports();
                        break;
                    case "6":
                        handleLaunchPythonVisualizations();
                        break;
                    case "7":
                        handleDatabaseReset();
                        break;
                    case "0":
                        running = false;
                        System.out.println("\n>> Thank you for using VCET Course Enrollment System. Goodbye!");
                        break;
                    default:
                        System.out.println(">> Invalid option! Please enter a number between 0 and 7.");
                }
            } catch (Exception e) {
                System.out.println("\n>> [ERROR] " + e.getMessage());
            }
        }
    }

    private static void printBanner() {
        System.out.println("==========================================================================================");
        System.out.println("               VELALAR COLLEGE OF ENGINEERING AND TECHNOLOGY, ERODE - 12                  ");
        System.out.println("                          DEPARTMENT OF INFORMATION TECHNOLOGY                            ");
        System.out.println("                   Activity: Project-Based Assessment | Class: 25IT3A                     ");
        System.out.println("         SCENARIO 17: Online Course Registration and Student Enrollment Analysis System    ");
        System.out.println("               Author: Dhanush .K | Reg No: 732925ITR017 | Team: 17, 47, 77, 107         ");
        System.out.println("==========================================================================================");
    }

    private static void ensureDatabaseReady() {
        try {
            if (!DatabaseInitializer.isDatabaseInitialized()) {
                System.out.println(">> Initializing database tables and loading seed data...");
                DatabaseInitializer.initializeDatabase();
                System.out.println(">> Database initialized successfully with sample records!\n");
            }
        } catch (Exception e) {
            System.err.println(">> Database initialization notice: " + e.getMessage());
        }
    }

    private static void printMainMenu() {
        System.out.println("\n----------------------------- MAIN MENU -----------------------------");
        System.out.println("  [1] Course Registration Portal (Register Student & Check Capacity)");
        System.out.println("  [2] Student Management (Add Student, View All, Search)");
        System.out.println("  [3] Course Catalog Management (Add Course, View Catalog & Seats)");
        System.out.println("  [4] Enrollment & Participation Records (Track & Update Status)");
        System.out.println("  [5] Analytical Reports (Available Seats, Completions, Department Stats)");
        System.out.println("  [6] Run Python Data Analytics & Visualizations (Matplotlib/Seaborn)");
        System.out.println("  [7] Reset / Re-seed Database");
        System.out.println("  [0] Exit");
        System.out.println("---------------------------------------------------------------------");
    }

    // =========================================================================
    // 1. Course Registration (Requirements 3 & 4)
    // =========================================================================
    private static void handleCourseRegistration() {
        System.out.println("\n--- [1] COURSE REGISTRATION PORTAL ---");
        try {
            // First display available courses with current seats
            displayCourseCatalogBrief();

            String studentId = InputValidator.readString("Enter Student ID (e.g., 732925ITR017)", true);
            if (!studentService.studentExists(studentId)) {
                System.out.println(">> Notice: Student ID '" + studentId + "' is not registered yet.");
                System.out.print("Do you want to quickly add this student? (y/n): ");
                if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
                    addNewStudentWithId(studentId);
                } else {
                    System.out.println(">> Registration cancelled.");
                    return;
                }
            } else {
                Student s = studentService.getStudent(studentId);
                System.out.println(">> Verified Student: " + s.getName() + " (" + s.getDepartment() + ", Year " + s.getYearOfStudy() + ")");
            }

            String courseId = InputValidator.readString("Enter Course ID to Register (e.g., CRS-101)", true);

            System.out.println(">> Verifying registration criteria (Duplicate check & Capacity check)...");
            int enrollmentId = enrollmentService.registerStudent(studentId, courseId, LocalDate.now());

            System.out.println("\n=======================================================");
            System.out.println(" [SUCCESS] Registration Confirmed!");
            System.out.println(" Enrollment ID : " + enrollmentId);
            System.out.println(" Student ID    : " + studentId);
            System.out.println(" Course ID     : " + courseId);
            System.out.println(" Date          : " + LocalDate.now());
            System.out.println(" Status        : Enrolled");
            System.out.println("=======================================================");

        } catch (DuplicateEnrollmentException e) {
            System.out.println("\n>> [DUPLICATE REGISTRATION BLOCKED]: " + e.getMessage());
        } catch (CourseCapacityFullException e) {
            System.out.println("\n>> [CAPACITY LIMIT REACHED]: " + e.getMessage());
        } catch (CourseEnrollmentException e) {
            System.out.println("\n>> [VALIDATION REJECTED]: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("\n>> [DATABASE ERROR]: " + e.getMessage());
        }
    }

    // =========================================================================
    // 2. Student Management (Requirement 1)
    // =========================================================================
    private static void handleStudentManagement() {
        System.out.println("\n--- [2] STUDENT MANAGEMENT ---");
        System.out.println("  [1] View All Students");
        System.out.println("  [2] Search Student by ID & View Enrollment History");
        System.out.println("  [3] Register New Student");
        System.out.print("Choose option: ");
        String sub = scanner.nextLine().trim();

        try {
            switch (sub) {
                case "1":
                    List<Student> students = studentService.getAllStudents();
                    String[] headers = {"Student ID", "Full Name", "Department", "Year", "Email"};
                    List<String[]> rows = new ArrayList<>();
                    for (Student s : students) {
                        rows.add(new String[]{
                                s.getStudentId(),
                                s.getName(),
                                s.getDepartment(),
                                String.valueOf(s.getYearOfStudy()),
                                s.getEmail() == null ? "N/A" : s.getEmail()
                        });
                    }
                    TablePrinter.printTable("Registered Students Directory", headers, rows);
                    break;

                case "2":
                    String id = InputValidator.readString("Enter Student ID to Search", true);
                    Student s = studentService.getStudent(id);
                    System.out.println("\n--- Student Profile ---");
                    System.out.println("ID         : " + s.getStudentId());
                    System.out.println("Name       : " + s.getName());
                    System.out.println("Department : " + s.getDepartment());
                    System.out.println("Year       : " + s.getYearOfStudy());
                    System.out.println("Email      : " + s.getEmail());

                    List<Enrollment> enrollments = enrollmentService.getStudentEnrollments(id);
                    String[] eHeaders = {"Enrollment ID", "Course ID", "Course Name", "Category", "Date", "Status"};
                    List<String[]> eRows = new ArrayList<>();
                    for (Enrollment e : enrollments) {
                        eRows.add(new String[]{
                                String.valueOf(e.getEnrollmentId()),
                                e.getCourseId(),
                                e.getCourseName(),
                                e.getCategory(),
                                e.getEnrollmentDate().toString(),
                                e.getStatus()
                        });
                    }
                    TablePrinter.printTable("Enrolled Courses for " + s.getName(), eHeaders, eRows);
                    break;

                case "3":
                    addNewStudentWithId(null);
                    break;

                default:
                    System.out.println(">> Invalid option.");
            }
        } catch (Exception e) {
            System.out.println(">> Error: " + e.getMessage());
        }
    }

    private static void addNewStudentWithId(String prefilledId) throws Exception {
        String studentId = prefilledId != null ? prefilledId : InputValidator.readString("Enter Student ID (e.g. 732925ITR099)", true);
        String name = InputValidator.readString("Enter Full Name", true);
        String[] depts = {
                "Information Technology",
                "Computer Science and Engineering",
                "Electronics and Communication Engineering",
                "Artificial Intelligence and Data Science",
                "Mechanical Engineering"
        };
        String dept = InputValidator.readChoice("Select Department:", depts);
        int year = InputValidator.readInt("Enter Year of Study", 1, 4);
        String email = InputValidator.readString("Enter Email address", false);
        if (email.isEmpty()) {
            email = studentId.toLowerCase() + "@vcet.ac.in";
        }

        Student s = new Student(studentId, name, dept, year, email);
        studentService.registerNewStudent(s);
        System.out.println(">> [SUCCESS] Student registered successfully!");
    }

    // =========================================================================
    // 3. Course Management (Requirement 2)
    // =========================================================================
    private static void handleCourseManagement() {
        System.out.println("\n--- [3] COURSE CATALOG MANAGEMENT ---");
        System.out.println("  [1] View Full Course Catalog with Real-Time Capacity");
        System.out.println("  [2] Add New Course Offering");
        System.out.print("Choose option: ");
        String sub = scanner.nextLine().trim();

        try {
            switch (sub) {
                case "1":
                    displayCourseCatalogDetailed();
                    break;
                case "2":
                    String courseId = InputValidator.readString("Enter Course ID (e.g., CRS-109)", true);
                    String courseName = InputValidator.readString("Enter Course Name", true);
                    String[] categories = {"Certificate Course", "Skill Development", "Technical Training"};
                    String category = InputValidator.readChoice("Select Course Category:", categories);
                    int duration = InputValidator.readInt("Enter Duration in Weeks", 1, 52);
                    int maxCap = InputValidator.readInt("Enter Maximum Enrollment Capacity", 1, 200);

                    Course course = new Course(courseId, courseName, category, duration, maxCap);
                    courseService.createCourse(course);
                    System.out.println(">> [SUCCESS] New course created successfully!");
                    break;
                default:
                    System.out.println(">> Invalid option.");
            }
        } catch (Exception e) {
            System.out.println(">> Error: " + e.getMessage());
        }
    }

    private static void displayCourseCatalogBrief() throws Exception {
        List<CourseReport> reports = reportService.getSeatAvailabilityReport();
        String[] headers = {"Course ID", "Course Name", "Category", "Duration", "Capacity", "Enrolled", "Seats Left", "Status"};
        List<String[]> rows = new ArrayList<>();
        for (CourseReport r : reports) {
            rows.add(new String[]{
                    r.getCourseId(),
                    r.getCourseName(),
                    r.getCategory(),
                    r.getDurationWeeks() + " wks",
                    String.valueOf(r.getMaxCapacity()),
                    String.valueOf(r.getCurrentEnrolled()),
                    String.valueOf(r.getAvailableSeats()),
                    r.getEnrollmentStatus()
            });
        }
        TablePrinter.printTable("Available Courses & Seat Status", headers, rows);
    }

    private static void displayCourseCatalogDetailed() throws Exception {
        List<CourseReport> reports = reportService.getSeatAvailabilityReport();
        String[] headers = {"Course ID", "Course Title", "Category", "Duration", "Max Cap", "Enrolled", "Seats Avail", "Fill %", "Status"};
        List<String[]> rows = new ArrayList<>();
        for (CourseReport r : reports) {
            rows.add(new String[]{
                    r.getCourseId(),
                    r.getCourseName(),
                    r.getCategory(),
                    r.getDurationWeeks() + " wks",
                    String.valueOf(r.getMaxCapacity()),
                    String.valueOf(r.getCurrentEnrolled()),
                    String.valueOf(r.getAvailableSeats()),
                    String.format("%.1f%%", r.getSeatUtilizationPct()),
                    r.getEnrollmentStatus()
            });
        }
        TablePrinter.printTable("Detailed Course Offerings & Seat Availability", headers, rows);
    }

    // =========================================================================
    // 4. Participation & Completion Tracking (Requirement 5)
    // =========================================================================
    private static void handleParticipationAndCompletion() {
        System.out.println("\n--- [4] PARTICIPATION & COMPLETION TRACKING ---");
        System.out.println("  [1] View All Participation Records");
        System.out.println("  [2] Update Attendance, Assessment Score & Mark Completion");
        System.out.print("Choose option: ");
        String sub = scanner.nextLine().trim();

        try {
            switch (sub) {
                case "1":
                    List<ParticipationRecord> list = enrollmentService.getAllParticipationRecords();
                    String[] headers = {"Record ID", "Enroll ID", "Student", "Course", "Attendance", "Score", "Completion", "Date", "Remarks"};
                    List<String[]> rows = new ArrayList<>();
                    for (ParticipationRecord p : list) {
                        rows.add(new String[]{
                                String.valueOf(p.getRecordId()),
                                String.valueOf(p.getEnrollmentId()),
                                p.getStudentName() + " (" + p.getStudentId() + ")",
                                p.getCourseId(),
                                String.format("%.1f%%", p.getAttendancePercentage()),
                                p.getAssessmentScore() != null ? String.format("%.1f", p.getAssessmentScore()) : "Pending",
                                p.getCompletionStatus(),
                                p.getCompletionDate() != null ? p.getCompletionDate().toString() : "-",
                                p.getRemarks() != null ? p.getRemarks() : ""
                        });
                    }
                    TablePrinter.printTable("Student Course Participation & Completion Records", headers, rows);
                    break;

                case "2":
                    int enrollId = InputValidator.readInt("Enter Enrollment ID to update", 1, 100000);
                    ParticipationRecord record = enrollmentService.getParticipationRecord(enrollId);

                    System.out.println("\nUpdating Record for: Student '" + record.getStudentName() + "' in Course '" + record.getCourseName() + "'");
                    System.out.println("Current Attendance : " + record.getAttendancePercentage() + "%");
                    System.out.println("Current Score      : " + (record.getAssessmentScore() != null ? record.getAssessmentScore() : "None"));
                    System.out.println("Current Status     : " + record.getCompletionStatus());

                    double attendance = InputValidator.readDouble("Enter New Attendance Percentage", 0.0, 100.0);
                    
                    System.out.print("Enter Assessment Score (0-100, or press Enter to leave pending): ");
                    String scoreInput = scanner.nextLine().trim();
                    Double score = null;
                    if (!scoreInput.isEmpty()) {
                        score = Double.parseDouble(scoreInput);
                    }

                    String[] statuses = {"Completed", "Incomplete", "Withdrawn"};
                    String completionStatus = InputValidator.readChoice("Select Completion Status:", statuses);
                    
                    LocalDate completionDate = null;
                    if ("Completed".equalsIgnoreCase(completionStatus)) {
                        completionDate = LocalDate.now();
                    }

                    String remarks = InputValidator.readString("Enter Evaluation Remarks / Feedback", false);

                    enrollmentService.updateParticipationAndCompletion(enrollId, attendance, score, completionStatus, completionDate, remarks);
                    System.out.println(">> [SUCCESS] Participation and completion status updated successfully!");
                    break;

                default:
                    System.out.println(">> Invalid option.");
            }
        } catch (Exception e) {
            System.out.println(">> Error: " + e.getMessage());
        }
    }

    // =========================================================================
    // 5. Analytical Reports (Requirement 6)
    // =========================================================================
    private static void handleAnalyticalReports() {
        System.out.println("\n--- [5] ANALYTICAL REPORTS & INSIGHTS ---");
        System.out.println("  [1] Course Enrollment & Seat Availability Report");
        System.out.println("  [2] Course Completion & Academic Performance Report");
        System.out.println("  [3] Department-wise Enrollment Distribution Report");
        System.out.print("Choose option: ");
        String sub = scanner.nextLine().trim();

        try {
            switch (sub) {
                case "1":
                    displayCourseCatalogDetailed();
                    break;

                case "2":
                    List<Map<String, Object>> compList = reportService.getCourseCompletionReport();
                    String[] cHeaders = {"Course ID", "Course Name", "Enrolled", "Completed", "Completion %", "Avg Attendance", "Avg Score"};
                    List<String[]> cRows = new ArrayList<>();
                    for (Map<String, Object> m : compList) {
                        cRows.add(new String[]{
                                (String) m.get("course_id"),
                                (String) m.get("course_name"),
                                String.valueOf(m.get("total_enrolled")),
                                String.valueOf(m.get("total_completed")),
                                String.format("%.2f%%", (Double) m.get("completion_rate")),
                                String.format("%.1f%%", (Double) m.get("avg_attendance")),
                                m.get("avg_score") != null ? String.format("%.1f", (Double) m.get("avg_score")) : "N/A"
                        });
                    }
                    TablePrinter.printTable("Course Completion and Academic Performance Summary", cHeaders, cRows);
                    break;

                case "3":
                    List<Map<String, Object>> deptList = reportService.getDepartmentDistributionReport();
                    String[] dHeaders = {"Department", "Registered Students", "Total Enrollments", "Completions"};
                    List<String[]> dRows = new ArrayList<>();
                    for (Map<String, Object> m : deptList) {
                        dRows.add(new String[]{
                                (String) m.get("department"),
                                String.valueOf(m.get("total_students")),
                                String.valueOf(m.get("total_enrollments")),
                                String.valueOf(m.get("total_completions"))
                        });
                    }
                    TablePrinter.printTable("Department-wise Course Enrollment Distribution", dHeaders, dRows);
                    break;

                default:
                    System.out.println(">> Invalid option.");
            }
        } catch (Exception e) {
            System.out.println(">> Error: " + e.getMessage());
        }
    }

    // =========================================================================
    // 6. Python Visualizations (Requirement 7 & 10)
    // =========================================================================
    private static void handleLaunchPythonVisualizations() {
        System.out.println("\n--- [6] LAUNCH PYTHON DATA PREPROCESSING & VISUALIZATION ---");
        System.out.println(">> Running 'python python/enrollment_analysis.py'...");
        try {
            ProcessBuilder pb = new ProcessBuilder("python", "python/enrollment_analysis.py");
            pb.redirectErrorStream(true);
            Process process = pb.start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("   [Python] " + line);
                }
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("\n>> [SUCCESS] Python analysis completed! Generated visualization charts saved to:");
                System.out.println("   Directory: d:\\project 2\\python\\visualizations\\");
            } else {
                System.out.println("\n>> [WARNING] Python process finished with code " + exitCode);
            }
        } catch (Exception e) {
            System.out.println(">> Error executing Python script: " + e.getMessage());
            System.out.println(">> You can also run it directly in terminal: python python/enrollment_analysis.py");
        }
    }

    // =========================================================================
    // 7. Database Administration
    // =========================================================================
    private static void handleDatabaseReset() {
        System.out.println("\n--- [7] DATABASE RESET & RE-SEED ---");
        System.out.print("Are you sure you want to reset the database and restore default seed data? (yes/no): ");
        String confirm = scanner.nextLine().trim();
        if (confirm.equalsIgnoreCase("yes")) {
            try {
                DatabaseInitializer.resetDatabase();
                System.out.println(">> [SUCCESS] Database reset and re-seeded successfully!");
            } catch (Exception e) {
                System.out.println(">> Error resetting database: " + e.getMessage());
            }
        } else {
            System.out.println(">> Database reset cancelled.");
        }
    }
}
