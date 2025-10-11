package studentmanagement.model;

import java.sql.Timestamp;

public class Teacher {
    private int teacherId;
    private String name;
    private String email;
    private String department;
    private Timestamp createdAt;

    public Teacher() {}

    public Teacher(int teacherId, String name, String email, String department, Timestamp createdAt) {
        this.teacherId = teacherId;
        this.name = name;
        this.email = email;
        this.department = department;
        this.createdAt = createdAt;
    }

    public int getTeacherId() { return teacherId; }
    public void setTeacherId(int teacherId) { this.teacherId = teacherId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "Teacher [id=" + teacherId + ", name=" + name + ", email=" + email + ", department=" + department + "]";
    }
}
