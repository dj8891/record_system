package com.springboot.record_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.springboot.record_system.model.CallLog;

public interface CallLogRepository extends JpaRepository<CallLog, Long> {
}
