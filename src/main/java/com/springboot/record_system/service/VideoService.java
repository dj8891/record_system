package com.springboot.record_system.service;

import com.springboot.record_system.model.VideoLog;
import com.springboot.record_system.repository.VideoLogRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Service
public class VideoService {

    private final VideoLogRepository videoLogRepository;
    private final UtilityService utilityService;

    public VideoService(VideoLogRepository videoLogRepository, UtilityService utilityService) {
        this.videoLogRepository = videoLogRepository;
        this.utilityService = utilityService;
    }

    private Date converDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
    public List<VideoLog> getVideo(String date, String name) throws IOException, InterruptedException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);
        // Convert String to LocalDateTime
        LocalDateTime fromDateTime = LocalDateTime.of(localDate.getYear(), localDate.getMonth(), localDate.getDayOfMonth(), 0, 0, 0);
        LocalDateTime toDateTime = LocalDateTime.of(localDate.getYear(), localDate.getMonth(), (localDate.getDayOfMonth() + 1), 0, 0, 0);
        Date fromDate = utilityService.convertLocalDateTimeToUtc(fromDateTime);
        Date toDate = utilityService.convertLocalDateTimeToUtc(toDateTime);
        return videoLogRepository.findByNameAndFromDateBetween(name, fromDate, toDate);
    }
}
