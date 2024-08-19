package com.springboot.record_system.controller;

import org.springframework.web.bind.annotation.*;

import com.springboot.record_system.dto.SearchCriteria;
import com.springboot.record_system.service.CallService;
import com.springboot.record_system.model.CallLog;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/call")
public class CallController {

    private final CallService callService;

    public CallController(CallService callService) {
        this.callService = callService;
    }

    @GetMapping(path = "/log/{id}")
    public CallLog getData(@PathVariable String id) {
        Optional< CallLog > optional = callService.getLogById(id);
        return optional.orElse(null);
    }

    @PostMapping("/logs")
    public List<CallLog> getCallLogs(@RequestBody SearchCriteria searchCriteria) {
        return callService.searchLogs(searchCriteria);
    }
}
