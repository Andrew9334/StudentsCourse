package com.custis.university.service;

import com.custis.university.model.ActionLog;
import com.custis.university.repository.ActionLogRepository;

import java.util.List;

public class ActionLogService {

    private final ActionLogRepository actionLogRepository;

    public ActionLogService(ActionLogRepository actionLogRepository) {
        this.actionLogRepository = actionLogRepository;
    }

    public ActionLog logAction(ActionLog actionLog) {
        return actionLogRepository.save(actionLog);
    }

    public List<ActionLog> getAllLogs() {
        return actionLogRepository.findAll();
    }
}