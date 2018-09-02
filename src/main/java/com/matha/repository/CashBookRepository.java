package com.matha.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.matha.domain.CashBook;

@Repository
public interface CashBookRepository extends JpaRepository<CashBook, Integer> {

	List<CashBook> findAllByOrderByTxnDateDesc();

	@Query(value="select cb from CashBook cb, CashHead hd where cb.type = hd and cb.txnDate between ?1 and ?2 and cb.description like %?3% order by cb.id desc")
	List<CashBook> fetchCashBookRecords(LocalDate fromDate, LocalDate toDate, String desc);

	@Query(value="select cb from CashBook cb where cb.txnDate >= ?1 and cb.description like %?2% order by cb.id desc")
	List<CashBook> fetchCashBookRecordsFr(LocalDate fromDate, String desc);

	@Query(value="select cb from CashBook cb where cb.txnDate <= ?1 and cb.description like %?2% order by cb.id desc")
	List<CashBook> fetchCashBookRecordsTo(LocalDate toDate, String desc);

	@Query(value="select cb from CashBook cb where cb.description like %?1% order by cb.id desc")
	List<CashBook> fetchCashBookRecords(String desc);

	@Query(value = "select ISNULL(max(id),0) + 1 from CashBook")
	public Integer fetchNextSerialSeqVal();

	List<CashBook> findAllByTxnDateBetweenOrderByIdDesc(LocalDate fromDate, LocalDate toDate);
}
