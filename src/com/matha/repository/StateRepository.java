package com.matha.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.matha.domain.State;

@Repository
public interface StateRepository extends MongoRepository<State, String> {

}
