package com.matha.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.matha.domain.Publisher;
import com.matha.domain.PurchasePayment;

@Repository
public interface PurchasePayRepository extends JpaRepository<PurchasePayment, Integer>
{
	@Query("select payment from PurchasePayment payment where payment.salesTxn.publisher = ?1")
	public List<PurchasePayment> findAllByPublisher(Publisher school);

}
