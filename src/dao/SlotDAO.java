package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Slot;
import util.DatabaseConnection;

public class SlotDAO {
    public void addSlot(Slot slot) throws SQLException {
        String sql = "INSERT INTO Slot (timetable_id, day, hour, course_id, teacher_id, location) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, slot.getTimetableId());
            stmt.setString(2, slot.getDay());
            stmt.setInt(3, slot.getHour());
            stmt.setInt(4, slot.getCourseId());
            stmt.setInt(5, slot.getTeacherId());
            stmt.setString(6, slot.getLocation());
            stmt.executeUpdate();
        }
    }

    public Slot getSlotById(int id) throws SQLException {
        String sql = "SELECT * FROM Slot WHERE slot_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Slot slot = new Slot();
                slot.setSlotId(rs.getInt("slot_id"));
                slot.setTimetableId(rs.getInt("timetable_id"));
                slot.setDay(rs.getString("day"));
                slot.setHour(rs.getInt("hour"));
                slot.setCourseId(rs.getInt("course_id"));
                slot.setTeacherId(rs.getInt("teacher_id"));
                slot.setLocation(rs.getString("location"));
                slot.setCreatedAt(rs.getTimestamp("created_at"));
                return slot;
            }
        }
        return null;
    }

    public List<Slot> getSlotsByTimetable(int timetableId) throws SQLException {
        List<Slot> slots = new ArrayList<>();
        String sql = "SELECT * FROM Slot WHERE timetable_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, timetableId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Slot slot = new Slot();
                slot.setSlotId(rs.getInt("slot_id"));
                slot.setTimetableId(rs.getInt("timetable_id"));
                slot.setDay(rs.getString("day"));
                slot.setHour(rs.getInt("hour"));
                slot.setCourseId(rs.getInt("course_id"));
                slot.setTeacherId(rs.getInt("teacher_id"));
                slot.setLocation(rs.getString("location"));
                slot.setCreatedAt(rs.getTimestamp("created_at"));
                slots.add(slot);
            }
        }
        return slots;
    }

    public void updateSlot(Slot slot) throws SQLException {
        String sql = "UPDATE Slot SET timetable_id = ?, day = ?, hour = ?, course_id = ?, teacher_id = ?, location = ? WHERE slot_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, slot.getTimetableId());
            stmt.setString(2, slot.getDay());
            stmt.setInt(3, slot.getHour());
            stmt.setInt(4, slot.getCourseId());
            stmt.setInt(5, slot.getTeacherId());
            stmt.setString(6, slot.getLocation());
            stmt.setInt(7, slot.getSlotId());
            stmt.executeUpdate();
        }
    }

    public void deleteSlot(int id) throws SQLException {
        String sql = "DELETE FROM Slot WHERE slot_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<Slot> getAllSlots() throws SQLException {
        List<Slot> slots = new ArrayList<>();
        String sql = "SELECT * FROM Slot";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Slot slot = new Slot();
                slot.setSlotId(rs.getInt("slot_id"));
                slot.setTimetableId(rs.getInt("timetable_id"));
                slot.setDay(rs.getString("day"));
                slot.setHour(rs.getInt("hour"));
                slot.setCourseId(rs.getInt("course_id"));
                slot.setTeacherId(rs.getInt("teacher_id"));
                slot.setLocation(rs.getString("location"));
                slot.setCreatedAt(rs.getTimestamp("created_at"));
                slots.add(slot);
            }
        }
        return slots;
    }
}
