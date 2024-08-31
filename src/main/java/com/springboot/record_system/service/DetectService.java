package com.springboot.record_system.service;

import com.springboot.record_system.dto.DetectDTO;
import com.springboot.record_system.model.DetectLog;
import com.springboot.record_system.repository.DetectLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DetectService {
    private final DetectLogRepository detectLogRepository;

    public DetectService(DetectLogRepository detectLogRepository) {
        this.detectLogRepository = detectLogRepository;
    }

    public void updateRecord(DetectDTO detectDTO, String fileLocation) {
        DetectLog detectLog = new DetectLog();
        detectLog.setBtnClicked(detectDTO.getBtnClicked());
        detectLog.setUrl(detectDTO.getUrl());
        detectLog.setIpAddress(detectDTO.getIpAddress());
        detectLog.setKeyPressed(detectDTO.getKeyPressed());
        LocalDateTime logTime = LocalDateTime.now();
        detectLog.setLogTime(logTime);
        detectLog.setFileLocation(fileLocation);
        detectLogRepository.save(detectLog);
    }
}
