package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.StudentCourse;
import util.DatabaseConnection;

public class StudentCourseDAO {
    public void enrollStudent(StudentCourse enrollment) throws SQLException {
        String sql = "INSERT INTO StudentCourse (student_id, course_id) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, enrollment.getStudentId());
            stmt.setInt(2, enrollment.getCourseId());
            stmt.executeUpdate();
        }
    }



    public void unenrollStudent(int studentId, int courseId) throws SQLException {
        String sql = "DELETE FROM StudentCourse WHERE student_id = ? AND course_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            stmt.setInt(2, courseId);
            stmt.executeUpdate();
        }
    }

    public List<StudentCourse> getAllEnrollments() throws SQLException {
        List<StudentCourse> enrollments = new ArrayList<>();
        String sql = "SELECT * FROM StudentCourse";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                StudentCourse enrollment = new StudentCourse();
                enrollment.setStudentCourseId(rs.getInt("student_course_id"));
                enrollment.setStudentId(rs.getInt("student_id"));
                enrollment.setCourseId(rs.getInt("course_id"));
                enrollment.setEnrolledAt(rs.getTimestamp("enrolled_at"));
                enrollments.add(enrollment);
            }
        }
        return enrollments;
    }
}
