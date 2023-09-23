package com.matha.repository;

import com.matha.domain.DeletedPurchaseTransaction;
import com.matha.domain.Publisher;
import com.matha.domain.PurchaseTransaction;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DeletedPurchaseTxnRepository extends JpaRepository<DeletedPurchaseTransaction, Integer> {

	@Query("select salesTxn from DeletedPurchaseTransaction salesTxn where salesTxn.publisher = ?1 and salesTxn.txnDate between ?2 and ?3")
	List<DeletedPurchaseTransaction> findByFromToDate(Publisher school, LocalDate fromDate, LocalDate toDate, Sort sortIn);

    List<DeletedPurchaseTransaction> findByTxnDateBetween(LocalDate fromDate, LocalDate toDate, Sort sortIn);

    List<DeletedPurchaseTransaction> findByPurchaseIsNotNullAndTxnDateBetween(LocalDate fromDate, LocalDate toDate, Sort sortIn);

	List<DeletedPurchaseTransaction> findByPaymentIsNotNullAndTxnDateBetween(LocalDate fromDate, LocalDate toDate, Sort sortIn);

	List<DeletedPurchaseTransaction> findByPurchaseReturnIsNotNullAndTxnDateBetween(LocalDate fromDate, LocalDate toDate, Sort sortIn);

	DeletedPurchaseTransaction findByPublisherAndPrevTxnIsNull(Publisher publisher);

	DeletedPurchaseTransaction findByPublisherAndNextTxnIsNull(Publisher publisher);
}
