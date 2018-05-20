package com.matha.repository;

import com.matha.domain.PurchaseReturnDet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseReturnDetRepository extends JpaRepository<PurchaseReturnDet, Integer> {

//	List<SalesDet> findAllBySerialId(String id);

}
