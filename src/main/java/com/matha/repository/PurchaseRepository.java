package com.matha.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.matha.domain.Publisher;
import com.matha.domain.Purchase;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, String> {

//	@Query("select sales from Purchase sales where sales.salesTxn.publisher = ?1")
	Page<Purchase> findAllByPublisher(Publisher pub, Pageable pageable);

	@Query(value = "SELECT NEXT VALUE FOR PurchaseSeq", nativeQuery = true)
	Long fetchNextSeqVal();

	@Query(value = "select ISNULL(max(serialNo),0) + 1 from Purchase where financialYear = ?1")
	public Integer fetchNextSerialSeqVal(Integer fy);
}
