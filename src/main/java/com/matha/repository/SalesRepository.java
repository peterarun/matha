package com.matha.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.matha.domain.Sales;

@Repository
public interface SalesRepository extends JpaRepository<Sales, String> {

	@Query(value = "SELECT NEXT VALUE FOR SalesSeq", nativeQuery = true)
	public Long fetchNextSeqVal();
}
