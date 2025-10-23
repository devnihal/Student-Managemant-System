package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import java.sql.SQLException;
import java.util.List;
import dao.TeacherDAO;
import model.Teacher;

public class TeacherPanel extends JPanel {
    private TeacherDAO teacherDAO = new TeacherDAO();
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField nameField, emailField, deptField;
    private JButton addButton, deleteButton, refreshButton;

    public TeacherPanel() {
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
                    Teacher teacher = teacherDAO.getTeacherById(id);
                    if (teacher != null) {
                        String newValue = (String) tableModel.getValueAt(row, column);
                        switch (column) {
                            case 1: teacher.setName(newValue); break;
                            case 2: teacher.setEmail(newValue); break;
                            case 3: teacher.setDepartment(newValue); break;
                        }
                        teacherDAO.updateTeacher(teacher);
                        loadTeachers(); // Refresh to show updated data
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error updating teacher: " + ex.getMessage());
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
        addButton.addActionListener(e -> addTeacher());
        deleteButton.addActionListener(e -> deleteTeacher());
        refreshButton.addActionListener(e -> loadTeachers());

        loadTeachers();
    }

    private void loadTeachers() {
        try {
            tableModel.setRowCount(0);
            List<Teacher> teachers = teacherDAO.getAllTeachers();
            for (Teacher t : teachers) {
                tableModel.addRow(new Object[]{t.getTeacherId(), t.getName(), t.getEmail(), t.getDepartment()});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading teachers: " + e.getMessage());
        }
    }

    private void addTeacher() {
        try {
            Teacher teacher = new Teacher(nameField.getText(), emailField.getText(), deptField.getText());
            teacherDAO.addTeacher(teacher);
            loadTeachers();
            clearFields();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error adding teacher: " + e.getMessage());
        }
    }



    private void deleteTeacher() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a teacher to delete.");
            return;
        }
        try {
            int id = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
            teacherDAO.deleteTeacher(id);
            loadTeachers();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error deleting teacher: " + e.getMessage());
        }
    }

    private void clearFields() {
        nameField.setText("");
        emailField.setText("");
        deptField.setText("");
    }
}
