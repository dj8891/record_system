package com.springboot.record_system.repository;

import com.springboot.record_system.model.DetectLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetectLogRepository extends MongoRepository<DetectLog, String> {

}
