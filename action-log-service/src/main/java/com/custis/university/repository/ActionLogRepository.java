package com.custis.university.repository;

import com.custis.university.model.ActionLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActionLogRepository extends MongoRepository<ActionLog, String> {
}
