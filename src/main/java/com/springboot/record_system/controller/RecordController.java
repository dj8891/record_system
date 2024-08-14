package com.springboot.record_system.controller;

import com.springboot.record_system.model.CallLog;
import com.springboot.record_system.service.FileProcessingService;
import com.springboot.record_system.service.CallService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.springboot.record_system.dto.RecordDTO;
import java.io.*;

@RestController
@RequestMapping("/record")
public class RecordController {

  private final FileProcessingService fileProcessingService;
  private final CallService callService;

  public RecordController(FileProcessingService fileProcessingService, CallService callService) {
    this.fileProcessingService = fileProcessingService;
    this.callService = callService;
  }

  @PostMapping("/upload")
  public String uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("id") String id) {

    if(file.isEmpty()) {
      return "Please select a file to upload";
    }
    try {
      String originalFilename = file.getOriginalFilename();
      boolean isVideo = false;
      // Check if the file name is not null and contains a dot
      if (originalFilename != null && originalFilename.contains(".")) {
        // Split the file name by the dot and get the last part (extension)
        String extension = originalFilename.substring(originalFilename.lastIndexOf('.') + 1);
        isVideo = extension.equals("avi");
      }
      fileProcessingService.processFile(file.getInputStream(), file.getOriginalFilename());
      callService.updateRecord(id, isVideo, "uploads/" + file.getOriginalFilename());
      return "File uploaded successfully: " + file.getOriginalFilename();
    } catch (IOException e) {
      return "File upload failed: " + e.getMessage();
    }
  }

  @PostMapping("/create")
  public String create(
      @RequestBody RecordDTO recordDTO,
      HttpServletRequest request) {
    String ipAddress = request.getHeader("X-Forwarded-For");
    if (ipAddress == null || ipAddress.isEmpty()) {
      ipAddress = request.getRemoteAddr();
    }
    recordDTO.setIpAddress(ipAddress);
    CallLog callLog = callService.createRecord(recordDTO);
    return callLog.getId();
  }
}
