package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import java.sql.SQLException;
import java.util.List;
import dao.SlotDAO;
import dao.TimetableDAO;
import dao.TeacherDAO;
import dao.CourseDAO;
import model.Slot;
import model.Timetable;
import model.Teacher;
import model.Course;

public class SlotPanel extends JPanel {
    private SlotDAO slotDAO = new SlotDAO();
    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<String> timetableComboBox, teacherComboBox, courseComboBox;
    private JTextField dayField, hourField, locationField;
    private JButton addButton, updateButton, deleteButton, refreshButton, clearButton;

    public SlotPanel() {
        setLayout(new BorderLayout());

        // Table
        tableModel = new DefaultTableModel(new String[]{"ID", "Timetable", "Day", "Hour", "Course", "Teacher", "Location"}, 0);
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    loadSlotDataToFields(selectedRow);
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Form
        JPanel formPanel = new JPanel(new GridLayout(7, 2));
        formPanel.add(new JLabel("Timetable:"));
        timetableComboBox = new JComboBox<>();
        formPanel.add(timetableComboBox);
        formPanel.add(new JLabel("Day:"));
        dayField = new JTextField();
        formPanel.add(dayField);
        formPanel.add(new JLabel("Hour:"));
        hourField = new JTextField();
        formPanel.add(hourField);
        formPanel.add(new JLabel("Course:"));
        courseComboBox = new JComboBox<>();
        formPanel.add(courseComboBox);
        formPanel.add(new JLabel("Teacher:"));
        teacherComboBox = new JComboBox<>();
        formPanel.add(teacherComboBox);
        formPanel.add(new JLabel("Location:"));
        locationField = new JTextField();
        formPanel.add(locationField);

        // Buttons
        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        refreshButton = new JButton("Refresh");
        clearButton = new JButton("Clear");
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(clearButton);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(formPanel, BorderLayout.NORTH);
        southPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(southPanel, BorderLayout.SOUTH);

        // Listeners
        addButton.addActionListener(e -> addSlot());
        updateButton.addActionListener(e -> updateSlot());
        deleteButton.addActionListener(e -> deleteSlot());
        refreshButton.addActionListener(e -> { loadSlots(); loadComboBoxes(); });
        clearButton.addActionListener(e -> clearFields());

        loadComboBoxes();
        loadSlots();
        clearFields();
    }

    private void loadSlots() {
        try {
            tableModel.setRowCount(0);
            List<Slot> slots = slotDAO.getAllSlots();
            for (Slot s : slots) {
                String timetableName = getTimetableName(s.getTimetableId());
                String courseName = getCourseName(s.getCourseId());
                String teacherName = getTeacherName(s.getTeacherId());
                tableModel.addRow(new Object[]{s.getSlotId(), timetableName, s.getDay(), s.getHour(), courseName, teacherName, s.getLocation()});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading slots: " + e.getMessage());
        }
    }

    private void addSlot() {
        try {
            String selectedTimetable = (String) timetableComboBox.getSelectedItem();
            String selectedCourse = (String) courseComboBox.getSelectedItem();
            String selectedTeacher = (String) teacherComboBox.getSelectedItem();
            if (selectedTimetable == null || selectedCourse == null || selectedTeacher == null) {
                JOptionPane.showMessageDialog(this, "Please select timetable, course, and teacher.");
                return;
            }
            int timetableId = Integer.parseInt(selectedTimetable.split(" - ")[0]);
            int courseId = Integer.parseInt(selectedCourse.split(" - ")[0]);
            int teacherId = Integer.parseInt(selectedTeacher.split(" - ")[0]);
            Slot slot = new Slot(timetableId, dayField.getText(), Integer.parseInt(hourField.getText()), courseId, teacherId, locationField.getText());
            slotDAO.addSlot(slot);
            loadSlots();
            clearFields();
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error adding slot: " + e.getMessage());
        }
    }

    private void updateSlot() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a slot to update.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to update this slot?", "Confirm Update", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            int id = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
            Slot slot = slotDAO.getSlotById(id);
            if (slot != null) {
                String selectedTimetable = (String) timetableComboBox.getSelectedItem();
                String selectedCourse = (String) courseComboBox.getSelectedItem();
                String selectedTeacher = (String) teacherComboBox.getSelectedItem();
                if (selectedTimetable == null || selectedCourse == null || selectedTeacher == null) {
                    JOptionPane.showMessageDialog(this, "Please select timetable, course, and teacher.");
                    return;
                }
                slot.setTimetableId(Integer.parseInt(selectedTimetable.split(" - ")[0]));
                slot.setDay(dayField.getText());
                slot.setHour(Integer.parseInt(hourField.getText()));
                slot.setCourseId(Integer.parseInt(selectedCourse.split(" - ")[0]));
                slot.setTeacherId(Integer.parseInt(selectedTeacher.split(" - ")[0]));
                slot.setLocation(locationField.getText());
                slotDAO.updateSlot(slot);
                loadSlots();
                clearFields();
            }
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error updating slot: " + e.getMessage());
        }
    }

    private void deleteSlot() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a slot to delete.");
            return;
        }
        try {
            int id = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
            slotDAO.deleteSlot(id);
            loadSlots();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error deleting slot: " + e.getMessage());
        }
    }

    private void clearFields() {
        dayField.setText("");
        hourField.setText("");
        locationField.setText("");
        timetableComboBox.setSelectedIndex(-1);
        courseComboBox.setSelectedIndex(-1);
        teacherComboBox.setSelectedIndex(-1);
    }

    private void loadSlotDataToFields(int selectedRow) {
        try {
            int id = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
            Slot slot = slotDAO.getSlotById(id);
            if (slot != null) {
                // Set combo boxes
                setComboBoxSelection(timetableComboBox, slot.getTimetableId() + " - " + getTimetableName(slot.getTimetableId()));
                setComboBoxSelection(courseComboBox, slot.getCourseId() + " - " + getCourseName(slot.getCourseId()));
                setComboBoxSelection(teacherComboBox, slot.getTeacherId() + " - " + getTeacherName(slot.getTeacherId()));

                // Set text fields
                dayField.setText(slot.getDay());
                hourField.setText(String.valueOf(slot.getHour()));
                locationField.setText(slot.getLocation());
            }
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error loading slot data: " + e.getMessage());
        }
    }

    private void setComboBoxSelection(JComboBox<String> comboBox, String value) {
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            if (comboBox.getItemAt(i).equals(value)) {
                comboBox.setSelectedIndex(i);
                return;
            }
        }
        comboBox.setSelectedIndex(-1); // If not found, deselect
    }

    private String getTimetableName(int timetableId) {
        try {
            TimetableDAO timetableDAO = new TimetableDAO();
            Timetable timetable = timetableDAO.getTimetableById(timetableId);
            return timetable != null ? timetable.getName() : "Unknown";
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

    private String getTeacherName(int teacherId) {
        try {
            TeacherDAO teacherDAO = new TeacherDAO();
            Teacher teacher = teacherDAO.getTeacherById(teacherId);
            return teacher != null ? teacher.getName() : "Unknown";
        } catch (SQLException e) {
            return "Unknown";
        }
    }

    private void loadComboBoxes() {
        try {
            // Load timetables
            timetableComboBox.removeAllItems();
            TimetableDAO timetableDAO = new TimetableDAO();
            List<Timetable> timetables = timetableDAO.getAllTimetables();
            for (Timetable t : timetables) {
                timetableComboBox.addItem(t.getTimetableId() + " - " + t.getName());
            }

            // Load teachers
            teacherComboBox.removeAllItems();
            TeacherDAO teacherDAO = new TeacherDAO();
            List<Teacher> teachers = teacherDAO.getAllTeachers();
            for (Teacher t : teachers) {
                teacherComboBox.addItem(t.getTeacherId() + " - " + t.getName());
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
