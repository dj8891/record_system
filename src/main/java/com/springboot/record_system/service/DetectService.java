package com.springboot.record_system.service;

import com.springboot.record_system.dto.DetectDTO;
import com.springboot.record_system.dto.RecordDTO;
import com.springboot.record_system.model.DetectLog;
import com.springboot.record_system.repository.DetectLogRepository;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.Date;

@Service
public class DetectService {
    private final DetectLogRepository detectLogRepository;
    private final UtilityService utilityService;

    public DetectService(DetectLogRepository detectLogRepository, UtilityService utilityService) {
        this.detectLogRepository = detectLogRepository;
        this.utilityService = utilityService;
    }

    public void updateRecord(DetectDTO detectDTO, String fileLocation) {
        DetectLog detectLog = new DetectLog();
        detectLog.setBtnClicked(detectDTO.getBtnClicked());
        detectLog.setUrl(detectDTO.getUrl());
        detectLog.setIpAddress(detectDTO.getIpAddress());
        detectLog.setKeyPressed(detectDTO.getKeyPressed());
        RecordDTO recordDTO = new RecordDTO();

        LocalDateTime asiaTime = recordDTO.getCurrentDateTime();
        Date utcTime = utilityService.convertLocalDateTimeToUtc(asiaTime);
        detectLog.setLogTime(utcTime);
        detectLog.setFileLocation(fileLocation);
        detectLogRepository.save(detectLog);
    }
}
