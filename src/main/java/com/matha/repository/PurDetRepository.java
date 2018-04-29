package com.matha.repository;

import com.matha.domain.PurchaseDet;
import com.matha.domain.SalesDet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurDetRepository extends JpaRepository<PurchaseDet, Integer> {

//	List<SalesDet> findAllBySerialId(String id);

}
