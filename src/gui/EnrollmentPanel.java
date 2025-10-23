package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import java.sql.SQLException;
import java.util.List;
import dao.StudentCourseDAO;
import dao.StudentDAO;
import dao.CourseDAO;
import model.StudentCourse;
import model.Student;
import model.Course;

public class EnrollmentPanel extends JPanel {
    private StudentCourseDAO enrollmentDAO = new StudentCourseDAO();
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton enrollButton, unenrollButton, refreshButton;
    private JComboBox<String> studentComboBox, courseComboBox;

    public EnrollmentPanel() {
        setLayout(new BorderLayout());

        // Table
        tableModel = new DefaultTableModel(new String[]{"Student ID", "Student Name", "Student Email", "Student Department", "Course ID", "Course Name", "Course Credits"}, 0);
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getModel().addTableModelListener(e -> {
            int row = e.getFirstRow();
            int column = e.getColumn();
            if (column >= 1 && column <= 3) { // Student Name, Email, Department columns
                int studentId = (int) tableModel.getValueAt(row, 0);
                String newValue = (String) tableModel.getValueAt(row, column);
                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to update this student detail?", "Confirm Update", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    updateStudentDetail(studentId, column, newValue);
                } else {
                    loadEnrollments(); // Reload to revert changes
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Form
        JPanel formPanel = new JPanel(new GridLayout(3, 2));
        formPanel.add(new JLabel("Student:"));
        studentComboBox = new JComboBox<>();
        formPanel.add(studentComboBox);
        formPanel.add(new JLabel("Course:"));
        courseComboBox = new JComboBox<>();
        formPanel.add(courseComboBox);

        // Buttons
        JPanel buttonPanel = new JPanel();
        enrollButton = new JButton("Enroll");
        unenrollButton = new JButton("Unenroll");
        refreshButton = new JButton("Refresh");
        buttonPanel.add(enrollButton);
        buttonPanel.add(unenrollButton);
        buttonPanel.add(refreshButton);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(formPanel, BorderLayout.NORTH);
        southPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(southPanel, BorderLayout.SOUTH);

        // Listeners
        enrollButton.addActionListener(e -> enrollStudent());
        unenrollButton.addActionListener(e -> unenrollStudent());
        refreshButton.addActionListener(e -> { loadEnrollments(); loadStudentsAndCourses(); });

        loadStudentsAndCourses();
        loadEnrollments();
        clearFields();
    }

    private void loadEnrollments() {
        try {
            tableModel.setRowCount(0);
            List<StudentCourse> enrollments = enrollmentDAO.getAllEnrollments();
            StudentDAO studentDAO = new StudentDAO();
            CourseDAO courseDAO = new CourseDAO();
            for (StudentCourse e : enrollments) {
                Student student = studentDAO.getStudentById(e.getStudentId());
                Course course = courseDAO.getCourseById(e.getCourseId());
                if (student != null && course != null) {
                    tableModel.addRow(new Object[]{e.getStudentId(), student.getName(), student.getEmail(), student.getDepartment(), e.getCourseId(), course.getCourseName(), course.getCredits()});
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading enrollments: " + e.getMessage());
        }
    }

    private void enrollStudent() {
        try {
            String selectedStudent = (String) studentComboBox.getSelectedItem();
            String selectedCourse = (String) courseComboBox.getSelectedItem();
            if (selectedStudent == null || selectedCourse == null) {
                JOptionPane.showMessageDialog(this, "Please select a student and a course.");
                return;
            }
            int studentId = Integer.parseInt(selectedStudent.split(" - ")[0]);
            int courseId = Integer.parseInt(selectedCourse.split(" - ")[0]);
            StudentCourse enrollment = new StudentCourse(studentId, courseId);
            enrollmentDAO.enrollStudent(enrollment);
            loadEnrollments();
            clearFields();
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error enrolling student: " + e.getMessage());
        }
    }

    private void unenrollStudent() {
        try {
            String selectedStudent = (String) studentComboBox.getSelectedItem();
            String selectedCourse = (String) courseComboBox.getSelectedItem();
            if (selectedStudent == null || selectedCourse == null) {
                JOptionPane.showMessageDialog(this, "Please select a student and a course.");
                return;
            }
            int studentId = Integer.parseInt(selectedStudent.split(" - ")[0]);
            int courseId = Integer.parseInt(selectedCourse.split(" - ")[0]);
            enrollmentDAO.unenrollStudent(studentId, courseId);
            loadEnrollments();
            clearFields();
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error unenrolling student: " + e.getMessage());
        }
    }

    private void clearFields() {
        studentComboBox.setSelectedIndex(-1);
        courseComboBox.setSelectedIndex(-1);
    }

    private void loadStudentsAndCourses() {
        try {
            // Load students into combo box
            studentComboBox.removeAllItems();
            List<Student> students = new StudentDAO().getAllStudents();
            for (Student s : students) {
                studentComboBox.addItem(s.getStudentId() + " - " + s.getName());
            }

            // Load courses into combo box
            courseComboBox.removeAllItems();
            List<Course> courses = new CourseDAO().getAllCourses();
            for (Course c : courses) {
                courseComboBox.addItem(c.getCourseId() + " - " + c.getCourseName());
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading students and courses: " + e.getMessage());
        }
    }





    private void updateStudentDetail(int studentId, int column, String newValue) {
        try {
            StudentDAO studentDAO = new StudentDAO();
            Student student = studentDAO.getStudentById(studentId);
            if (student != null) {
                switch (column) {
                    case 1: // Name
                        student.setName(newValue);
                        break;
                    case 2: // Email
                        student.setEmail(newValue);
                        break;
                    case 3: // Department
                        student.setDepartment(newValue);
                        break;
                }
                studentDAO.updateStudent(student);
                JOptionPane.showMessageDialog(this, "Student detail updated successfully.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error updating student detail: " + e.getMessage());
        }
    }
}
