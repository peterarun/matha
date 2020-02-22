package com.matha.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.matha.domain.Publisher;
import com.matha.domain.Purchase;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, String> {

	@Query("select sales from Purchase sales where sales.salesTxn.publisher = ?1 and (sales.statusInd is null or sales.statusInd <> -2)")
	Page<Purchase> findAllActiveByPublisher(Publisher pub, Pageable pageable);

	Page<Purchase> findByInvoiceNoLike(String searchStr, Pageable pageable);

	@Query("select sales from Purchase sales where serialNo like ?1")
	Page<Purchase> findBySerialNoLikeStr(String searchStr, Pageable pageable);

	@Query(value = "SELECT NEXT VALUE FOR PurchaseSeq", nativeQuery = true)
	Long fetchNextSeqVal();

	@Query(value = "select ISNULL(max(serialNo),0) + 1 from Purchase where financialYear = ?1")
	public Integer fetchNextSerialSeqVal(Integer fy);

	@Query("select sales from Purchase sales where sales.publisher = ?1 and financialYear = ?2 ")
	List<Purchase> findAllByPublisherAndFinancialYear(Publisher pub, int fy);

	List<Purchase> findAllByPublisherAndPurchaseDateAfter(Publisher pub, LocalDate dt, Sort sortIn);

	List<Purchase> findAllByPurchaseDateAfter(LocalDate dt, Sort sortIn);

	List<Purchase> findAllByFinancialYear(Integer fy, Sort sortIn);

	List<Purchase> findAllBySalesTxnIsNull();
}
