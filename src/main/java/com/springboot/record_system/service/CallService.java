package com.springboot.record_system.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.springboot.record_system.model.IPSetting;
import com.springboot.record_system.repository.IPRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.record_system.repository.CallLogRepository;
import com.springboot.record_system.model.CallLog;
import com.springboot.record_system.dto.SearchCriteria;

@Service
public class CallService {

    @Autowired
    private final CallLogRepository callLogRepository;

    @Autowired
    private final IPRepository ipRepository;

    public CallService(CallLogRepository callLogRepository, IPRepository ipRepository) {
        this.callLogRepository = callLogRepository;
        this.ipRepository = ipRepository;
    }

    public List<CallLog> searchLogs(SearchCriteria criteria) {
        List<CallLog> logs = callLogRepository.findAll().stream()
                .peek(log -> {
                    Optional<IPSetting> ipAddressOpt = ipRepository.findOneByIpAddress(log.getIpAddress());
                    String username = ipAddressOpt.map(IPSetting::getUserName).orElse("Unknown");
                    log.setUserName(username);
                })
                .collect(Collectors.toList());

        if (criteria.getSearchTerm() != null && !criteria.getSearchTerm().isEmpty()) {
            logs = logs.stream()
                    .filter(log -> log.getClient().contains(criteria.getSearchTerm()) ||
                            log.getUserName().contains(criteria.getSearchTerm()) ||
                            log.getCompany().contains(criteria.getSearchTerm()) ||
                            log.getCaption().contains(criteria.getSearchTerm()))
                    .collect(Collectors.toList());
        }

        if (criteria.getCallers() != null && !criteria.getCallers().isEmpty()) {
            logs = logs.stream()
                    .filter(log -> criteria.getCallers().contains(log.getUserName()))
                    .collect(Collectors.toList());
        }

        // Filter by date range using fromTime and toTime
        if (criteria.getQueryTime() != null) {
            logs = logs.stream()
                    .filter(log -> log.getFromTime().isBefore(criteria.getQueryTime()) &&
                            log.getToTime().isAfter(criteria.getQueryTime()))
                    .collect(Collectors.toList());
        }

        return logs;
    }
}
