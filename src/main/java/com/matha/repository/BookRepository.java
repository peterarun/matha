package com.matha.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.matha.domain.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, String> {

	List<Book> findAllByNameStartingWith(String bookName);

}
