package com.company.mongoDb.repository;

import com.company.mongoDb.model.Branch;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BranchRepository extends MongoRepository<Branch,String> {
}
