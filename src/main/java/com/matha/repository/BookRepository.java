package com.matha.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.matha.domain.Book;
import com.matha.domain.Publisher;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

	List<Book> findAllByNameStartingWith(String bookName);

	List<Book> findAllByPublisher(Publisher publisher);

}
