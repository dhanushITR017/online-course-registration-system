"""
========================================================================================
Project: Online Course Registration and Student Enrollment Analysis System
Scenario 17 - Project-Based Assessment
Institution: Velalar College of Engineering and Technology, Erode - 12
Department: Information Technology | Class: 25IT3A
Author: Dhanush .K | Reg No: 732925ITR017 | Team: 17, 47, 77, 107
Course Mapping: 25ITC31 Foundations of Data Science
========================================================================================
"""

import os
import sqlite3
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import seaborn as sns

# Set professional visualization style
sns.set_theme(style="whitegrid", palette="muted")
plt.rcParams['font.sans-serif'] = 'Arial'
plt.rcParams['font.family'] = 'sans-serif'
plt.rcParams['figure.dpi'] = 300

DB_PATH = os.path.join(os.path.dirname(__file__), "..", "database", "course_enrollment.db")
OUTPUT_DIR = os.path.join(os.path.dirname(__file__), "visualizations")
SCHEMA_PATH = os.path.join(os.path.dirname(__file__), "..", "database", "schema.sql")
SEED_PATH = os.path.join(os.path.dirname(__file__), "..", "database", "seed_data.sql")


def ensure_database():
    """Ensure SQLite database and tables exist. If not, initialize with schema and seed data."""
    os.makedirs(os.path.dirname(DB_PATH), exist_ok=True)
    conn = sqlite3.connect(DB_PATH)
    cursor = conn.cursor()
    cursor.execute("SELECT count(*) FROM sqlite_master WHERE type='table' AND name='students'")
    count = cursor.fetchone()[0]

    if count == 0:
        print(">> Database not found or empty. Initializing from SQL scripts...")
        with open(SCHEMA_PATH, 'r', encoding='utf-8') as f:
            cursor.executescript(f.read())
        with open(SEED_PATH, 'r', encoding='utf-8') as f:
            cursor.executescript(f.read())
        conn.commit()
        print(">> Database successfully created and seeded!")
    conn.close()


def load_datasets():
    """Retrieve datasets from SQLite database using SQL queries and load into Pandas DataFrames."""
    print(">> Retrieving datasets from SQLite database...")
    conn = sqlite3.connect(DB_PATH)

    students_df = pd.read_sql_query("SELECT * FROM students", conn)
    courses_df = pd.read_sql_query("SELECT * FROM courses", conn)
    enrollments_df = pd.read_sql_query("SELECT * FROM enrollments", conn)
    participation_df = pd.read_sql_query("SELECT * FROM participation_records", conn)

    # Combined analytical view
    query_master = """
    SELECT 
        e.enrollment_id,
        e.enrollment_date,
        e.status AS enrollment_status,
        s.student_id,
        s.name AS student_name,
        s.department,
        s.year_of_study,
        c.course_id,
        c.course_name,
        c.category,
        c.duration_weeks,
        c.max_capacity,
        COALESCE(pr.attendance_percentage, 0.0) AS attendance_percentage,
        pr.assessment_score,
        COALESCE(pr.completion_status, 'Incomplete') AS completion_status,
        pr.completion_date
    FROM enrollments e
    JOIN students s ON e.student_id = s.student_id
    JOIN courses c ON e.course_id = c.course_id
    LEFT JOIN participation_records pr ON e.enrollment_id = pr.enrollment_id;
    """
    master_df = pd.read_sql_query(query_master, conn)
    conn.close()

    print(f"   [Loaded] Students: {len(students_df)}, Courses: {len(courses_df)}, Enrollments: {len(enrollments_df)}")
    return students_df, courses_df, enrollments_df, participation_df, master_df


def preprocess_data(df):
    """
    Data Cleaning & Transformation (Unit 4 of 25ITC31):
    - Handling missing values
    - Type casting
    - Feature engineering (Performance Index, Fill Rate)
    """
    print(">> Preprocessing and cleaning dataset...")
    df = df.copy()

    # Convert dates
    df['enrollment_date'] = pd.to_datetime(df['enrollment_date'])
    df['completion_date'] = pd.to_datetime(df['completion_date'])

    # Fill missing scores with descriptive indicator or category
    df['score_imputed'] = df['assessment_score'].fillna(df['assessment_score'].median())
    
    # Feature Engineering: Performance Category based on score
    def categorize_performance(score):
        if pd.isna(score):
            return 'Pending Evaluation'
        elif score >= 90:
            return 'Distinction (>=90)'
        elif score >= 75:
            return 'First Class (75-89)'
        elif score >= 50:
            return 'Second Class (50-74)'
        else:
            return 'Needs Improvement (<50)'

    df['performance_category'] = df['assessment_score'].apply(categorize_performance)

    # Department Acronym for cleaner charts
    dept_map = {
        'Information Technology': 'IT',
        'Computer Science and Engineering': 'CSE',
        'Electronics and Communication Engineering': 'ECE',
        'Artificial Intelligence and Data Science': 'AI&DS',
        'Mechanical Engineering': 'MECH'
    }
    df['dept_code'] = df['department'].map(dept_map).fillna(df['department'])

    print("   [Cleaned] Missing values handled and derived features created.")
    return df


def generate_visualizations(master_df, courses_df):
    """Generate professional publication-quality visual charts matching 25ITC31 syllabus."""
    os.makedirs(OUTPUT_DIR, exist_ok=True)
    print(f">> Generating data visualizations into: {OUTPUT_DIR}")

    # -------------------------------------------------------------------------
    # Chart 1: Course Popularity (Enrollment Counts per Course)
    # -------------------------------------------------------------------------
    plt.figure(figsize=(10, 6))
    course_counts = master_df['course_name'].value_counts()
    ax = sns.barplot(x=course_counts.values, y=course_counts.index, palette="viridis")
    plt.title("Course Popularity: Total Student Enrollments per Course", fontsize=14, fontweight='bold', pad=15)
    plt.xlabel("Number of Enrolled Students", fontsize=11)
    plt.ylabel("Course Title", fontsize=11)
    
    for i, count in enumerate(course_counts.values):
        ax.text(count + 0.1, i, str(count), va='center', fontweight='bold', color='#333333')

    plt.tight_layout()
    chart1_path = os.path.join(OUTPUT_DIR, "01_course_popularity.png")
    plt.savefig(chart1_path, dpi=300)
    plt.close()
    print("   [Saved] 01_course_popularity.png")

    # -------------------------------------------------------------------------
    # Chart 2: Course Seat Capacity vs Active Enrollments vs Available Seats
    # -------------------------------------------------------------------------
    plt.figure(figsize=(11, 6))
    enrolled_per_course = master_df.groupby('course_id').size().reset_index(name='enrolled_count')
    merged_cap = courses_df.merge(enrolled_per_course, on='course_id', how='left').fillna({'enrolled_count': 0})
    merged_cap['available_seats'] = merged_cap['max_capacity'] - merged_cap['enrolled_count']

    x = np.arange(len(merged_cap))
    width = 0.35

    plt.bar(x - width/2, merged_cap['max_capacity'], width, label='Max Capacity', color='#4575b4', alpha=0.85)
    plt.bar(x + width/2, merged_cap['enrolled_count'], width, label='Current Enrolled', color='#d73027', alpha=0.85)

    plt.xlabel("Course Code", fontsize=11, fontweight='bold')
    plt.ylabel("Number of Seats", fontsize=11, fontweight='bold')
    plt.title("Course Capacity Utilization: Maximum Seats vs. Current Enrollments", fontsize=14, fontweight='bold', pad=15)
    plt.xticks(x, merged_cap['course_id'], rotation=0)
    plt.legend(frameon=True, facecolor='white', framealpha=0.9)
    plt.grid(axis='y', linestyle='--', alpha=0.7)

    for i in range(len(merged_cap)):
        plt.text(x[i] - width/2, merged_cap['max_capacity'][i] + 0.15, str(merged_cap['max_capacity'][i]), ha='center', fontsize=9)
        plt.text(x[i] + width/2, merged_cap['enrolled_count'][i] + 0.15, str(int(merged_cap['enrolled_count'][i])), ha='center', fontsize=9, fontweight='bold')

    plt.tight_layout()
    chart2_path = os.path.join(OUTPUT_DIR, "02_seat_capacity_utilization.png")
    plt.savefig(chart2_path, dpi=300)
    plt.close()
    print("   [Saved] 02_seat_capacity_utilization.png")

    # -------------------------------------------------------------------------
    # Chart 3: Department-wise Enrollment Distribution (Donut Chart)
    # -------------------------------------------------------------------------
    plt.figure(figsize=(8, 8))
    dept_counts = master_df['dept_code'].value_counts()
    colors = sns.color_palette('pastel')[0:len(dept_counts)]

    wedges, texts, autotexts = plt.pie(
        dept_counts.values,
        labels=dept_counts.index,
        autopct='%1.1f%%',
        pctdistance=0.75,
        startangle=140,
        colors=colors,
        textprops={'fontsize': 11}
    )
    for autotext in autotexts:
        autotext.set_color('#111111')
        autotext.set_weight('bold')

    # Draw center circle for Donut effect
    centre_circle = plt.Circle((0, 0), 0.55, fc='white')
    fig = plt.gcf()
    fig.gca().add_artist(centre_circle)

    plt.title("Department-wise Student Enrollment Distribution", fontsize=14, fontweight='bold', pad=15)
    plt.tight_layout()
    chart3_path = os.path.join(OUTPUT_DIR, "03_department_enrollment_donut.png")
    plt.savefig(chart3_path, dpi=300)
    plt.close()
    print("   [Saved] 03_department_enrollment_donut.png")

    # -------------------------------------------------------------------------
    # Chart 4: Course Completion Status Breakdown (Count Plot)
    # -------------------------------------------------------------------------
    plt.figure(figsize=(9, 5))
    status_order = ['Completed', 'In-Progress', 'Enrolled']
    ax = sns.countplot(data=master_df, x='category', hue='enrollment_status', hue_order=status_order, palette='Set2')
    plt.title("Enrollment Status Breakdown across Course Categories", fontsize=14, fontweight='bold', pad=15)
    plt.xlabel("Course Category", fontsize=11, fontweight='bold')
    plt.ylabel("Student Count", fontsize=11, fontweight='bold')
    plt.legend(title="Enrollment Status", loc="upper right")

    for p in ax.patches:
        height = p.get_height()
        if height > 0:
            ax.annotate(f"{int(height)}", (p.get_x() + p.get_width() / 2., height),
                        ha='center', va='bottom', fontsize=9, xytext=(0, 3),
                        textcoords='offset points')

    plt.tight_layout()
    chart4_path = os.path.join(OUTPUT_DIR, "04_completion_status_breakdown.png")
    plt.savefig(chart4_path, dpi=300)
    plt.close()
    print("   [Saved] 04_completion_status_breakdown.png")

    # -------------------------------------------------------------------------
    # Chart 5: Category vs Duration vs Enrollment Heatmap
    # -------------------------------------------------------------------------
    plt.figure(figsize=(8, 6))
    pivot = pd.pivot_table(
        master_df,
        values='enrollment_id',
        index='category',
        columns='duration_weeks',
        aggfunc='count',
        fill_value=0
    )
    sns.heatmap(pivot, annot=True, fmt="d", cmap="YlGnBu", cbar_kws={'label': 'Enrollment Count'}, linewidths=1)
    plt.title("Enrollment Density: Course Category vs Duration (Weeks)", fontsize=13, fontweight='bold', pad=15)
    plt.xlabel("Course Duration in Weeks", fontsize=11)
    plt.ylabel("Course Category", fontsize=11)
    plt.tight_layout()
    chart5_path = os.path.join(OUTPUT_DIR, "05_category_duration_heatmap.png")
    plt.savefig(chart5_path, dpi=300)
    plt.close()
    print("   [Saved] 05_category_duration_heatmap.png")

    # -------------------------------------------------------------------------
    # Chart 6: Student Attendance & Assessment Score Distribution (Box Plot)
    # -------------------------------------------------------------------------
    plt.figure(figsize=(10, 6))
    completed_records = master_df[master_df['assessment_score'].notna()]
    if not completed_records.empty:
        sns.boxplot(x='category', y='assessment_score', data=completed_records, palette='Pastel1', showmeans=True)
        sns.stripplot(x='category', y='assessment_score', data=completed_records, color='black', alpha=0.6, jitter=0.2)
        plt.title("Assessment Score Distribution by Course Category (Evaluated Students)", fontsize=13, fontweight='bold', pad=15)
        plt.xlabel("Course Category", fontsize=11, fontweight='bold')
        plt.ylabel("Assessment Score (out of 100)", fontsize=11, fontweight='bold')
    else:
        sns.boxplot(x='category', y='attendance_percentage', data=master_df, palette='Pastel1')
        plt.title("Attendance Percentage Distribution by Course Category", fontsize=13, fontweight='bold', pad=15)
        plt.xlabel("Course Category", fontsize=11, fontweight='bold')
        plt.ylabel("Attendance %", fontsize=11, fontweight='bold')

    plt.tight_layout()
    chart6_path = os.path.join(OUTPUT_DIR, "06_attendance_score_boxplot.png")
    plt.savefig(chart6_path, dpi=300)
    plt.close()
    print("   [Saved] 06_attendance_score_boxplot.png")


def print_summary_statistics(master_df, courses_df):
    """Compute and display summary descriptive statistics."""
    print("\n" + "="*80)
    print("                 STUDENT ENROLLMENT STATISTICAL SUMMARY")
    print("="*80)
    print(f"Total Unique Students Enrolled : {master_df['student_id'].nunique()}")
    print(f"Total Course Offerings         : {len(courses_df)}")
    print(f"Total Enrollments Processed    : {len(master_df)}")
    completed_count = (master_df['completion_status'] == 'Completed').sum()
    print(f"Total Courses Completed        : {completed_count} ({completed_count / len(master_df) * 100:.1f}%)")
    in_progress = (master_df['completion_status'] == 'Incomplete').sum()
    print(f"Active / In-Progress Students  : {in_progress}")
    avg_att = master_df['attendance_percentage'].mean()
    print(f"Average Attendance Rate        : {avg_att:.2f}%")
    valid_scores = master_df['assessment_score'].dropna()
    if not valid_scores.empty:
        print(f"Average Assessment Score       : {valid_scores.mean():.2f}/100 (Max: {valid_scores.max()}, Min: {valid_scores.min()})")
    print("="*80 + "\n")


def main():
    print("========================================================================================")
    print("  VCET - Online Course Registration and Student Enrollment Analysis (Python Pipeline)   ")
    print("  Author: Dhanush .K (732925ITR017) | Class: 25IT3A                                    ")
    print("========================================================================================")
    ensure_database()
    students_df, courses_df, enrollments_df, participation_df, master_df = load_datasets()
    processed_df = preprocess_data(master_df)
    print_summary_statistics(processed_df, courses_df)
    generate_visualizations(processed_df, courses_df)
    print(">> All analyses and visualizations completed successfully!")


if __name__ == "__main__":
    main()
