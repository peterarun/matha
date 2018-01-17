package com.matha.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.matha.domain.CashBook;

@Repository
public interface CashBookRepository extends JpaRepository<CashBook, String> {

	List<CashBook> findAllByOrderByTxnDateDesc();

}
