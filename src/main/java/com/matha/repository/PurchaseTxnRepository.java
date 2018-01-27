package com.matha.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.matha.domain.Publisher;
import com.matha.domain.PurchaseTransaction;

@Repository
public interface PurchaseTxnRepository extends JpaRepository<PurchaseTransaction, Integer> {

	@Query("select salesTxn from PurchaseTransaction salesTxn where salesTxn.publisher = ?1 and salesTxn.txnDate between ?2 and ?3")
	List<PurchaseTransaction> findByFromToDate(Publisher school, LocalDate fromDate, LocalDate toDate);
	
	@Query("select sum(salesTxn.amount) from PurchaseTransaction salesTxn where salesTxn.publisher = ?1 ")
	Double findBalance(Publisher school);
}
