package com.matha.repository;

import com.matha.domain.School;
import com.matha.domain.SchoolAdjustment;
import com.matha.domain.SchoolPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SchoolAdjustmentRepository extends JpaRepository<SchoolAdjustment, Integer>
{
	@Query("select payment from SchoolAdjustment payment where payment.salesTxn.school = ?1")
	public List<SchoolAdjustment> findAllBySchool(School school);

}
