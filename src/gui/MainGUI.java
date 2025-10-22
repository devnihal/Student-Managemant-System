package gui;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class MainGUI extends JFrame {
    private JTabbedPane tabbedPane;

    public MainGUI() {
        setTitle("Student Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        tabbedPane = new JTabbedPane();

        // Add tabs for each entity
        tabbedPane.addTab("Students", new StudentPanel());
        tabbedPane.addTab("Teachers", new TeacherPanel());
        tabbedPane.addTab("Courses", new CoursePanel());
        tabbedPane.addTab("Enrollments", new EnrollmentPanel());
        tabbedPane.addTab("Timetables", new TimetablePanel());
        tabbedPane.addTab("Slots", new SlotPanel());
        tabbedPane.addTab("Attendance", new AttendancePanel());
        tabbedPane.addTab("Results", new ResultPanel());

        add(tabbedPane);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainGUI().setVisible(true);
        });
    }
}
