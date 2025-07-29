package com.antoine.springJwt.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.antoine.springJwt.dto.AuditLogDto;
import com.antoine.springJwt.service.AuditLogService;

@CrossOrigin(origins = "http://localhost:5173/")
@RestController
@RequestMapping("/api/audit-logs")
public class AuditLogController {
    private final AuditLogService auditLogService;

    public AuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @GetMapping
    public List<AuditLogDto> getAuditLogs() {
        return auditLogService.getAllLogs()
           .stream()
           .map(log -> new AuditLogDto(
            log.getTimestamp(),
            log.getUser().getFirstname() + " " + log.getUser().getLastname(),
            log.getEntity(),
            log.getAction(),
            log.getDescription()
           ))
           .collect(Collectors.toList());

        //    auditLogService.log("Created", "Revenue", "Revenue for July 2025", loggedInUser);

    }
    
}
