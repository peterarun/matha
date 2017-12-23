package com.matha.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.matha.domain.School;

@Repository
public interface SchoolRepository extends MongoRepository<School, String> {

	List<School> findByNameLike(String schoolNamePart);
}
