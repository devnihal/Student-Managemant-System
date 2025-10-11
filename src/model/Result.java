package studentmanagement.model;

import java.sql.Timestamp;

public class Result {
    private int resultId;
    private int studentId;
    private int courseId;
    private double marks;
    private String grade;
    private Timestamp createdAt;

    public Result() {}

    public Result(int resultId, int studentId, int courseId, double marks, String grade, Timestamp createdAt) {
        this.resultId = resultId;
        this.studentId = studentId;
        this.courseId = courseId;
        this.marks = marks;
        this.grade = grade;
        this.createdAt = createdAt;
    }

    public int getResultId() { return resultId; }
    public void setResultId(int resultId) { this.resultId = resultId; }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public int getCourseId() { return courseId; }
    public void setCourseId(int courseId) { this.courseId = courseId; }

    public double getMarks() { return marks; }
    public void setMarks(double marks) { this.marks = marks; }

    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "Result [id=" + resultId + ", studentId=" + studentId + ", courseId=" + courseId + ", grade=" + grade + "]";
    }
}
