package com.springboot.record_system.service;

import com.springboot.record_system.dto.RecordDTO;
import com.springboot.record_system.model.DetectLog;
import com.springboot.record_system.model.IPSetting;
import com.springboot.record_system.model.VideoLog;
import com.springboot.record_system.repository.DetectLogRepository;
import com.springboot.record_system.repository.IPRepository;
import com.springboot.record_system.repository.VideoLogRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ScheduledService {

    private final IPRepository ipRepository;
    private final DetectLogRepository detectLogRepository;
    private final VideoLogRepository videoLogRepository;
    private final int userCountPerThread = 5;
    public ScheduledService(IPRepository ipRepository,
                            DetectLogRepository detectLogRepository,
                            VideoLogRepository videoLogRepository) {
        this.ipRepository = ipRepository;
        this.detectLogRepository = detectLogRepository;
        this.videoLogRepository = videoLogRepository;
    }

    @Async("taskExecuter")
    protected void processSchedule(Pageable pageable) {
        List<IPSetting> userList = ipRepository.findByCustomQuery(pageable);
        userList.forEach(user -> {
                    String ipAddress = user.getIpAddress();
                    String userName = user.getUserName();
                    List<DetectLog> detectList = detectLogRepository.findAllByIpAddressOrderByLogTimeAsc(ipAddress);
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
                        RecordDTO recordDTO = new RecordDTO();
                        // Create a LocalDateTime object with the specific value
                        LocalDateTime dateTime = recordDTO.getCurrentDateTime();

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

                        // Execute FFmpeg command using the list of files
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
                                System.out.println("tasklist: " + line);
                            int exitCode = process.waitFor();
                            if(exitCode == 0) {
                                boolean cleared = imageListFile.delete();
                                LocalDateTime startTime = detectList.getFirst().getLogTime();
                                LocalDateTime endTime = detectList.getLast().getLogTime();
                                // save video data to database
                                VideoLog videoLog = new VideoLog(startTime, endTime, "upload/video/" + userName + "/" + result + ".mp4", ipAddress, userName);
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

    @Scheduled(fixedRate = 600000, initialDelay = 600000)
    public void firstTask() {
        System.out.println("Task One - Thread: " + Thread.currentThread().getName());
        Pageable pageable = PageRequest.of(0, userCountPerThread);
        processSchedule(pageable);
    }

    @Scheduled(fixedRate = 600000, initialDelay = 600000)
    public void secondTask() {
        System.out.println("Task Two - Thread: " + Thread.currentThread().getName());
        Pageable pageable = PageRequest.of(1, userCountPerThread);
        processSchedule(pageable);
    }
}
