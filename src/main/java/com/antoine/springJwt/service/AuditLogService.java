package com.antoine.springJwt.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.antoine.springJwt.model.AuditLog;
import com.antoine.springJwt.model.User;
import com.antoine.springJwt.repository.AuditLogRepository;

@Service
public class AuditLogService {
    @Autowired
    private AuditLogRepository auditLogRepository;

    public void log(String action, String entity, String description, User user) {
        AuditLog log = new AuditLog();
        log.setAction(action);
        log.setEntity(entity);
        log.setDescription(description);
        log.setTimestamp(LocalDateTime.now());
        log.setUser(user);
        auditLogRepository.save(log);
        System.out.println(log);
    }

    public List<AuditLog> getAllLogs() {
        return auditLogRepository.findAllByOrderByTimestampDesc();
    }
}

