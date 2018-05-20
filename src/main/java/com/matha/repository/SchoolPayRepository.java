package com.matha.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.matha.domain.School;
import com.matha.domain.SchoolPayment;

@Repository
public interface SchoolPayRepository extends JpaRepository<SchoolPayment, Integer>
{
	@Query("select payment from SchoolPayment payment where payment.salesTxn.school = ?1")
	public List<SchoolPayment> findAllBySchool(School school);

}
