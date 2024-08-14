package com.springboot.record_system.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

@Service
public class FileProcessingService {

  private static final String UPLOAD_DIR = "uploads/";

  @Async
  public void processFile(InputStream inputStream, String fileName) {
    // Create upload directory if it doesn't exist
    File uploadDir = new File(UPLOAD_DIR);
    if(!uploadDir.exists()) {
      boolean result = uploadDir.mkdirs();
      System.out.println(result);
    }
    Path filePath = Paths.get(UPLOAD_DIR + fileName);

    try ( OutputStream outputStream = Files.newOutputStream(filePath) ) {
      byte[] buffer = new byte[1024];
      int bytesRead;
      while((bytesRead = inputStream.read(buffer)) != -1) {
        outputStream.write(buffer, 0, bytesRead);
      }
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }
}
