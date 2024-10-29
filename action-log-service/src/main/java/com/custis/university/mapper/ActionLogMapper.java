package com.custis.university.mapper;

import com.custis.university.dto.ActionLogDTO;
import com.custis.university.model.ActionLog;

public class ActionLogMapper {

    public static ActionLogDTO toDTO(ActionLog actionLog) {
        return new ActionLogDTO(
                actionLog.getId(),
                actionLog.getAction(),
                actionLog.getStudentId(),
                actionLog.getTimestamp()
        );
    }

    public static ActionLog toEntity(ActionLogDTO actionLogDTO) {
        ActionLog actionLog = new ActionLog();
        actionLog.setId(actionLogDTO.getId());
        actionLog.setAction(actionLogDTO.getAction());
        actionLog.setStudentId(actionLogDTO.getStudentId());
        actionLog.setTimestamp(actionLogDTO.getTimestamp());
        return actionLog;
    }
}
