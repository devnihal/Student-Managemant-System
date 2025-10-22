package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Calendar;
import dao.AttendanceDAO;
import dao.StudentDAO;
import dao.CourseDAO;
import dao.SlotDAO;
import dao.TeacherDAO;
import model.Attendance;
import model.Student;
import model.Course;
import model.Slot;
import model.Teacher;

public class AttendancePanel extends JPanel {
    private AttendanceDAO attendanceDAO = new AttendanceDAO();
    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<String> studentComboBox, slotComboBox, markedByComboBox;
    private JSpinner dateSpinner;
    private JCheckBox statusCheckBox;
    private JButton markButton, updateButton, refreshButton;

    public AttendancePanel() {
        setLayout(new BorderLayout());

        // Table
        tableModel = new DefaultTableModel(new String[]{"ID", "Student", "Course", "Slot", "Date", "Status", "Marked By"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Form
        JPanel formPanel = new JPanel(new GridLayout(6, 2));
        formPanel.add(new JLabel("Student:"));
        studentComboBox = new JComboBox<>();
        formPanel.add(studentComboBox);
        formPanel.add(new JLabel("Slot:"));
        slotComboBox = new JComboBox<>();
        formPanel.add(slotComboBox);
        formPanel.add(new JLabel("Date:"));
        dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(dateEditor);
        formPanel.add(dateSpinner);
        formPanel.add(new JLabel("Status:"));
        statusCheckBox = new JCheckBox("Present");
        formPanel.add(statusCheckBox);
        formPanel.add(new JLabel("Marked By (Teacher):"));
        markedByComboBox = new JComboBox<>();
        formPanel.add(markedByComboBox);

        // Buttons
        JPanel buttonPanel = new JPanel();
        markButton = new JButton("Mark Attendance");
        updateButton = new JButton("Update");
        refreshButton = new JButton("Refresh");
        buttonPanel.add(markButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(refreshButton);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(formPanel, BorderLayout.NORTH);
        southPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(southPanel, BorderLayout.SOUTH);

        // Listeners
        markButton.addActionListener(e -> markAttendance());
        updateButton.addActionListener(e -> updateAttendance());
        refreshButton.addActionListener(e -> { loadAttendance(); loadComboBoxes(); });

        loadComboBoxes();
        loadAttendance();
        clearFields();
    }

    private void loadAttendance() {
        try {
            tableModel.setRowCount(0);
            List<Attendance> attendances = attendanceDAO.getAllAttendances();
            for (Attendance a : attendances) {
                String studentName = getStudentName(a.getStudentId());
                String courseName = getCourseNameFromSlot(a.getSlotId());
                String slotInfo = getSlotInfo(a.getSlotId());
                String teacherName = a.getMarkedBy() != null ? getTeacherName(a.getMarkedBy()) : "";
                tableModel.addRow(new Object[]{a.getAttendanceId(), studentName, courseName, slotInfo, a.getDate(), a.isStatus() ? "Present" : "Absent", teacherName});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading attendance: " + e.getMessage());
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

    private String getTeacherName(int teacherId) {
        try {
            TeacherDAO teacherDAO = new TeacherDAO();
            Teacher teacher = teacherDAO.getTeacherById(teacherId);
            return teacher != null ? teacher.getName() : "Unknown";
        } catch (SQLException e) {
            return "Unknown";
        }
    }

    private String getCourseNameFromSlot(int slotId) {
        try {
            SlotDAO slotDAO = new SlotDAO();
            Slot slot = slotDAO.getSlotById(slotId);
            if (slot != null) {
                CourseDAO courseDAO = new CourseDAO();
                Course course = courseDAO.getCourseById(slot.getCourseId());
                return course != null ? course.getCourseName() : "Unknown";
            }
            return "Unknown";
        } catch (SQLException e) {
            return "Unknown";
        }
    }

    private String getSlotInfo(int slotId) {
        try {
            SlotDAO slotDAO = new SlotDAO();
            Slot slot = slotDAO.getSlotById(slotId);
            return slot != null ? slot.getDay() + " " + slot.getHour() : "Unknown";
        } catch (SQLException e) {
            return "Unknown";
        }
    }

    private void markAttendance() {
        try {
            String selectedStudent = (String) studentComboBox.getSelectedItem();
            String selectedSlot = (String) slotComboBox.getSelectedItem();
            if (selectedStudent == null || selectedSlot == null) {
                JOptionPane.showMessageDialog(this, "Please select student and slot.");
                return;
            }
            int studentId = Integer.parseInt(selectedStudent.split(" - ")[0]);
            int slotId = Integer.parseInt(selectedSlot.split(" - ")[0]);
            SlotDAO slotDAO = new SlotDAO();
            Slot slot = slotDAO.getSlotById(slotId);
            int courseId = slot.getCourseId();
            java.util.Date selectedDate = (java.util.Date) dateSpinner.getValue();
            Date date = new Date(selectedDate.getTime());
            boolean status = statusCheckBox.isSelected();
            String selectedMarkedBy = (String) markedByComboBox.getSelectedItem();
            Integer markedBy = selectedMarkedBy == null ? null : Integer.parseInt(selectedMarkedBy.split(" - ")[0]);
            Attendance attendance = new Attendance(studentId, courseId, slotId, date, status, markedBy);
            attendanceDAO.markAttendance(attendance);
            loadAttendance();
            clearFields();
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error marking attendance: " + e.getMessage());
        }
    }

    private void updateAttendance() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select an attendance record to update.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to update this attendance?", "Confirm Update", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            boolean status = statusCheckBox.isSelected();
            String selectedMarkedBy = (String) markedByComboBox.getSelectedItem();
            Integer markedBy = selectedMarkedBy == null ? null : Integer.parseInt(selectedMarkedBy.split(" - ")[0]);
            Attendance attendance = new Attendance();
            attendance.setAttendanceId(id);
            attendance.setStatus(status);
            attendance.setMarkedBy(markedBy);
            attendanceDAO.updateAttendance(attendance);
            loadAttendance();
            clearFields();
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error updating attendance: " + e.getMessage());
        }
    }

    private void clearFields() {
        studentComboBox.setSelectedIndex(-1);
        slotComboBox.setSelectedIndex(-1);
        dateSpinner.setValue(new java.util.Date());
        statusCheckBox.setSelected(false);
        markedByComboBox.setSelectedIndex(-1);
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

            // Load slots
            slotComboBox.removeAllItems();
            SlotDAO slotDAO = new SlotDAO();
            List<Slot> slots = slotDAO.getAllSlots();
            for (Slot s : slots) {
                slotComboBox.addItem(s.getSlotId() + " - " + s.getDay() + " " + s.getHour());
            }

            // Load teachers
            markedByComboBox.removeAllItems();
            TeacherDAO teacherDAO = new TeacherDAO();
            List<Teacher> teachers = teacherDAO.getAllTeachers();
            for (Teacher t : teachers) {
                markedByComboBox.addItem(t.getTeacherId() + " - " + t.getName());
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading combo boxes: " + e.getMessage());
        }
    }
}
