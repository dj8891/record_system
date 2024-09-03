package com.springboot.record_system.service;

import com.springboot.record_system.dto.RecordDTO;
import com.springboot.record_system.model.DetectLog;
import com.springboot.record_system.model.VideoLog;
import com.springboot.record_system.repository.DetectLogRepository;
import com.springboot.record_system.repository.VideoLogRepository;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class VideoService {

    private final VideoLogRepository videoLogRepository;
    private final DetectLogRepository detectLogRepository;

    public VideoService(VideoLogRepository videoLogRepository, DetectLogRepository detectLogRepository) {
        this.videoLogRepository = videoLogRepository;
        this.detectLogRepository = detectLogRepository;
    }

    private Date converDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
    public List<VideoLog> getVideo(String date, String name) throws IOException, InterruptedException {
        return new ArrayList<>();
    }
}
