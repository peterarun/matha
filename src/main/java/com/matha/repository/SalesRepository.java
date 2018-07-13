package com.matha.repository;

import com.matha.domain.Sales;
import com.matha.domain.School;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SalesRepository extends JpaRepository<Sales, String> {

	@Query(value = "SELECT NEXT VALUE FOR SalesSeq", nativeQuery = true)
	public Long fetchNextSeqVal();
	
//	@Query(value = "SELECT NEXT VALUE FOR SalesSerialSeq", nativeQuery = true)
	@Query(value = "select ISNULL(max(serialNo),0) + 1 from Sales where financialYear = ?1")
	public Integer fetchNextSerialSeqVal(Integer fy);

	@Query("select sales from Sales sales where sales.salesTxn.school = ?1")
	public List<Sales> findAllByTxnSchool(School school);

	public List<Sales> findAllBySchool(School school);

	public Page<Sales> findAllByIdLike(String searchStr, Pageable pageable);

	public List<Sales> findAllBySchoolAndFinancialYear(School school, int fy);

	public List<Sales> findAllByFinancialYear(int fy, Sort sortIn);

	public List<Sales> findAllByTxnDateAfter(LocalDate txnDt, Sort sortIn);

	public List<Sales> findAllBySchoolAndTxnDateAfter(School sc, LocalDate txnDt, Sort sortIn);

	public List<Sales> findAllBySalesTxnIsNull();

	Sales findById(String salesId);
}
