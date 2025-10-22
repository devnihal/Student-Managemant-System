

package model;

import java.sql.Timestamp;

public class Slot {
    private int slotId;
    private int timetableId;
    private String day;
    private int hour;
    private int courseId;
    private int teacherId;
    private String location;
    private Timestamp createdAt;

    public Slot() {}

    public Slot(int timetableId, String day, int hour, int courseId, int teacherId, String location) {
        this.timetableId = timetableId;
        this.day = day;
        this.hour = hour;
        this.courseId = courseId;
        this.teacherId = teacherId;
        this.location = location;
    }

    // Getters and Setters
    public int getSlotId() { return slotId; }
    public void setSlotId(int slotId) { this.slotId = slotId; }

    public int getTimetableId() { return timetableId; }
    public void setTimetableId(int timetableId) { this.timetableId = timetableId; }

    public String getDay() { return day; }
    public void setDay(String day) { this.day = day; }

    public int getHour() { return hour; }
    public void setHour(int hour) { this.hour = hour; }

    public int getCourseId() { return courseId; }
    public void setCourseId(int courseId) { this.courseId = courseId; }

    public int getTeacherId() { return teacherId; }
    public void setTeacherId(int teacherId) { this.teacherId = teacherId; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "Slot{" +
                "slotId=" + slotId +
                ", timetableId=" + timetableId +
                ", day='" + day + '\'' +
                ", hour=" + hour +
                ", courseId=" + courseId +
                ", teacherId=" + teacherId +
                ", location='" + location + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
