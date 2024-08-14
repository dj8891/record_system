package com.springboot.record_system.service;

import com.springboot.record_system.model.IPSetting;
import com.springboot.record_system.repository.IPRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class SettingService {

    @Autowired
    private final IPRepository ipRepository;

    public SettingService(IPRepository ipRepository) {
        this.ipRepository = ipRepository;
    }

    public List<IPSetting> getAllData() {
        return ipRepository.findAll();
    }

    public Optional<IPSetting> getDataById(String id) {
        return ipRepository.findById(id);
    }

    public ResponseEntity<?> saveOrUpdate(IPSetting data) {

        List<IPSetting> list = ipRepository.findAllByIpAddress(data.getIpAddress());
        if (!list.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Duplicate IP address found: " + data.getIpAddress());
        }

        return ResponseEntity.ok(ipRepository.save(data));
    }

    public void deleteDataById(String id) {
        ipRepository.deleteById(id);
    }
}
