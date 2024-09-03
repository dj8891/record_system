package com.springboot.record_system.repository;

import com.springboot.record_system.model.VideoLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VideoLogRepository extends MongoRepository<VideoLog, String> {
    public boolean existsByFromDateBetweenAndName(LocalDateTime startDate, LocalDateTime endDate, String name);
    public List<VideoLog> findAllByFromDateBetweenAndName(LocalDateTime startDate, LocalDateTime toDate, String name);
}
