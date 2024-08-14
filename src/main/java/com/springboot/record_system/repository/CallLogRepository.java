package com.springboot.record_system.repository;

import com.springboot.record_system.model.CallLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CallLogRepository extends MongoRepository<CallLog, String> {
}
