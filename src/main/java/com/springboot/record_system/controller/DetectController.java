package com.springboot.record_system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.record_system.dto.DetectDTO;
import com.springboot.record_system.dto.FileUploadDTO;
import com.springboot.record_system.dto.VideoRequestDTO;
import com.springboot.record_system.model.VideoLog;
import com.springboot.record_system.service.FileProcessingService;
import com.springboot.record_system.service.VideoService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    public String uploadFile(@RequestParam("url") String url, @RequestParam("isKeyPressed") boolean isKeyPressed, @RequestParam("isBtnClicked") boolean isBtnClicked, @ModelAttribute("file") MultipartFile file, HttpServletRequest request) throws IOException {
        String uploadDir = "src/main/resources/upload/detect/";
        if(file.isEmpty()) {
            return "Please select a file to upload";
        }
        String ipAddress = request.getHeader("X-Forwarded-For");
        if(ipAddress == null || ipAddress.isEmpty()) {
            ipAddress = request.getRemoteAddr();
        }
        DetectDTO detectDTO = new DetectDTO();
        detectDTO.setUrl(url);
        detectDTO.setBtnClicked(isBtnClicked);
        detectDTO.setKeyPressed(isKeyPressed);
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
