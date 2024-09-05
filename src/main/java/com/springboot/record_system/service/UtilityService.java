package com.springboot.record_system.service;

import org.springframework.stereotype.Service;

import java.time.*;
import java.util.Date;
import java.util.Locale;

@Service
public class UtilityService {
    public Date convertLocalDateTimeToUtc(LocalDateTime localDateTime) {
        ZonedDateTime utcDateTime = localDateTime.atZone(ZoneId.of("UTC"));
        return Date.from(utcDateTime.toInstant());
    }
}
