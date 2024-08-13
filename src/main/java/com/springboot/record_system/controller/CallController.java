package com.springboot.record_system.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.springboot.record_system.dto.SearchCriteria;
import com.springboot.record_system.service.CallService;
import com.springboot.record_system.model.CallLog;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/call")
public class CallController {

    private final CallService callService;

    public CallController(CallService callService) {
        this.callService = callService;
    }

    @PostMapping("/logs")
    public List<CallLog> getCallLogs(@RequestBody SearchCriteria searchCriteria) {
        return callService.searchLogs(searchCriteria);
    }
}
