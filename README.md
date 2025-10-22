# Student Management System - Complete Java Course for Beginners

Welcome to the **Student Management System** project! This is a comprehensive Java course disguised as a project walkthrough. We'll build and understand a full desktop application that manages students, teachers, courses, enrollments, timetables, attendance, and results.

**Who is this for?** Absolute Java beginners. No prior experience needed. We'll explain every line of code, every concept, and every block.

**What you'll learn:**

- Java basics (classes, objects, methods)
- Object-Oriented Programming (OOP)
- Database programming with JDBC
- Building GUIs with Swing
- File handling and configuration
- Exception handling
- Collections and generics
- And much more!

By the end, you'll understand this entire project and be ready to build your own Java apps.

## Course Overview

This course is divided into lessons. Each lesson covers a part of the project with:

- **Concept Explanations**: What and why.
- **Code Walkthroughs**: Line-by-line breakdowns.
- **Exercises**: Try modifying the code.
- **Key Takeaways**: What you learned.

**Project Goal**: A tabbed GUI app where users can manage school data stored in MySQL.

---

## Lesson 1: Project Setup and Database

### 1.1 What is a Project Structure?

In Java, projects are organized in folders called **packages**. Our structure:

- `src/`: Source code
- `lib/`: External libraries (like MySQL driver)
- `db/`: Database files
- `build/`: Compiled code (created later)

**Why packages?** Like organizing books in a library – easier to find and manage.

### 1.2 Setting Up the Database

Databases store data persistently. We use MySQL.

**File: `db/schema.sql`**

```sql
-- Student Management System Database Schema

CREATE DATABASE IF NOT EXISTS student_mgmt;
USE student_mgmt;

-- Student table
CREATE TABLE Student (
    student_id INT PRIMARY KEY AUTO_INCREMENT,  -- Unique ID, auto-increases
    name VARCHAR(100) NOT NULL,                  -- Name, required
    email VARCHAR(120) UNIQUE NOT NULL,          -- Email, unique and required
    department VARCHAR(100),                     -- Department, optional
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP  -- Auto-set to current time
);
```

**Explanation:**

- `CREATE TABLE Student`: Makes a table named "Student".
- `student_id INT PRIMARY KEY AUTO_INCREMENT`: ID field, unique, increases automatically.
- `name VARCHAR(100) NOT NULL`: Text field up to 100 chars, must have a value.
- `UNIQUE`: No two students can have the same email.
- `TIMESTAMP DEFAULT CURRENT_TIMESTAMP`: Sets creation time automatically.

**Other Tables:**

- `Teacher`: Similar to Student, but for teachers.
- `Course`: Courses with name, credits, description.
- `StudentCourse`: Links students to courses (enrollments).
- `Timetable`: Groups of schedules.
- `Slot`: Specific class times (day, hour, course, teacher, location).
- `Attendance`: Tracks if a student was present in a slot on a date.
- `Result`: Grades for students in courses.

**Relationships:**

- Foreign keys link tables (e.g., Attendance references Student, Course, Slot).

**Exercise:** Add a new table for "Assignments" with fields for course_id, title, due_date.

### 1.3 Database Connection

**File: `src/util/DatabaseConnection.java`**

```java
package util;  // This file is in the 'util' package

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.util.Properties;

public class DatabaseConnection {
    private static String URL;
    private static String USER;
    private static String PASSWORD;

    static {  // This block runs once when the class is loaded
        loadDatabaseConfig();
    }

    private static void loadDatabaseConfig() {
        Properties props = new Properties();  // Object to hold key-value pairs
        try (InputStream input = new FileInputStream(System.getProperty("user.dir") + "/.env")) {
            // Open .env file in project root
            props.load(input);  // Load properties from file
            String host = props.getProperty("DB_HOST");  // Get value for DB_HOST
            String port = props.getProperty("DB_PORT");
            String dbName = props.getProperty("DB_NAME");
            USER = props.getProperty("DB_USER");
            PASSWORD = props.getProperty("DB_PASSWORD");
            URL = "jdbc:mysql://" + host + ":" + port + "/" + dbName;  // Build connection URL
        } catch (IOException e) {
            throw new RuntimeException("Error loading database configuration", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");  // Load MySQL driver
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found", e);
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);  // Connect and return connection
    }
}
```

**Key Concepts:**

- **Static Block**: Code that runs when class loads – perfect for setup.
- **Properties**: Reads config from `.env` file (like environment variables).
- **JDBC URL**: `jdbc:mysql://host:port/database` – tells Java how to connect.
- **Connection**: Like a phone line to the database. Use it to send queries.

**Why static?** One connection setup for the whole app.

**Exercise:** Modify to support multiple databases by adding a parameter to `getConnection()`.

---

## Lesson 2: Model Classes (Data Blueprints)

Models represent data. They're simple classes with fields and getters/setters.

### 2.1 What is a Class?

A class is a template. An object is an instance.

**Example:** `Student` class defines what a student has (name, email). A `Student` object is John with name="John".

### 2.2 Student Model

**File: `src/model/Student.java`**

```java
package model;  // In 'model' package

import java.sql.Timestamp;  // For date/time

public class Student {
    private int studentId;        // Private: only accessible inside this class
    private String name;
    private String email;
    private String department;
    private Timestamp createdAt;  // When record was created

    public Student() {}  // Default constructor: no parameters

    public Student(String name, String email, String department) {  // Constructor with params
        this.name = name;  // 'this' refers to this object
        this.email = email;
        this.department = department;
    }

    // Getters: return field values
    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }  // Setters: change values

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    @Override  // Overrides Object's toString
    public String toString() {
        return "Student{" +
                "studentId=" + studentId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", department='" + department + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
```

**Key Concepts:**

- **Encapsulation**: Fields private, access via methods.
- **Constructors**: Initialize objects. Overloaded (multiple versions).
- **Getters/Setters**: Standard way to access private fields.
- **toString()**: For printing/debugging.

**Why private fields?** Protects data – can't be changed directly.

**Other Models:**

- `Teacher`: Similar, but for teachers.
- `Course`: Has courseName, credits, description.
- `StudentCourse`: Links student_id and course_id, with enrolled_at timestamp.
- `Timetable`: name, semester, department.
- `Slot`: timetable_id, day, hour, course_id, teacher_id, location.
- `Attendance`: student_id, course_id, slot_id, date, status (boolean), marked_by.
- `Result`: student_id, course_id, marks (decimal), grade.

**Exercise:** Add a `phone` field to `Student` with getter/setter.

---

## Lesson 3: DAO Classes (Data Access Objects)

DAOs handle database operations: Create, Read, Update, Delete (CRUD).

### 3.1 What is JDBC?

Java Database Connectivity – sends SQL queries from Java.

**PreparedStatement**: Safe way to insert data (prevents SQL injection).

**ResultSet**: Holds query results. Use `next()` to move to rows, `getString()` to get values.

### 3.2 StudentDAO

**File: `src/dao/StudentDAO.java`**

```java
package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Student;
import util.DatabaseConnection;

public class StudentDAO {
    // Add a new student
    public void addStudent(Student student) throws SQLException {
        String sql = "INSERT INTO Student (name, email, department) VALUES (?, ?, ?)";  // ? are placeholders
        try (Connection conn = DatabaseConnection.getConnection();  // Get connection
             PreparedStatement stmt = conn.prepareStatement(sql)) {  // Prepare statement
            stmt.setString(1, student.getName());  // Set first ?
            stmt.setString(2, student.getEmail());  // Second ?
            stmt.setString(3, student.getDepartment());  // Third ?
            stmt.executeUpdate();  // Run insert
        }  // Auto-close connection and statement
    }

    // Get student by ID
    public Student getStudentById(int id) throws SQLException {
        String sql = "SELECT * FROM Student WHERE student_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);  // Set ID
            ResultSet rs = stmt.executeQuery();  // Run select
            if (rs.next()) {  // If a row exists
                Student student = new Student();
                student.setStudentId(rs.getInt("student_id"));  // Get from result
                student.setName(rs.getString("name"));
                student.setEmail(rs.getString("email"));
                student.setDepartment(rs.getString("department"));
                student.setCreatedAt(rs.getTimestamp("created_at"));
                return student;
            }
        }
        return null;  // No student found
    }

    // Get all students
    public List<Student> getAllStudents() throws SQLException {
        List<Student> students = new ArrayList<>();  // List to hold students
        String sql = "SELECT * FROM Student";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();  // No params, use Statement
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {  // Loop through all rows
                Student student = new Student();
                student.setStudentId(rs.getInt("student_id"));
                student.setName(rs.getString("name"));
                student.setEmail(rs.getString("email"));
                student.setDepartment(rs.getString("department"));
                student.setCreatedAt(rs.getTimestamp("created_at"));
                students.add(student);  // Add to list
            }
        }
        return students;
    }

    // Update student
    public void updateStudent(Student student) throws SQLException {
        String sql = "UPDATE Student SET name = ?, email = ?, department = ? WHERE student_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, student.getName());
            stmt.setString(2, student.getEmail());
            stmt.setString(3, student.getDepartment());
            stmt.setInt(4, student.getStudentId());
            stmt.executeUpdate();
        }
    }

    // Delete student
    public void deleteStudent(int id) throws SQLException {
        String sql = "DELETE FROM Student WHERE student_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
```

**Key Concepts:**

- **Try-with-resources**: Auto-closes resources (Connection, Statement).
- **SQLException**: Thrown if database error.
- **List and ArrayList**: Collections for multiple objects.
- **CRUD**: Create (add), Read (get), Update, Delete.

**Other DAOs:**

- `TeacherDAO`: Same pattern for teachers.
- `CourseDAO`: For courses.
- `StudentCourseDAO`: Enroll/unenroll students.
- `TimetableDAO`: Manage timetables.
- `SlotDAO`: Manage slots.
- `AttendanceDAO`: Mark and view attendance.
- `ResultDAO`: Add and view results.

**Exercise:** Add a method `getStudentsByDepartment(String dept)` that returns students in a department.

---

## Lesson 4: GUI Classes (User Interface)

GUIs let users interact. We use Swing.

### 4.1 Swing Basics

- `JFrame`: Window.
- `JPanel`: Container.
- `JButton`: Clickable.
- `JTable`: Data grid.
- `JTextField`: Text input.
- `JComboBox`: Dropdown.

**Event Listeners**: Code that runs on clicks.

### 4.2 MainGUI

**File: `src/gui/MainGUI.java`**

```java
package gui;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class MainGUI extends JFrame {  // Extends JFrame: this IS a window
    private JTabbedPane tabbedPane;  // Tabs for different sections

    public MainGUI() {
        setTitle("Student Management System");  // Window title
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Close app on X
        setSize(800, 600);  // Size in pixels
        setLocationRelativeTo(null);  // Center on screen

        tabbedPane = new JTabbedPane();  // Create tabs

        // Add tabs with panels
        tabbedPane.addTab("Students", new StudentPanel());
        tabbedPane.addTab("Teachers", new TeacherPanel());
        tabbedPane.addTab("Courses", new CoursePanel());
        tabbedPane.addTab("Enrollments", new EnrollmentPanel());
        tabbedPane.addTab("Timetables", new TimetablePanel());
        tabbedPane.addTab("Slots", new SlotPanel());
        tabbedPane.addTab("Attendance", new AttendancePanel());
        tabbedPane.addTab("Results", new ResultPanel());

        add(tabbedPane);  // Add tabs to window
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {  // Run on Event Dispatch Thread (EDT)
            new MainGUI().setVisible(true);  // Create and show window
        });
    }
}
```

**Key Concepts:**

- **Inheritance**: `MainGUI extends JFrame` – gets window features.
- **Layout**: `add()` places components.
- **EDT**: Swing runs on special thread for safety.

### 4.3 StudentPanel

**File: `src/gui/StudentPanel.java`**

```java
public class StudentPanel extends JPanel {
    private StudentDAO studentDAO = new StudentDAO();
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField nameField, emailField, deptField;
    private JButton addButton, updateButton, deleteButton, refreshButton;

    public StudentPanel() {
        setLayout(new BorderLayout());  // Layout: North, South, East, West, Center

        // Table setup
        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Email", "Department"}, 0);
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  // One row at a time
        // Table listener for inline editing
        table.getModel().addTableModelListener(e -> {
            int row = e.getFirstRow();
            int column = e.getColumn();
            if (column >= 1) {  // Skip ID
                try {
                    int id = (int) tableModel.getValueAt(row, 0);
                    Student student = studentDAO.getStudentById(id);
                    if (student != null) {
                        String newValue = (String) tableModel.getValueAt(row, column);
                        switch (column) {
                            case 1: student.setName(newValue); break;
                            case 2: student.setEmail(newValue); break;
                            case 3: student.setDepartment(newValue); break;
                        }
                        studentDAO.updateStudent(student);
                        loadStudents();  // Refresh
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error updating: " + ex.getMessage());
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Form
        JPanel formPanel = new JPanel(new GridLayout(4, 2));
        formPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);
        // ... similar for email, dept

        // Buttons
        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Add");
        // ... other buttons

        // Listeners
        addButton.addActionListener(e -> addStudent());
        // ... other listeners

        loadStudents();  // Load data on start
    }

    private void loadStudents() {
        try {
            tableModel.setRowCount(0);  // Clear table
            List<Student> students = studentDAO.getAllStudents();
            for (Student s : students) {
                tableModel.addRow(new Object[]{s.getStudentId(), s.getName(), s.getEmail(), s.getDepartment()});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading: " + e.getMessage());
        }
    }

    private void addStudent() {
        try {
            Student student = new Student(nameField.getText(), emailField.getText(), deptField.getText());
            studentDAO.addStudent(student);
            loadStudents();
            clearFields();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error adding: " + e.getMessage());
        }
    }

    // Similar methods for update, delete
}
```

**Key Concepts:**

- **TableModel**: Manages table data.
- **Listeners**: Respond to events (clicks, edits).
- **JOptionPane**: Pop-up messages.

**Other Panels:** Similar structure – table, form, buttons, listeners.

**Exercise:** Add a search field to filter students by name.

---

## Lesson 5: Main and Utilities

### 5.1 Main Entry Point

**File: `src/Main.java`**

```java
import gui.MainGUI;

public class Main {
    public static void main(String[] args) {
        MainGUI.main(args);  // Start GUI
    }
}
```

Simple launcher.

### 5.2 Compilation Script

**File: `compile.bat`**

```batch
@echo off
echo Compiling Java files...
javac -cp "lib/mysql-connector-j-9.4.0.jar;src" -d build src/model/*.java src/dao/*.java src/gui/*.java src/util/*.java src/Main.java
if %errorlevel% neq 0 (
    echo Compilation failed.
    pause
    exit /b 1
)
echo Compilation successful.
echo Running the application...
java -cp "lib/mysql-connector-j-9.4.0.jar;build" Main
pause
```

**Explanation:**

- `javac`: Compiler. `-cp`: Classpath (where to find classes/libs).
- `-d build`: Output to build/.
- `java`: Run. `-cp`: Classpath for running.

---

## Lesson 6: Running and Testing

1. Set up MySQL and run `schema.sql`.
2. Create `.env` file.
3. Run `compile.bat`.
4. Use the GUI to add/view data.

**Test Each Panel:**

- Add a student, check database.
- Enroll in course, view enrollment.
- Mark attendance, view records.

**Common Issues:**

- Driver not found: Check lib/.
- Connection error: Check .env and MySQL.

---

## Final Project: What You Built

A full CRUD app with MVC architecture:

- **Model**: Data classes.
- **View**: GUI panels.
- **Controller**: DAOs.

**Skills Learned:**

- OOP, JDBC, Swing, Exception Handling, Collections.

**Next Steps:** Modify for your needs, add features like reports.

Congratulations! You're now a Java developer.
