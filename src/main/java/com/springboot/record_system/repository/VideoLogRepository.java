package com.springboot.record_system.repository;

import com.springboot.record_system.model.VideoLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface VideoLogRepository extends MongoRepository<VideoLog, String> {

//    @Aggregation(pipeline = {
//            "{ '$match': { 'name': ?0, 'fromDate': { '$gte': ?1 } } }",  // Corrected name filter
//            "{ '$lookup': { 'from': 'detect_log', 'let': { 'startDate': '$fromDate', 'endDate': '$toDate' }, " +
//                    "'pipeline': [ " +
//                    "{ '$match': { '$expr': { '$and': [ " +
//                    "{ '$gte': ['$logTime', '$$startDate'] }, " +   // Fixed comparison expressions
//                    "{ '$lt': ['$logTime', '$$endDate'] } ] } } } " +
//                    "], 'as': 'detectLogs' } }",
//            "{ '$unwind': { 'path': '$detectLogs', 'preserveNullAndEmptyArrays': true } }",  // Unwind with null preservation
//            "{ '$project': { 'fromDate': 1, 'toDate': 1, 'detectLogs': 1, 'fileLocation': 1 } }"
//    })
    public List<VideoLog> findByNameAndFromDateBetween(String name, Date fromDate, Date toDate);
}
