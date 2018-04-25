package com.matha.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.matha.domain.Sales;
import com.matha.domain.School;

@Repository
public interface SalesRepository extends JpaRepository<Sales, String> {

	@Query(value = "SELECT NEXT VALUE FOR SalesSeq", nativeQuery = true)
	public Long fetchNextSeqVal();
	
//	@Query(value = "SELECT NEXT VALUE FOR SalesSerialSeq", nativeQuery = true)
	@Query(value = "select ISNULL(max(invoiceNo),0) + 1 from Sales where financialYear = ?1")
	public Integer fetchNextSerialSeqVal(Integer fy);

	@Query("select sales from Sales sales where sales.salesTxn.school = ?1")
	public List<Sales> findAllByTxnSchool(School school);
	
	public List<Sales> findAllBySchool(School school);
}
