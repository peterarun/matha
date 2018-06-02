package com.matha.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.matha.domain.School;
import com.matha.domain.SchoolReturn;

@Repository
public interface SchoolReturnRepository extends JpaRepository<SchoolReturn, String>
{
	@Query("select payment from SchoolReturn payment where payment.salesTxn.school = ?1")
	public List<SchoolReturn> findAllBySchool(School school);

	@Query(value = "SELECT NEXT VALUE FOR SReturnSeq", nativeQuery = true)
	Long fetchNextSeqVal();

}
