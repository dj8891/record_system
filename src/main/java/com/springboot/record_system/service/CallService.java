package com.springboot.record_system.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.springboot.record_system.repository.CallLogRepository;
import com.springboot.record_system.model.CallLog;
import com.springboot.record_system.dto.SearchCriteria;

@Service
public class CallService {

    private final CallLogRepository callLogRepository;

    public CallService(CallLogRepository callLogRepository) {
        this.callLogRepository = callLogRepository;
    }

    public List<CallLog> searchLogs(SearchCriteria criteria) {
        List<CallLog> logs = callLogRepository.findAll();

        if (criteria.getSearchTerm() != null && !criteria.getSearchTerm().isEmpty()) {
            logs = logs.stream()
                    .filter(log -> log.getDetails().contains(criteria.getSearchTerm()) ||
                            log.getCaller().contains(criteria.getSearchTerm()))
                    .collect(Collectors.toList());
        }

        if (criteria.getCallers() != null && !criteria.getCallers().isEmpty()) {
            logs = logs.stream()
                    .filter(log -> criteria.getCallers().contains(log.getCaller()))
                    .collect(Collectors.toList());
        }

        // Add more filters based on criteria (e.g., date range, duration, etc.)

        return logs;
    }
}
