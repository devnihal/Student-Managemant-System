package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Timetable;
import util.DatabaseConnection;

public class TimetableDAO {
    public void addTimetable(Timetable timetable) throws SQLException {
        String sql = "INSERT INTO Timetable (name, semester, department) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, timetable.getName());
            stmt.setString(2, timetable.getSemester());
            stmt.setString(3, timetable.getDepartment());
            stmt.executeUpdate();
        }
    }

    public Timetable getTimetableById(int id) throws SQLException {
        String sql = "SELECT * FROM Timetable WHERE timetable_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Timetable timetable = new Timetable();
                timetable.setTimetableId(rs.getInt("timetable_id"));
                timetable.setName(rs.getString("name"));
                timetable.setSemester(rs.getString("semester"));
                timetable.setDepartment(rs.getString("department"));
                timetable.setCreatedAt(rs.getTimestamp("created_at"));
                return timetable;
            }
        }
        return null;
    }

    public List<Timetable> getAllTimetables() throws SQLException {
        List<Timetable> timetables = new ArrayList<>();
        String sql = "SELECT * FROM Timetable";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Timetable timetable = new Timetable();
                timetable.setTimetableId(rs.getInt("timetable_id"));
                timetable.setName(rs.getString("name"));
                timetable.setSemester(rs.getString("semester"));
                timetable.setDepartment(rs.getString("department"));
                timetable.setCreatedAt(rs.getTimestamp("created_at"));
                timetables.add(timetable);
            }
        }
        return timetables;
    }

    public void updateTimetable(Timetable timetable) throws SQLException {
        String sql = "UPDATE Timetable SET name = ?, semester = ?, department = ? WHERE timetable_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, timetable.getName());
            stmt.setString(2, timetable.getSemester());
            stmt.setString(3, timetable.getDepartment());
            stmt.setInt(4, timetable.getTimetableId());
            stmt.executeUpdate();
        }
    }

    public void deleteTimetable(int id) throws SQLException {
        String sql = "DELETE FROM Timetable WHERE timetable_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
