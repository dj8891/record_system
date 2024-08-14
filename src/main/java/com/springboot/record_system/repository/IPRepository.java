package com.springboot.record_system.repository;

import com.springboot.record_system.model.IPSetting;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IPRepository extends MongoRepository<IPSetting, String> {
    List<IPSetting> findAllByIpAddress(String ipAddress);
    Optional<IPSetting> findOneByIpAddress(String ipAddress);
}
