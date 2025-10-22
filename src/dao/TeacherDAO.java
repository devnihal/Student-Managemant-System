package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Teacher;
import util.DatabaseConnection;

public class TeacherDAO {
    public void addTeacher(Teacher teacher) throws SQLException {
        String sql = "INSERT INTO Teacher (name, email, department) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, teacher.getName());
            stmt.setString(2, teacher.getEmail());
            stmt.setString(3, teacher.getDepartment());
            stmt.executeUpdate();
        }
    }

    public Teacher getTeacherById(int id) throws SQLException {
        String sql = "SELECT * FROM Teacher WHERE teacher_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Teacher teacher = new Teacher();
                teacher.setTeacherId(rs.getInt("teacher_id"));
                teacher.setName(rs.getString("name"));
                teacher.setEmail(rs.getString("email"));
                teacher.setDepartment(rs.getString("department"));
                teacher.setCreatedAt(rs.getTimestamp("created_at"));
                return teacher;
            }
        }
        return null;
    }

    public List<Teacher> getAllTeachers() throws SQLException {
        List<Teacher> teachers = new ArrayList<>();
        String sql = "SELECT * FROM Teacher";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Teacher teacher = new Teacher();
                teacher.setTeacherId(rs.getInt("teacher_id"));
                teacher.setName(rs.getString("name"));
                teacher.setEmail(rs.getString("email"));
                teacher.setDepartment(rs.getString("department"));
                teacher.setCreatedAt(rs.getTimestamp("created_at"));
                teachers.add(teacher);
            }
        }
        return teachers;
    }

    public void updateTeacher(Teacher teacher) throws SQLException {
        String sql = "UPDATE Teacher SET name = ?, email = ?, department = ? WHERE teacher_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, teacher.getName());
            stmt.setString(2, teacher.getEmail());
            stmt.setString(3, teacher.getDepartment());
            stmt.setInt(4, teacher.getTeacherId());
            stmt.executeUpdate();
        }
    }

    public void deleteTeacher(int id) throws SQLException {
        String sql = "DELETE FROM Teacher WHERE teacher_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
