package com.custis.university.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.ZonedDateTime;
import java.util.Objects;

@Document(collection = "action_logs")
public class ActionLog {

    @Id
    private String id;
    private String action;
    private String studentId;
    private ZonedDateTime timestamp;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActionLog actionLog = (ActionLog) o;
        return Objects.equals(id, actionLog.id) && Objects.equals(studentId, actionLog.studentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, studentId);
    }
}
