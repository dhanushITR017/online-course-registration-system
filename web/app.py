import os
import sqlite3
import subprocess
import sys
from datetime import date
from flask import Flask, render_template, request, jsonify, send_from_directory

app = Flask(__name__, template_folder='templates', static_folder='static')

BASE_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
DB_PATH = os.path.join(BASE_DIR, 'database', 'course_enrollment.db')
VIZ_DIR = os.path.join(BASE_DIR, 'python', 'visualizations')

def get_db_connection():
    conn = sqlite3.connect(DB_PATH)
    conn.row_factory = sqlite3.Row
    conn.execute("PRAGMA foreign_keys = ON;")
    return conn

@app.route('/')
def index():
    return render_template('index.html')

@app.route('/visualizations/<path:filename>')
def serve_visualization(filename):
    return send_from_directory(VIZ_DIR, filename)

@app.route('/api/summary')
def api_summary():
    conn = get_db_connection()
    c = conn.cursor()
    c.execute("SELECT COUNT(*) FROM students")
    total_students = c.fetchone()[0]
    
    c.execute("SELECT COUNT(*) FROM courses")
    total_courses = c.fetchone()[0]
    
    c.execute("SELECT COUNT(*) FROM enrollments WHERE status != 'Cancelled'")
    active_enrollments = c.fetchone()[0]
    
    c.execute("SELECT COUNT(*) FROM participation_records WHERE completion_status = 'Completed'")
    completed_courses = c.fetchone()[0]
    
    c.execute("SELECT ROUND(AVG(attendance_percentage), 2), ROUND(AVG(assessment_score), 2) FROM participation_records")
    row = c.fetchone()
    avg_attendance = row[0] if row[0] is not None else 0.0
    avg_score = row[1] if row[1] is not None else 0.0
    
    conn.close()
    return jsonify({
        "total_students": total_students,
        "total_courses": total_courses,
        "active_enrollments": active_enrollments,
        "completed_courses": completed_courses,
        "avg_attendance": avg_attendance,
        "avg_score": avg_score
    })

@app.route('/api/courses')
def api_courses():
    conn = get_db_connection()
    query = """
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
        ORDER BY c.course_id;
    """
    rows = conn.execute(query).fetchall()
    courses = [dict(r) for r in rows]
    conn.close()
    return jsonify(courses)

@app.route('/api/students')
def api_students():
    conn = get_db_connection()
    query = """
        SELECT 
            s.student_id,
            s.name,
            s.department,
            s.year_of_study,
            s.email,
            COUNT(e.enrollment_id) AS enrolled_count
        FROM students s
        LEFT JOIN enrollments e ON s.student_id = e.student_id AND e.status != 'Cancelled'
        GROUP BY s.student_id, s.name, s.department, s.year_of_study, s.email
        ORDER BY s.student_id;
    """
    rows = conn.execute(query).fetchall()
    students = [dict(r) for r in rows]
    conn.close()
    return jsonify(students)

@app.route('/api/enrollments')
def api_enrollments():
    conn = get_db_connection()
    query = """
        SELECT 
            e.enrollment_id,
            e.student_id,
            s.name AS student_name,
            s.department,
            e.course_id,
            c.course_name,
            c.category,
            e.enrollment_date,
            e.status AS enrollment_status,
            COALESCE(pr.attendance_percentage, 0.0) AS attendance_percentage,
            pr.assessment_score,
            COALESCE(pr.completion_status, 'Incomplete') AS completion_status,
            pr.completion_date,
            pr.remarks
        FROM enrollments e
        JOIN students s ON e.student_id = s.student_id
        JOIN courses c ON e.course_id = c.course_id
        LEFT JOIN participation_records pr ON e.enrollment_id = pr.enrollment_id
        ORDER BY e.enrollment_id DESC;
    """
    rows = conn.execute(query).fetchall()
    enrollments = [dict(r) for r in rows]
    conn.close()
    return jsonify(enrollments)

@app.route('/api/register', methods=['POST'])
def api_register():
    data = request.json or {}
    student_id = (data.get('student_id') or '').strip().upper()
    course_id = (data.get('course_id') or '').strip().upper()
    
    if not student_id or not course_id:
        return jsonify({"success": False, "error": "Both Student ID and Course ID are required."}), 400
        
    conn = get_db_connection()
    c = conn.cursor()
    
    # 1. Verify student exists
    c.execute("SELECT student_id, name FROM students WHERE student_id = ?", (student_id,))
    student = c.fetchone()
    if not student:
        conn.close()
        return jsonify({"success": False, "error": f"Student '{student_id}' does not exist in the database."}), 404
        
    # 2. Verify course exists
    c.execute("SELECT course_id, course_name, max_capacity FROM courses WHERE course_id = ?", (course_id,))
    course = c.fetchone()
    if not course:
        conn.close()
        return jsonify({"success": False, "error": f"Course '{course_id}' does not exist in the catalog."}), 404
        
    # 3. Check for Duplicate Enrollment (Functional Requirement 3)
    c.execute("""
        SELECT enrollment_id, status FROM enrollments 
        WHERE student_id = ? AND course_id = ? AND status != 'Cancelled'
    """, (student_id, course_id))
    existing = c.fetchone()
    if existing:
        conn.close()
        return jsonify({
            "success": False, 
            "error": f"DUPLICATE REGISTRATION BLOCKED: Student '{student_id}' is already enrolled in Course '{course_id}' (Status: {existing['status']}). Duplicate enrollments are prohibited."
        }), 409

    # 4. Check Course Capacity (Functional Requirement 4)
    c.execute("""
        SELECT COUNT(*) FROM enrollments 
        WHERE course_id = ? AND status IN ('Enrolled', 'In-Progress', 'Completed')
    """, (course_id,))
    active_count = c.fetchone()[0]
    max_capacity = course['max_capacity']
    available_seats = max_capacity - active_count
    
    if available_seats <= 0:
        conn.close()
        return jsonify({
            "success": False,
            "error": f"COURSE CAPACITY FULL: Course '{course['course_name']}' ({course_id}) has reached its limit of {max_capacity} seats. Available seats: 0."
        }), 409
        
    # 5. Insert enrollment and participation record atomically
    try:
        enroll_date = date.today().isoformat()
        c.execute("""
            INSERT INTO enrollments (student_id, course_id, enrollment_date, status)
            VALUES (?, ?, ?, 'Enrolled')
        """, (student_id, course_id, enroll_date))
        enrollment_id = c.lastrowid
        
        c.execute("""
            INSERT INTO participation_records (enrollment_id, attendance_percentage, assessment_score, completion_status, remarks)
            VALUES (?, 0.0, NULL, 'Incomplete', 'Initial registration')
        """, (enrollment_id,))
        
        conn.commit()
        conn.close()
        return jsonify({
            "success": True,
            "message": f"Successfully enrolled Student {student_id} in {course['course_name']} ({course_id})!",
            "enrollment_id": enrollment_id,
            "available_seats_left": available_seats - 1
        })
    except Exception as e:
        conn.rollback()
        conn.close()
        return jsonify({"success": False, "error": f"Database transaction failed: {str(e)}"}), 500

@app.route('/api/update-participation', methods=['POST'])
def api_update_participation():
    data = request.json or {}
    enrollment_id = data.get('enrollment_id')
    try:
        attendance = float(data.get('attendance_percentage', 0))
    except (ValueError, TypeError):
        return jsonify({"success": False, "error": "Attendance must be a valid number between 0 and 100."}), 400
        
    score_raw = data.get('assessment_score')
    score = None
    if score_raw not in (None, '', 'null'):
        try:
            score = float(score_raw)
            if not (0 <= score <= 100):
                return jsonify({"success": False, "error": "Assessment score must be between 0 and 100."}), 400
        except ValueError:
            return jsonify({"success": False, "error": "Invalid score value."}), 400
            
    if not (0 <= attendance <= 100):
        return jsonify({"success": False, "error": "Attendance percentage must be between 0 and 100."}), 400
        
    completion_status = data.get('completion_status', 'Incomplete')
    remarks = (data.get('remarks') or '').strip()
    completion_date = date.today().isoformat() if completion_status == 'Completed' else None
    
    conn = get_db_connection()
    c = conn.cursor()
    try:
        c.execute("""
            UPDATE participation_records
            SET attendance_percentage = ?,
                assessment_score = ?,
                completion_status = ?,
                completion_date = ?,
                remarks = ?,
                updated_at = CURRENT_TIMESTAMP
            WHERE enrollment_id = ?
        """, (attendance, score, completion_status, completion_date, remarks, enrollment_id))
        
        parent_status = 'Completed' if completion_status == 'Completed' else 'In-Progress'
        c.execute("UPDATE enrollments SET status = ? WHERE enrollment_id = ?", (parent_status, enrollment_id))
        
        conn.commit()
        conn.close()
        return jsonify({"success": True, "message": "Participation & performance record updated successfully!"})
    except Exception as e:
        conn.rollback()
        conn.close()
        return jsonify({"success": False, "error": str(e)}), 500

@app.route('/api/add-student', methods=['POST'])
def api_add_student():
    data = request.json or {}
    student_id = (data.get('student_id') or '').strip().upper()
    name = (data.get('name') or '').strip()
    department = (data.get('department') or '').strip().upper()
    try:
        year = int(data.get('year_of_study'))
    except (ValueError, TypeError):
        return jsonify({"success": False, "error": "Year of study must be an integer between 1 and 4."}), 400
        
    email = (data.get('email') or '').strip().lower()
    
    if not (1 <= year <= 4):
        return jsonify({"success": False, "error": "Year of study must be between 1 and 4."}), 400
    if not student_id or not name or not department:
        return jsonify({"success": False, "error": "Student ID, Name, and Department are required."}), 400
        
    conn = get_db_connection()
    try:
        conn.execute("""
            INSERT INTO students (student_id, name, department, year_of_study, email)
            VALUES (?, ?, ?, ?, ?)
        """, (student_id, name, department, year, email or None))
        conn.commit()
        conn.close()
        return jsonify({"success": True, "message": f"Student '{name}' ({student_id}) added successfully!"})
    except sqlite3.IntegrityError as ie:
        conn.close()
        return jsonify({"success": False, "error": f"Integrity error: Student ID or Email already exists ({str(ie)})."}), 409

@app.route('/api/add-course', methods=['POST'])
def api_add_course():
    data = request.json or {}
    course_id = (data.get('course_id') or '').strip().upper()
    course_name = (data.get('course_name') or '').strip()
    category = (data.get('category') or '').strip()
    try:
        duration_weeks = int(data.get('duration_weeks'))
        max_capacity = int(data.get('max_capacity'))
    except (ValueError, TypeError):
        return jsonify({"success": False, "error": "Duration and Capacity must be positive numbers."}), 400
        
    valid_categories = ['Certificate Course', 'Skill Development', 'Technical Training']
    if category not in valid_categories:
        return jsonify({"success": False, "error": f"Invalid category. Must be one of: {', '.join(valid_categories)}"}), 400
    if duration_weeks <= 0 or max_capacity <= 0:
        return jsonify({"success": False, "error": "Duration and Capacity must be greater than zero."}), 400
        
    conn = get_db_connection()
    try:
        conn.execute("""
            INSERT INTO courses (course_id, course_name, category, duration_weeks, max_capacity)
            VALUES (?, ?, ?, ?, ?)
        """, (course_id, course_name, category, duration_weeks, max_capacity))
        conn.commit()
        conn.close()
        return jsonify({"success": True, "message": f"Course '{course_name}' ({course_id}) created with capacity of {max_capacity}!"})
    except sqlite3.IntegrityError:
        conn.close()
        return jsonify({"success": False, "error": f"Course ID '{course_id}' already exists."}), 409

@app.route('/api/reports/seats')
def api_report_seats():
    conn = get_db_connection()
    query = """
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
    """
    rows = conn.execute(query).fetchall()
    conn.close()
    return jsonify([dict(r) for r in rows])

@app.route('/api/reports/performance')
def api_report_performance():
    conn = get_db_connection()
    query = """
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
    """
    rows = conn.execute(query).fetchall()
    conn.close()
    return jsonify([dict(r) for r in rows])

@app.route('/api/reports/departments')
def api_report_departments():
    conn = get_db_connection()
    query = """
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
    """
    rows = conn.execute(query).fetchall()
    conn.close()
    return jsonify([dict(r) for r in rows])

@app.route('/api/refresh-visualizations', methods=['POST'])
def api_refresh_visualizations():
    script_path = os.path.join(BASE_DIR, 'python', 'enrollment_analysis.py')
    try:
        result = subprocess.run([sys.executable, script_path], capture_output=True, text=True, check=True)
        return jsonify({"success": True, "message": "Visualizations regenerated successfully!", "output": result.stdout})
    except subprocess.CalledProcessError as err:
        return jsonify({"success": False, "error": err.stderr}), 500

if __name__ == '__main__':
    port = int(os.environ.get('PORT', 5000))
    print(f">> Starting VCET Course Enrollment Web Portal on http://127.0.0.1:{port}")
    app.run(host='0.0.0.0', port=port, debug=False)
