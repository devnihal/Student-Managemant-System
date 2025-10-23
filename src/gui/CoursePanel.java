package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
import dao.CourseDAO;
import model.Course;

public class CoursePanel extends JPanel {
    private CourseDAO courseDAO = new CourseDAO();
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField nameField, creditsField, descField;
    private JButton addButton, deleteButton, refreshButton;

    public CoursePanel() {
        setLayout(new BorderLayout());

        // Table
        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Credits", "Description"}, 0);
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getModel().addTableModelListener(e -> {
            int row = e.getFirstRow();
            int column = e.getColumn();
            if (column >= 1) { // Skip ID column
                try {
                    int id = (int) tableModel.getValueAt(row, 0);
                    Course course = courseDAO.getCourseById(id);
                    if (course != null) {
                        Object newValue = tableModel.getValueAt(row, column);
                        switch (column) {
                            case 1: course.setCourseName((String) newValue); break;
                            case 2: course.setCredits((Integer) newValue); break;
                            case 3: course.setDescription((String) newValue); break;
                        }
                        courseDAO.updateCourse(course);
                        loadCourses(); // Refresh to show updated data
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error updating course: " + ex.getMessage());
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Disable ID field modification
        table.getColumnModel().getColumn(0).setCellEditor(null); // Make ID column non-editable

        // Form
        JPanel formPanel = new JPanel(new GridLayout(4, 2));
        formPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);
        formPanel.add(new JLabel("Credits:"));
        creditsField = new JTextField();
        formPanel.add(creditsField);
        formPanel.add(new JLabel("Description:"));
        descField = new JTextField();
        formPanel.add(descField);

        // Buttons
        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Add");
        deleteButton = new JButton("Delete");
        refreshButton = new JButton("Refresh");
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(formPanel, BorderLayout.NORTH);
        southPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(southPanel, BorderLayout.SOUTH);

        // Listeners
        addButton.addActionListener(e -> addCourse());
        deleteButton.addActionListener(e -> deleteCourse());
        refreshButton.addActionListener(e -> loadCourses());

        loadCourses();
    }

    private void loadCourses() {
        try {
            tableModel.setRowCount(0);
            List<Course> courses = courseDAO.getAllCourses();
            for (Course c : courses) {
                tableModel.addRow(new Object[]{c.getCourseId(), c.getCourseName(), c.getCredits(), c.getDescription()});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading courses: " + e.getMessage());
        }
    }

    private void addCourse() {
        try {
            Course course = new Course(nameField.getText(), Integer.parseInt(creditsField.getText()), descField.getText());
            courseDAO.addCourse(course);
            loadCourses();
            clearFields();
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error adding course: " + e.getMessage());
        }
    }



    private void deleteCourse() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a course to delete.");
            return;
        }
        try {
            int id = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
            courseDAO.deleteCourse(id);
            loadCourses();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error deleting course: " + e.getMessage());
        }
    }

    private void clearFields() {
        nameField.setText("");
        creditsField.setText("");
        descField.setText("");
    }
}
