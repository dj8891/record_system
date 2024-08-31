package com.springboot.record_system.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.record_system.dto.DetectDTO;
import com.springboot.record_system.dto.FileUploadDTO;
import com.springboot.record_system.service.DetectService;
import com.springboot.record_system.service.FileProcessingService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/detect")
public class DetectController {

    private final FileProcessingService fileProcessingService;
    private final DetectService detectService;
    private final ObjectMapper objectMapper;

    public DetectController(FileProcessingService fileProcessingService,
                            DetectService detectService,
                            ObjectMapper objectMapper) {
        this.fileProcessingService = fileProcessingService;
        this.detectService = detectService;
        this.objectMapper = objectMapper;
    }
    // check if server is connected
    @GetMapping("/connected")
    public ResponseEntity<String> isConnected() {
        return ResponseEntity.ok("connected");
    }

    @PostMapping("/create")
    public String uploadFile(@RequestParam("data") String detectStr, @ModelAttribute("file") MultipartFile file, HttpServletRequest request) throws JsonProcessingException {
        String uploadDir = "src/main/resources/static/upload/detect/";
        DetectDTO detectDTO = objectMapper.readValue(detectStr, DetectDTO.class);
        if(file.isEmpty()) {
            return "Please select a file to upload";
        }
        String ipAddress = request.getHeader("X-Forwarded-For");
        if(ipAddress == null || ipAddress.isEmpty()) {
            ipAddress = request.getRemoteAddr();
        }
        detectDTO.setIpAddress(ipAddress);
        FileUploadDTO fileUploadDTO = new FileUploadDTO(uploadDir, file, "", false, detectDTO);
        fileProcessingService.processFile(fileUploadDTO);
        return "File uploaded successfully: " + file.getOriginalFilename();
    }
}
