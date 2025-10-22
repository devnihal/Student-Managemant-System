

package model;

import java.sql.Timestamp;

public class StudentCourse {
    private int studentCourseId;
    private int studentId;
    private int courseId;
    private Timestamp enrolledAt;

    public StudentCourse() {}

    public StudentCourse(int studentId, int courseId) {
        this.studentId = studentId;
        this.courseId = courseId;
    }

    // Getters and Setters
    public int getStudentCourseId() { return studentCourseId; }
    public void setStudentCourseId(int studentCourseId) { this.studentCourseId = studentCourseId; }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public int getCourseId() { return courseId; }
    public void setCourseId(int courseId) { this.courseId = courseId; }

    public Timestamp getEnrolledAt() { return enrolledAt; }
    public void setEnrolledAt(Timestamp enrolledAt) { this.enrolledAt = enrolledAt; }

    @Override
    public String toString() {
        return "StudentCourse{" +
                "studentCourseId=" + studentCourseId +
                ", studentId=" + studentId +
                ", courseId=" + courseId +
                ", enrolledAt=" + enrolledAt +
                '}';
    }
}
