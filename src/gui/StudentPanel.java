package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
import dao.StudentDAO;
import model.Student;

public class StudentPanel extends JPanel {
    private StudentDAO studentDAO = new StudentDAO();
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField nameField, emailField, deptField;
    private JButton addButton, updateButton, deleteButton, refreshButton;

    public StudentPanel() {
        setLayout(new BorderLayout());

        // Table
        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Email", "Department"}, 0);
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getModel().addTableModelListener(e -> {
            int row = e.getFirstRow();
            int column = e.getColumn();
            if (column >= 1) { // Skip ID column
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
                        loadStudents(); // Refresh to show updated data
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error updating student: " + ex.getMessage());
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
        formPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        formPanel.add(emailField);
        formPanel.add(new JLabel("Department:"));
        deptField = new JTextField();
        formPanel.add(deptField);

        // Disable ID field modification
        table.getColumnModel().getColumn(0).setCellEditor(null); // Make ID column non-editable

        // Buttons
        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        refreshButton = new JButton("Refresh");
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(formPanel, BorderLayout.NORTH);
        southPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(southPanel, BorderLayout.SOUTH);

        // Listeners
        addButton.addActionListener(e -> addStudent());
        updateButton.addActionListener(e -> updateStudent());
        deleteButton.addActionListener(e -> deleteStudent());
        refreshButton.addActionListener(e -> loadStudents());

        loadStudents();
    }

    private void loadStudents() {
        try {
            tableModel.setRowCount(0);
            List<Student> students = studentDAO.getAllStudents();
            for (Student s : students) {
                tableModel.addRow(new Object[]{s.getStudentId(), s.getName(), s.getEmail(), s.getDepartment()});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading students: " + e.getMessage());
        }
    }

    private void addStudent() {
        try {
            Student student = new Student(nameField.getText(), emailField.getText(), deptField.getText());
            studentDAO.addStudent(student);
            loadStudents();
            clearFields();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error adding student: " + e.getMessage());
        }
    }

    private void updateStudent() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a student to update.");
            return;
        }
        try {
            int id = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
            Student student = studentDAO.getStudentById(id);
            if (student != null) {
                String name = nameField.getText().trim();
                String email = emailField.getText().trim();
                String dept = deptField.getText().trim();
                if (name.isEmpty() || email.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Name and Email cannot be empty.");
                    return;
                }
                student.setName(name);
                student.setEmail(email);
                student.setDepartment(dept);
                studentDAO.updateStudent(student);
                loadStudents();
                clearFields();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error updating student: " + e.getMessage());
        }
    }

    private void deleteStudent() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a student to delete.");
            return;
        }
        try {
            int id = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
            studentDAO.deleteStudent(id);
            loadStudents();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error deleting student: " + e.getMessage());
        }
    }

    private void clearFields() {
        nameField.setText("");
        emailField.setText("");
        deptField.setText("");
    }
}
