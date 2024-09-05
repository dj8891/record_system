package com.springboot.record_system.repository;

import com.springboot.record_system.model.VideoLog;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface VideoLogRepository extends MongoRepository<VideoLog, String> {
//    @Aggregation(pipeline = {
//            "{ '$match': { 'fromDate': { '$gte': ?1, '$lt': ?2 }, 'name': ?0 } }",
//            "{ '$lookup': { 'from': 'detect_log', 'let': { 'ip': '$ipAddress', 'start': '$fromDate', 'end': '$toDate' }, " +
//                    "'pipeline': [ { '$match': { '$expr': { '$and': [ { '$eq': ['$ipAddress', '$$ip'] }, " +
//                    { '$gte': ['$logTime', '$$start'] }, { '$lte': ['$logTime', '$$end'] } ] } }, " +
//        "{ '$project': { 'logTime': 1, 'userName': 1, 'ipAddress': 1 } } ], 'as': 'ipAddress' } }",
//        "{ '$unwind': { 'path': '$ipAddress', 'preserveNullAndEmptyArrays': true } }",
//        "{ '$match': { 'ipAddress.userName': ?0 } }",
//        "{ '$addFields': { 'userName': '$ipAddress.userName', 'ipAddress': '$ipAddress.ipAddress' } }"
//        })
    @Aggregation(pipeline = {
            "{ '$match': { 'fromDate': { '$gte': ?1, '$lt': ?2 } }, 'name': ?0 }",
            "{ '$lookup': { 'from': 'detect_log', 'let': { 'startDate': '$fromDate', 'endDate': '$toDate' }, " +
                    "'pipeline'} }",
            "{ '$unwind': { 'path': '$ipAddress', 'preserveNullAndEmptyArrays': true } }",
            "{ '$match': { 'ipAddress.userName': ?0 } }",
            "{ '$addFields': { 'userName': '$ipAddress.userName', 'ipAddress': '$ipAddress.ipAddress' } }"
    })
    public List<VideoLog> findByFromDateBetweenAndName(String name, Date startDate, Date toDate);
    public boolean existsByFromDateBetweenAndName(Date startDate, Date endDate, String name);
    public List<VideoLog> findAllByFromDateBetweenAndName(Date startDate, Date endDate, String name);
}
