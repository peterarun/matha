package com.matha.repository;

import com.matha.domain.SalesTransaction;
import com.matha.domain.School;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SalesTxnRepository extends JpaRepository<SalesTransaction, Integer> {

	@Query("select salesTxn from SalesTransaction salesTxn where salesTxn.school = ?1 and salesTxn.txnDate between ?2 and ?3")
	List<SalesTransaction> findByFromToDate(School school, LocalDate fromDate, LocalDate toDate);
	
//	@Query("select sum(salesTxn.amount) from SalesTransaction salesTxn where salesTxn.school = ?1 ")
//	Double findBalance(School school);
//
//	SalesTransaction findByPrevTxnIsNull();
//
//	SalesTransaction findByNextTxnIsNull();

	SalesTransaction findBySchoolAndPrevTxnIsNull(School school);

	SalesTransaction findBySchoolAndNextTxnIsNull(School school);

	List<SalesTransaction> findByTxnDateBetween(LocalDate fromDate, LocalDate toDate, Sort sortIn);

	List<SalesTransaction> findBySaleIsNotNullAndTxnDateBetween(LocalDate fromDate, LocalDate toDate, Sort sortIn);

	List<SalesTransaction> findByPaymentIsNotNullAndTxnDateBetween(LocalDate fromDate, LocalDate toDate, Sort sortIn);

	List<SalesTransaction> findBySalesReturnIsNotNullAndTxnDateBetween(LocalDate fromDate, LocalDate toDate, Sort sortIn);

}
