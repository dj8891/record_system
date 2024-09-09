package com.springboot.record_system.controller;

import com.springboot.record_system.dto.FileUploadDTO;
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
  public String uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("id") String id) throws IOException {
    String callUploadDir = "upload/call/";
    if(file.isEmpty()) {
      return "Please select a file to upload";
    }
    FileUploadDTO fileUploadDTO = new FileUploadDTO(callUploadDir, file, id, true);
    fileProcessingService.processFile(fileUploadDTO);
    return "File uploaded successfully: " + file.getOriginalFilename();
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
