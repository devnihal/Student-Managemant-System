package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
import dao.ResultDAO;
import dao.StudentDAO;
import dao.CourseDAO;
import model.Result;
import model.Student;
import model.Course;

public class ResultPanel extends JPanel {
    private ResultDAO resultDAO = new ResultDAO();
    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<String> studentComboBox, courseComboBox;
    private JTextField marksField, gradeField;
    private JButton addButton, updateButton, refreshButton;

    public ResultPanel() {
        setLayout(new BorderLayout());

        // Table
        tableModel = new DefaultTableModel(new String[]{"Student", "Course", "Marks", "Grade"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Form
        JPanel formPanel = new JPanel(new GridLayout(5, 2));
        formPanel.add(new JLabel("Student:"));
        studentComboBox = new JComboBox<>();
        formPanel.add(studentComboBox);
        formPanel.add(new JLabel("Course:"));
        courseComboBox = new JComboBox<>();
        formPanel.add(courseComboBox);
        formPanel.add(new JLabel("Marks:"));
        marksField = new JTextField();
        formPanel.add(marksField);
        formPanel.add(new JLabel("Grade:"));
        gradeField = new JTextField();
        formPanel.add(gradeField);

        // Buttons
        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Add Result");
        updateButton = new JButton("Update");
        refreshButton = new JButton("Refresh");
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(refreshButton);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(formPanel, BorderLayout.NORTH);
        southPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(southPanel, BorderLayout.SOUTH);

        // Listeners
        addButton.addActionListener(e -> addResult());
        updateButton.addActionListener(e -> updateResult());
        refreshButton.addActionListener(e -> { loadResults(); loadComboBoxes(); });

        loadComboBoxes();
        loadResults();
        clearFields();
    }

    private void loadResults() {
        try {
            tableModel.setRowCount(0);
            List<Result> results = resultDAO.getAllResults();
            for (Result r : results) {
                String studentName = getStudentName(r.getStudentId());
                String courseName = getCourseName(r.getCourseId());
                tableModel.addRow(new Object[]{studentName, courseName, r.getMarks(), r.getGrade()});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading results: " + e.getMessage());
        }
    }

    private String getStudentName(int studentId) {
        try {
            StudentDAO studentDAO = new StudentDAO();
            Student student = studentDAO.getStudentById(studentId);
            return student != null ? student.getName() : "Unknown";
        } catch (SQLException e) {
            return "Unknown";
        }
    }

    private String getCourseName(int courseId) {
        try {
            CourseDAO courseDAO = new CourseDAO();
            Course course = courseDAO.getCourseById(courseId);
            return course != null ? course.getCourseName() : "Unknown";
        } catch (SQLException e) {
            return "Unknown";
        }
    }

    private void addResult() {
        try {
            String selectedStudent = (String) studentComboBox.getSelectedItem();
            String selectedCourse = (String) courseComboBox.getSelectedItem();
            if (selectedStudent == null || selectedCourse == null) {
                JOptionPane.showMessageDialog(this, "Please select student and course.");
                return;
            }
            int studentId = Integer.parseInt(selectedStudent.split(" - ")[0]);
            int courseId = Integer.parseInt(selectedCourse.split(" - ")[0]);
            double marks = Double.parseDouble(marksField.getText());
            String grade = gradeField.getText();
            Result result = new Result(studentId, courseId, marks, grade);
            resultDAO.addResult(result);
            loadResults();
            clearFields();
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error adding result: " + e.getMessage());
        }
    }

    private void updateResult() {
        try {
            String selectedStudent = (String) studentComboBox.getSelectedItem();
            String selectedCourse = (String) courseComboBox.getSelectedItem();
            if (selectedStudent == null || selectedCourse == null) {
                JOptionPane.showMessageDialog(this, "Please select student and course.");
                return;
            }
            int studentId = Integer.parseInt(selectedStudent.split(" - ")[0]);
            int courseId = Integer.parseInt(selectedCourse.split(" - ")[0]);
            Result result = resultDAO.getResultByStudentAndCourse(studentId, courseId);
            if (result != null) {
                result.setMarks(Double.parseDouble(marksField.getText()));
                result.setGrade(gradeField.getText());
                resultDAO.updateResult(result);
                loadResults();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Result not found.");
            }
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error updating result: " + e.getMessage());
        }
    }

    private void clearFields() {
        studentComboBox.setSelectedIndex(-1);
        courseComboBox.setSelectedIndex(-1);
        marksField.setText("");
        gradeField.setText("");
    }

    private void loadComboBoxes() {
        try {
            // Load students
            studentComboBox.removeAllItems();
            StudentDAO studentDAO = new StudentDAO();
            List<Student> students = studentDAO.getAllStudents();
            for (Student s : students) {
                studentComboBox.addItem(s.getStudentId() + " - " + s.getName());
            }

            // Load courses
            courseComboBox.removeAllItems();
            CourseDAO courseDAO = new CourseDAO();
            List<Course> courses = courseDAO.getAllCourses();
            for (Course c : courses) {
                courseComboBox.addItem(c.getCourseId() + " - " + c.getCourseName());
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading combo boxes: " + e.getMessage());
        }
    }
}
