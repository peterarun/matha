package com.matha.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.matha.domain.SalesTransaction;
import com.matha.domain.School;

@Repository
public interface SalesTxnRepository extends JpaRepository<SalesTransaction, Integer> {

	@Query("select salesTxn from SalesTransaction salesTxn where salesTxn.school = ?1 and salesTxn.txnDate between ?2 and ?3")
	List<SalesTransaction> findByFromToDate(School school, LocalDate fromDate, LocalDate toDate);
}
