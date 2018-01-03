package com.matha.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.matha.domain.Book;

@Repository
public interface BookRepository extends MongoRepository<Book, String> {

}
