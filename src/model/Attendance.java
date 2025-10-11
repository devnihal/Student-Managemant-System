package studentmanagement.model;

import java.sql.Timestamp;
import java.sql.Date;

public class Attendance {
    private int attendanceId;
    private int studentId;
    private int courseId;
    private int slotId;
    private Date date;
    private boolean status;
    private int markedBy;
    private Timestamp markedAt;

    public Attendance() {}

    public Attendance(int attendanceId, int studentId, int courseId, int slotId, Date date, boolean status, int markedBy, Timestamp markedAt) {
        this.attendanceId = attendanceId;
        this.studentId = studentId;
        this.courseId = courseId;
        this.slotId = slotId;
        this.date = date;
        this.status = status;
        this.markedBy = markedBy;
        this.markedAt = markedAt;
    }

    public int getAttendanceId() { return attendanceId; }
    public void setAttendanceId(int attendanceId) { this.attendanceId = attendanceId; }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public int getCourseId() { return courseId; }
    public void setCourseId(int courseId) { this.courseId = courseId; }

    public int getSlotId() { return slotId; }
    public void setSlotId(int slotId) { this.slotId = slotId; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public boolean isStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }

    public int getMarkedBy() { return markedBy; }
    public void setMarkedBy(int markedBy) { this.markedBy = markedBy; }

    public Timestamp getMarkedAt() { return markedAt; }
    public void setMarkedAt(Timestamp markedAt) { this.markedAt = markedAt; }

    @Override
    public String toString() {
        return "Attendance [id=" + attendanceId + ", studentId=" + studentId + ", courseId=" + courseId + ", status=" + status + "]";
    }
}
