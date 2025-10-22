package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Result;
import util.DatabaseConnection;

public class ResultDAO {
    public void addResult(Result result) throws SQLException {
        String sql = "INSERT INTO Result (student_id, course_id, marks, grade) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, result.getStudentId());
            stmt.setInt(2, result.getCourseId());
            if (result.getMarks() != null) {
                stmt.setDouble(3, result.getMarks());
            } else {
                stmt.setNull(3, Types.DOUBLE);
            }
            stmt.setString(4, result.getGrade());
            stmt.executeUpdate();
        }
    }

    public Result getResultByStudentAndCourse(int studentId, int courseId) throws SQLException {
        String sql = "SELECT * FROM Result WHERE student_id = ? AND course_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            stmt.setInt(2, courseId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Result result = new Result();
                result.setResultId(rs.getInt("result_id"));
                result.setStudentId(rs.getInt("student_id"));
                result.setCourseId(rs.getInt("course_id"));
                result.setMarks(rs.getDouble("marks"));
                result.setGrade(rs.getString("grade"));
                result.setCreatedAt(rs.getTimestamp("created_at"));
                return result;
            }
        }
        return null;
    }

    public List<Result> getResultsByStudent(int studentId) throws SQLException {
        List<Result> results = new ArrayList<>();
        String sql = "SELECT * FROM Result WHERE student_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Result result = new Result();
                result.setResultId(rs.getInt("result_id"));
                result.setStudentId(rs.getInt("student_id"));
                result.setCourseId(rs.getInt("course_id"));
                result.setMarks(rs.getDouble("marks"));
                result.setGrade(rs.getString("grade"));
                result.setCreatedAt(rs.getTimestamp("created_at"));
                results.add(result);
            }
        }
        return results;
    }

    public void updateResult(Result result) throws SQLException {
        String sql = "UPDATE Result SET marks = ?, grade = ? WHERE result_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            if (result.getMarks() != null) {
                stmt.setDouble(1, result.getMarks());
            } else {
                stmt.setNull(1, Types.DOUBLE);
            }
            stmt.setString(2, result.getGrade());
            stmt.setInt(3, result.getResultId());
            stmt.executeUpdate();
        }
    }

    public List<Result> getAllResults() throws SQLException {
        List<Result> results = new ArrayList<>();
        String sql = "SELECT * FROM Result";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Result result = new Result();
                result.setResultId(rs.getInt("result_id"));
                result.setStudentId(rs.getInt("student_id"));
                result.setCourseId(rs.getInt("course_id"));
                result.setMarks(rs.getDouble("marks"));
                result.setGrade(rs.getString("grade"));
                result.setCreatedAt(rs.getTimestamp("created_at"));
                results.add(result);
            }
        }
        return results;
    }
}
