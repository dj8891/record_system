package com.springboot.record_system.service;

import com.springboot.record_system.dto.FileUploadDTO;
import com.springboot.record_system.dto.RecordDTO;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class FileProcessingService {

  private final CallService callService;
  private final DetectService detectService;

  public FileProcessingService(CallService callService, DetectService detectService) {
    this.callService = callService;
    this.detectService = detectService;
  }

  @Async
  public void processFile(FileUploadDTO fileUploadDTO) {
    MultipartFile file = fileUploadDTO.getFile();
    String UPLOAD_DIR = fileUploadDTO.getUploadDir();

    String originalFilename = file.getOriginalFilename();
    boolean isVideo = false;
    // Check if the file name is not null and contains a dot
    if (originalFilename != null && originalFilename.contains(".")) {
      // Split the file name by the dot and get the last part (extension)
      String extension = originalFilename.substring(originalFilename.lastIndexOf('.') + 1);
      isVideo = extension.equals("mp4");
    }
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
    String fileName = (fileUploadDTO.getDetectDTO().getIpAddress().isEmpty() ? "" : (fileUploadDTO.getDetectDTO().getIpAddress() + "_")) + result + "_" + file.getOriginalFilename();
    File uploadDir = new File(UPLOAD_DIR);
    if(!uploadDir.exists()) {
      boolean dir = uploadDir.mkdirs();
      System.out.println(dir);
    }
    Path filePath = Paths.get(UPLOAD_DIR + fileName);
    try ( OutputStream outputStream = Files.newOutputStream(filePath) ) {
      byte[] buffer = new byte[1024];
      int bytesRead;
      InputStream inputStream = file.getInputStream();
      while((bytesRead = inputStream.read(buffer)) != -1) {
        outputStream.write(buffer, 0, bytesRead);
      }
    } catch (IOException e) {
      System.out.println("server error: " + e.getMessage());
    }
    if(fileUploadDTO.isCall())
      callService.updateRecord(fileUploadDTO.getId(), isVideo, "upload/call/" + fileName);
    else {
      detectService.updateRecord(fileUploadDTO.getDetectDTO(), "upload/detect/" + fileName);
    }
  }
}
