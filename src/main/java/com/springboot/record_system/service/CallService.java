package com.springboot.record_system.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.record_system.repository.CallLogRepository;
import com.springboot.record_system.model.CallLog;
import com.springboot.record_system.dto.SearchCriteria;

@Service
public class CallService {

    @Autowired
    private final CallLogRepository callLogRepository;

    public CallService(CallLogRepository callLogRepository) {
        this.callLogRepository = callLogRepository;
    }

    public List<CallLog> searchLogs(SearchCriteria criteria) {
        List<CallLog> logs = callLogRepository.findAll();

        if (criteria.getSearchTerm() != null && !criteria.getSearchTerm().isEmpty()) {
            logs = logs.stream()
                    .filter(log -> log.getClient().contains(criteria.getSearchTerm()) ||
                            log.getCaller().contains(criteria.getSearchTerm()) ||
                            log.getCompany().contains(criteria.getSearchTerm()) ||
                            log.getCaption().contains(criteria.getSearchTerm()))
                    .collect(Collectors.toList());
        }

        if (criteria.getCallers() != null && !criteria.getCallers().isEmpty()) {
            logs = logs.stream()
                    .filter(log -> criteria.getCallers().contains(log.getCaller()))
                    .collect(Collectors.toList());
        }

        // Filter by date range using fromTime and toTime
        if (criteria.getFromTime() != null && criteria.getToTime() != null) {
            logs = logs.stream()
                    .filter(log -> log.getFromTime().isBefore(criteria.getToTime()) &&
                            log.getToTime().isAfter(criteria.getFromTime()))
                    .collect(Collectors.toList());
        }

        return logs;
    }
}
