package com.matha.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.matha.domain.Publisher;
import com.matha.domain.Purchase;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, String> {

	@Query("select sales from Purchase sales where sales.salesTxn.publisher = ?1")
	List<Purchase> findAllByPublisher(Publisher pub);

}
