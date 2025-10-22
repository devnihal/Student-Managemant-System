

package model;

import java.sql.Timestamp;

public class Course {
    private int courseId;
    private String courseName;
    private int credits;
    private String description;
    private Timestamp createdAt;

    public Course() {}

    public Course(String courseName, int credits, String description) {
        this.courseName = courseName;
        this.credits = credits;
        this.description = description;
    }

    // Getters and Setters
    public int getCourseId() { return courseId; }
    public void setCourseId(int courseId) { this.courseId = courseId; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public int getCredits() { return credits; }
    public void setCredits(int credits) { this.credits = credits; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "Course{" +
                "courseId=" + courseId +
                ", courseName='" + courseName + '\'' +
                ", credits=" + credits +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
