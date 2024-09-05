package com.springboot.record_system.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.record_system.dto.DetectDTO;
import com.springboot.record_system.dto.FileUploadDTO;
import com.springboot.record_system.dto.VideoRequestDTO;
import com.springboot.record_system.dto.VideoResponseDTO;
import com.springboot.record_system.model.VideoLog;
import com.springboot.record_system.service.FileProcessingService;
import com.springboot.record_system.service.VideoService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/detect")
public class DetectController {

    private final FileProcessingService fileProcessingService;
    private final ObjectMapper objectMapper;
    private final VideoService videoService;

    public DetectController(FileProcessingService fileProcessingService,
                            VideoService videoService,
                            ObjectMapper objectMapper) {
        this.fileProcessingService = fileProcessingService;
        this.objectMapper = objectMapper;
        this.videoService = videoService;
    }
    // check if server is connected
    @GetMapping("/connected")
    public ResponseEntity<String> isConnected() {
        return ResponseEntity.ok("connected");
    }

    @PostMapping("/create")
    public String uploadFile(@RequestParam("data") String detectStr, @ModelAttribute("file") MultipartFile file, HttpServletRequest request) throws IOException {
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

    @PostMapping("/video")
    public ResponseEntity<List<VideoLog>> getVideo(@RequestBody VideoRequestDTO videoRequestDTO) throws IOException, InterruptedException {
        return ResponseEntity.ok(videoService.getVideo(videoRequestDTO.getDate(), videoRequestDTO.getName()));
    }
}
