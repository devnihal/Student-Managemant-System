package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import java.sql.SQLException;
import java.util.List;
import dao.TimetableDAO;
import model.Timetable;

public class TimetablePanel extends JPanel {
    private TimetableDAO timetableDAO = new TimetableDAO();
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField nameField, semesterField, deptField;
    private JButton addButton, updateButton, deleteButton, refreshButton;

    public TimetablePanel() {
        setLayout(new BorderLayout());

        // Table
        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Semester", "Department"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Form
        JPanel formPanel = new JPanel(new GridLayout(4, 2));
        formPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);
        formPanel.add(new JLabel("Semester:"));
        semesterField = new JTextField();
        formPanel.add(semesterField);
        formPanel.add(new JLabel("Department:"));
        deptField = new JTextField();
        formPanel.add(deptField);

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
        addButton.addActionListener(e -> addTimetable());
        updateButton.addActionListener(e -> updateTimetable());
        deleteButton.addActionListener(e -> deleteTimetable());
        refreshButton.addActionListener(e -> loadTimetables());

        loadTimetables();
    }

    private void loadTimetables() {
        try {
            tableModel.setRowCount(0);
            List<Timetable> timetables = timetableDAO.getAllTimetables();
            for (Timetable t : timetables) {
                tableModel.addRow(new Object[]{t.getTimetableId(), t.getName(), t.getSemester(), t.getDepartment()});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading timetables: " + e.getMessage());
        }
    }

    private void addTimetable() {
        try {
            Timetable timetable = new Timetable(nameField.getText(), semesterField.getText(), deptField.getText());
            timetableDAO.addTimetable(timetable);
            loadTimetables();
            clearFields();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error adding timetable: " + e.getMessage());
        }
    }

    private void updateTimetable() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a timetable to update.");
            return;
        }
        try {
            int id = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
            Timetable timetable = timetableDAO.getTimetableById(id);
            if (timetable != null) {
                timetable.setName(nameField.getText());
                timetable.setSemester(semesterField.getText());
                timetable.setDepartment(deptField.getText());
                timetableDAO.updateTimetable(timetable);
                loadTimetables();
                clearFields();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error updating timetable: " + e.getMessage());
        }
    }

    private void deleteTimetable() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a timetable to delete.");
            return;
        }
        try {
            int id = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
            timetableDAO.deleteTimetable(id);
            loadTimetables();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error deleting timetable: " + e.getMessage());
        }
    }

    private void clearFields() {
        nameField.setText("");
        semesterField.setText("");
        deptField.setText("");
    }
}
