package com.matha.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.matha.domain.BookCategory;

@Repository
public interface CategoryRepository extends JpaRepository<BookCategory, String> {

}
