package com.springboot.record_system.repository;

import com.springboot.record_system.model.CallLog;
import com.springboot.record_system.model.DetectLog;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface DetectLogRepository extends MongoRepository<DetectLog, String> {

    @Aggregation(pipeline = {
            "{ '$match': { 'logTime': { '$gte': ?1, '$lt': ?2 } } }",
            "{ '$lookup': { 'from': 'ip_setting', 'localField': 'ipAddress', 'foreignField': 'ipAddress', 'as': 'ipAddress' } }",
            "{ '$unwind': { 'path': '$ipAddress', 'preserveNullAndEmptyArrays': true } }",
            "{ '$match': { 'ipAddress.userName': ?0 } }",
            "{ '$addFields': { 'userName': '$ipAddress.userName', 'ipAddress': '$ipAddress.ipAddress' } }"
    })
    List<DetectLog> findDetectLogWithUserNameAndTime(String name, LocalDateTime fromTime, LocalDateTime toTime);

    List<DetectLog> findAllByIpAddressOrderByLogTimeAsc(String ipAddress);
}
