package com.springboot.record_system.service;

import com.springboot.record_system.model.DetectLog;
import com.springboot.record_system.model.VideoLog;
import com.springboot.record_system.repository.DetectLogRepository;
import com.springboot.record_system.repository.VideoLogRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class VideoService {

    private final VideoLogRepository videoLogRepository;
    private final DetectLogRepository detectLogRepository;
    private final UtilityService utilityService;

    public VideoService(VideoLogRepository videoLogRepository, DetectLogRepository detectLogRepository, UtilityService utilityService) {
        this.videoLogRepository = videoLogRepository;
        this.detectLogRepository = detectLogRepository;
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
        List<VideoLog> videos = videoLogRepository.findByNameAndFromDateBetween(name, fromDate, toDate).stream()
                .peek(log -> {
                    List<DetectLog> detectLogs = detectLogRepository.findByIpAddressAndLogTimeBetweenOrderByLogTimeAsc(log.getIpAddress(), log.getFromDate(), log.getToDate());
                    AtomicInteger keyCnt = new AtomicInteger();
                    AtomicInteger mouseCnt = new AtomicInteger();
                    List<String> urls = new ArrayList<>();
                    String previousUrl = "";
                    if (!detectLogs.isEmpty()) {
                        for (DetectLog detectLog : detectLogs) {
                            if (detectLog.isKeyPressed()) keyCnt.getAndIncrement();
                            if (detectLog.isBtnClicked()) mouseCnt.getAndIncrement();

                            String curUrl = detectLog.getUrl();
                            if (curUrl != null && !curUrl.equals(previousUrl) && !urls.contains(curUrl)) {
                                urls.add(curUrl);
                                previousUrl = curUrl;
                            }
                        }
                    }
                    log.setKeyCount(keyCnt.get());
                    log.setMouseCount(mouseCnt.get());
                    log.setUrls(urls);
                }).collect(Collectors.toList());
        return videos;
    }
}
