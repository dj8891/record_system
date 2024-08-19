package com.springboot.record_system.service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.springboot.record_system.model.IPSetting;
import com.springboot.record_system.repository.IPRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.springboot.record_system.dto.RecordDTO;
import org.springframework.stereotype.Service;

import com.springboot.record_system.repository.CallLogRepository;
import com.springboot.record_system.model.CallLog;
import com.springboot.record_system.dto.SearchCriteria;

@Service
public class CallService {

    private final CallLogRepository callLogRepository;

    @Autowired
    private final IPRepository ipRepository;

    public CallService(CallLogRepository callLogRepository, IPRepository ipRepository) {
        this.callLogRepository = callLogRepository;
        this.ipRepository = ipRepository;
    }

    public Optional<CallLog> getLogById(String id) {
        return callLogRepository.findById(id);
    }

    public List<CallLog> searchLogs(SearchCriteria criteria) {
        List<CallLog> logs = callLogRepository.findAll().stream()
                .peek(log -> {
                    Optional<IPSetting> ipAddressOpt = ipRepository.findOneByIpAddress(log.getIpAddress());
                    String username = ipAddressOpt.map(IPSetting::getUserName).orElse("Unknown");
                    log.setUserName(username);
                })
                .sorted(Comparator.comparing(CallLog::getFromTime).reversed())
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

    public CallLog createRecord(RecordDTO recordDTO) {
        CallLog callLog = new CallLog();
        callLog.setClient(recordDTO.getClientName());
        callLog.setFromTime(recordDTO.getFromDateTime());
        callLog.setToTime(recordDTO.getCurrentDateTime());
        callLog.setCompany(recordDTO.getCompany());
        boolean gender = recordDTO.getGender().equals("male");
        callLog.setGender(gender);
        callLog.setIpAddress(recordDTO.getIpAddress());
        callLog.setNationality(recordDTO.getNationality());
        return callLogRepository.save(callLog);
    }

    public CallLog updateRecord(String id, boolean isVideo, String fileLocation) {
        CallLog callLog = callLogRepository.findById(id).orElseThrow(() -> new RuntimeException("Document not found"));
        callLog.setVideo(isVideo);
        callLog.setFileLocation(fileLocation);
        return callLogRepository.save(callLog);
    }
}
