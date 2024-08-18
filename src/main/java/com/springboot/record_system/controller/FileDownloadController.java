package com.springboot.record_system.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping(path="/upload")
public class FileDownloadController {

  @GetMapping("/{filename:.+}")
  public ResponseEntity<Resource> getFile(@PathVariable String filename) {
    try {
      String uploadDir = "src/main/resources/static/upload/";
      Path filePath = Paths.get(uploadDir).resolve(filename).normalize();
      Resource resource = new UrlResource(filePath.toUri());

      if (resource.exists() || resource.isReadable()) {
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
            .contentType(org.springframework.http.MediaType.APPLICATION_OCTET_STREAM)
            .body(resource);
      } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
      }
    } catch (IOException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }
}
