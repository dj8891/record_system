package com.springboot.record_system.dto;

import org.springframework.web.multipart.MultipartFile;

public class FileUploadDTO {
    String uploadDir; // upload dir
    MultipartFile file; // uploaded file data
    String id; // database primary key
    boolean isCall; // true: call log, false: detect log
    DetectDTO detectDTO;

    public FileUploadDTO(String uploadDir, MultipartFile file, String id, boolean isCall) {
        this(uploadDir, file, id, isCall, null);
    }

    public FileUploadDTO(String uploadDir, MultipartFile file, String id, boolean isCall, DetectDTO detectDTO) {
        this.uploadDir = uploadDir;
        this.file = file;
        this.id = id;
        this.isCall = isCall;
        this.detectDTO = detectDTO;
    }

    public DetectDTO getDetectDTO() {
        return detectDTO;
    }

    public void setDetectDTO(DetectDTO detectDTO) {
        this.detectDTO = detectDTO;
    }

    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isCall() {
        return isCall;
    }

    public void setCall(boolean call) {
        isCall = call;
    }
}
