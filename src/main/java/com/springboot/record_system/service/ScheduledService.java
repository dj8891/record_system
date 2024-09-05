package com.springboot.record_system.service;

import com.springboot.record_system.dto.RecordDTO;
import com.springboot.record_system.model.DetectLog;
import com.springboot.record_system.model.IPSetting;
import com.springboot.record_system.model.VideoLog;
import com.springboot.record_system.repository.DetectLogRepository;
import com.springboot.record_system.repository.IPRepository;
import com.springboot.record_system.repository.VideoLogRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Paths;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

@Service
public class ScheduledService {

    private final IPRepository ipRepository;
    private final DetectLogRepository detectLogRepository;
    private final VideoLogRepository videoLogRepository;
    private final ThreadPoolTaskScheduler threadPoolTaskScheduler;
    private final int userCountPerThread = 5;
    private final UtilityService utilityService;

    public ScheduledService(IPRepository ipRepository,
                            DetectLogRepository detectLogRepository,
                            VideoLogRepository videoLogRepository,
                            ThreadPoolTaskScheduler threadPoolTaskScheduler,
                            UtilityService utilityService) {
        this.ipRepository = ipRepository;
        this.detectLogRepository = detectLogRepository;
        this.videoLogRepository = videoLogRepository;
        this.threadPoolTaskScheduler = threadPoolTaskScheduler;
        this.utilityService = utilityService;
    }
    @PostConstruct
    public void onStartup() {
        startSchedule();
    }
    public void startSchedule() {
        RecordDTO recordDTO = new RecordDTO();
        // Create a LocalDateTime object with the specific value
        LocalDateTime localTime = recordDTO.getCurrentDateTime();
        int currentMin = localTime.getMinute();
        int nearMin = 10 * ((currentMin / 10) + 1);
        LocalDateTime detectTime = null;
        if(nearMin == 60) {
            detectTime = LocalDateTime.of(localTime.getYear(), localTime.getMonth(), localTime.getDayOfMonth(), localTime.getHour() + 1, 0, 0);
        } else detectTime = LocalDateTime.of(localTime.getYear(), localTime.getMonth(), localTime.getDayOfMonth(), localTime.getHour(), nearMin, 0);
        long difSeconds = Duration.between(localTime, detectTime).getSeconds();
        ScheduledFuture<?> firstScheduledFuture = threadPoolTaskScheduler.scheduleAtFixedRate(this::firstTask, Instant.now().plusSeconds(difSeconds), Duration.ofSeconds(600000));
        ScheduledFuture<?> secondScheduledFuture = threadPoolTaskScheduler.scheduleAtFixedRate(this::secondTask, Instant.now().plusSeconds(difSeconds), Duration.ofSeconds(600000));
    }

    @Async
    protected void processSchedule(Pageable pageable) {
        List<IPSetting> userList = ipRepository.findByCustomQuery(pageable);
        userList.forEach(user -> {
                    String ipAddress = user.getIpAddress();
                    String userName = user.getUserName();
                    try {
                        Thread.sleep(1000); // Sleep for 1 second
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e.getMessage());
                    }
                    RecordDTO recordDTO = new RecordDTO();
                    // Create a LocalDateTime object with the specific value
                    LocalDateTime dateTime = recordDTO.getCurrentDateTime();
                    LocalDateTime beforeDateTime = dateTime.minusMinutes(10);
                    Date currentDate = utilityService.convertLocalDateTimeToUtc(dateTime);
                    Date beforeDate = utilityService.convertLocalDateTimeToUtc(beforeDateTime);
                    List<DetectLog> detectList = detectLogRepository.findByIpAddressAndLogTimeBetweenOrderByLogTimeAsc(ipAddress, beforeDate, currentDate);
                    if(!detectList.isEmpty()) {
                        File imageListFile = new File("src/main/resources/static/upload/detect/", userName + "images.txt");
                        try (BufferedWriter writer = new BufferedWriter(new FileWriter(imageListFile))) {
                            for (DetectLog detectLog : detectList) {
                                File imgFile = new File("src/main/resources/static/" + detectLog.getFileLocation());
                                if (imgFile.exists() && imgFile.length() != 0) {
                                    writer.write("file '" + imgFile.getAbsolutePath() + "'");
                                    writer.newLine();
                                }
                            }
                        } catch (IOException e) {
                            throw new RuntimeException("Error writing image list file", e);
                        }

                        File uploadDir = new File("src/main/resources/static/upload/video/" + userName + "/");
                        if(!uploadDir.exists()) {
                            boolean dir = uploadDir.mkdirs();
                            System.out.println(dir);
                        }

                        // make video name

                        // Define the DateTimeFormatter to format the date part
                        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");

                        // Extract the date part
                        String datePart = dateTime.format(dateFormatter);

                        // Extract the nanoseconds part and format it as a string
                        String nanosecondsPart = String.format("%09d", dateTime.getNano());

                        // Combine the date part and nanoseconds part
                        String result = datePart + "_" + nanosecondsPart;
                        String relativePath = "src/main/resources/static/upload/video/" + userName;
                        String absolutePath = Paths.get(relativePath).toAbsolutePath().toString();

                        ProcessBuilder processBuilder = new ProcessBuilder(
                                "ffmpeg",
                                "-r", "2",
                                "-f", "concat",
                                "-safe", "0",
                                "-i", imageListFile.getAbsolutePath(),
                                "-c:v", "libx264",
                                "-pix_fmt", "yuv420p",
                                absolutePath + "/" + result + ".mp4"
                        );
                        processBuilder.redirectErrorStream(true);
                        try {
                            Process process = processBuilder.start();
                            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                            String line;
                            while ((line = reader.readLine()) != null)
                                System.out.println("taskList: " + line);
                            int exitCode = process.waitFor();
                            if(exitCode == 0) {
                                boolean cleared = imageListFile.delete();
                                // save video data to database
                                VideoLog videoLog = new VideoLog(beforeDate, currentDate, "upload/video/" + userName + "/" + result + ".mp4", ipAddress, userName);
                                videoLogRepository.save(videoLog);

                                for(DetectLog detectLog: detectList) {
                                    File newFile = new File("src/main/resources/static/" + detectLog.getFileLocation());
                                    if(newFile.exists()) {
                                        boolean isDelete = newFile.delete();
                                    }
                                }

                                // delete detect data used to make video from database
                                detectLogRepository.deleteAll(detectList);
                            } else {
                                System.out.println("Video creation failed with code " + exitCode);
                            }
                        } catch (IOException | InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
    }

    private void firstTask() {
        System.out.println("Task One - Thread: " + Thread.currentThread().getName());
        Pageable pageable = PageRequest.of(0, userCountPerThread);
        processSchedule(pageable);
    }

    private void secondTask() {
        System.out.println("Task Two - Thread: " + Thread.currentThread().getName());
        Pageable pageable = PageRequest.of(1, userCountPerThread);
        processSchedule(pageable);
    }
}
