package com.springboot.record_system.repository;

import com.springboot.record_system.model.CallLog;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CallLogRepository extends MongoRepository<CallLog, String> {
    @Aggregation(pipeline = {
            "{ '$lookup': { 'from': 'ip_setting', 'localField': 'ipAddress', 'foreignField': 'ipAddress', 'as': 'ipAddress' } }",
            "{ '$unwind': '$ipAddress' }",
            "{ '$addFields': { 'userName': '$ipAddress.userName', 'ipAddress': '$ipAddress.ipAddress' } }",
            "{ '$sort': { 'fromTime': -1 } }"
    })
    List<CallLog> findCallLogWithUserName();
}
