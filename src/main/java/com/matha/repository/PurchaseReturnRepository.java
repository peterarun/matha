package com.matha.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.matha.domain.Publisher;
import com.matha.domain.PurchaseReturn;

@Repository
public interface PurchaseReturnRepository extends JpaRepository<PurchaseReturn, Integer>
{
	@Query("select payment from PurchaseReturn payment where payment.salesTxn.publisher = ?1")
	public List<PurchaseReturn> findAllByPublisher(Publisher school, Sort dateSort);

}
