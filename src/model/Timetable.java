

package model;

import java.sql.Timestamp;

public class Timetable {
    private int timetableId;
    private String name;
    private String semester;
    private String department;
    private Timestamp createdAt;

    public Timetable() {}

    public Timetable(String name, String semester, String department) {
        this.name = name;
        this.semester = semester;
        this.department = department;
    }

    // Getters and Setters
    public int getTimetableId() { return timetableId; }
    public void setTimetableId(int timetableId) { this.timetableId = timetableId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "Timetable{" +
                "timetableId=" + timetableId +
                ", name='" + name + '\'' +
                ", semester='" + semester + '\'' +
                ", department='" + department + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
