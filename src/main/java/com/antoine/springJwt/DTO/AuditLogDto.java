package com.antoine.springJwt.dto;

import java.time.LocalDateTime;

public class AuditLogDto {
    private LocalDateTime timestamp;
    private String user;
    private String entity;
    private String action;
    private String description;

    public AuditLogDto(LocalDateTime timestamp, String user, String entity, String action, String description) {
        this.timestamp = timestamp;
        this.user = user;
        this.entity = entity;
        this.action = action;
        this.description = description;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    
}
