package com.custis.university.dto;

import java.time.ZonedDateTime;

public class ActionLogDTO {

    private String id;
    private String action;
    private String studentId;
    private ZonedDateTime timestamp;

    public ActionLogDTO() {
    }

    public ActionLogDTO(String id, String action, String studentId, ZonedDateTime timestamp) {
        this.id = id;
        this.action = action;
        this.studentId = studentId;
        this.timestamp = timestamp;
    }

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
    public String toString() {
        return "ActionLogDTO{" +
                "id='" + id + '\'' +
                ", action='" + action + '\'' +
                ", studentId='" + studentId + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
