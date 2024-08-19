package com.springboot.record_system.controller;

import com.springboot.record_system.model.IPSetting;
import com.springboot.record_system.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/setting")
public class SettingController {

    @Autowired
    private SettingService settingService;

    public SettingController(SettingService settingService) {
        this.settingService = settingService;
    }

    @GetMapping(path = "/list")
    public List<IPSetting> getAllData() {
        return settingService.getAllData();
    }

    @GetMapping(path = "/ip/{id}")
    public IPSetting getData(@PathVariable String id) {
        Optional< IPSetting > optional = settingService.getDataById(id);
        return optional.orElse(null);
    }

    @PostMapping(path = "/ip")
    public ResponseEntity<?> createData(@RequestBody IPSetting data) {
        return settingService.saveOrUpdate(data);
    }

    @PutMapping(path = "/ip/{id}")
    public ResponseEntity<?> udpateData(@RequestBody IPSetting data, @PathVariable(name="id") String id) {
        data.setId(id);
        return settingService.saveOrUpdate(data);
    }

    @DeleteMapping(path = "/ip/{id}")
    public void deleteDataById(@PathVariable String id) {
        settingService.deleteDataById(id);
    }
}
