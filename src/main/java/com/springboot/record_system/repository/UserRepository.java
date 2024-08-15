package com.springboot.record_system.repository;

import com.springboot.record_system.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
  Optional<User> findByName(String name);
}
