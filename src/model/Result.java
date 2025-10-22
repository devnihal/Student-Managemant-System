

package model;

import java.sql.Timestamp;

public class Result {
    private int resultId;
    private int studentId;
    private int courseId;
    private Double marks;
    private String grade;
    private Timestamp createdAt;

    public Result() {}

    public Result(int studentId, int courseId, Double marks, String grade) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.marks = marks;
        this.grade = grade;
    }

    // Getters and Setters
    public int getResultId() { return resultId; }
    public void setResultId(int resultId) { this.resultId = resultId; }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public int getCourseId() { return courseId; }
    public void setCourseId(int courseId) { this.courseId = courseId; }

    public Double getMarks() { return marks; }
    public void setMarks(Double marks) { this.marks = marks; }

    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "Result{" +
                "resultId=" + resultId +
                ", studentId=" + studentId +
                ", courseId=" + courseId +
                ", marks=" + marks +
                ", grade='" + grade + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
