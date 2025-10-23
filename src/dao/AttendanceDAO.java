package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Attendance;
import util.DatabaseConnection;

public class AttendanceDAO {
    public void markAttendance(Attendance attendance) throws SQLException {
        String sql = "INSERT INTO Attendance (student_id, course_id, slot_id, date, status, marked_by) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, attendance.getStudentId());
            stmt.setInt(2, attendance.getCourseId());
            stmt.setInt(3, attendance.getSlotId());
            stmt.setDate(4, attendance.getDate());
            stmt.setBoolean(5, attendance.isStatus());
            if (attendance.getMarkedBy() != null) {
                stmt.setInt(6, attendance.getMarkedBy());
            } else {
                stmt.setNull(6, Types.INTEGER);
            }
            stmt.executeUpdate();
        }
    }



    public List<Attendance> getAttendanceBySlot(int slotId, Date date) throws SQLException {
        List<Attendance> attendances = new ArrayList<>();
        String sql = "SELECT * FROM Attendance WHERE slot_id = ? AND date = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, slotId);
            stmt.setDate(2, date);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Attendance attendance = new Attendance();
                attendance.setAttendanceId(rs.getInt("attendance_id"));
                attendance.setStudentId(rs.getInt("student_id"));
                attendance.setCourseId(rs.getInt("course_id"));
                attendance.setSlotId(rs.getInt("slot_id"));
                attendance.setDate(rs.getDate("date"));
                attendance.setStatus(rs.getBoolean("status"));
                attendance.setMarkedBy(rs.getInt("marked_by"));
                attendance.setMarkedAt(rs.getTimestamp("marked_at"));
                attendances.add(attendance);
            }
        }
        return attendances;
    }

    public void updateAttendance(Attendance attendance) throws SQLException {
        String sql = "UPDATE Attendance SET status = ?, marked_by = ? WHERE attendance_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, attendance.isStatus());
            if (attendance.getMarkedBy() != null) {
                stmt.setInt(2, attendance.getMarkedBy());
            } else {
                stmt.setNull(2, Types.INTEGER);
            }
            stmt.setInt(3, attendance.getAttendanceId());
            stmt.executeUpdate();
        }
    }

    public List<Attendance> getAllAttendances() throws SQLException {
        List<Attendance> attendances = new ArrayList<>();
        String sql = "SELECT * FROM Attendance";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Attendance attendance = new Attendance();
                attendance.setAttendanceId(rs.getInt("attendance_id"));
                attendance.setStudentId(rs.getInt("student_id"));
                attendance.setCourseId(rs.getInt("course_id"));
                attendance.setSlotId(rs.getInt("slot_id"));
                attendance.setDate(rs.getDate("date"));
                attendance.setStatus(rs.getBoolean("status"));
                attendance.setMarkedBy(rs.getInt("marked_by"));
                attendance.setMarkedAt(rs.getTimestamp("marked_at"));
                attendances.add(attendance);
            }
        }
        return attendances;
    }
}
