package com.matha.repository;

import com.matha.domain.SalesDet;
import com.matha.domain.SalesReturnDet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesReturnDetRepository extends JpaRepository<SalesReturnDet, Integer> {

//	List<SalesDet> findAllBySerialId(String id);

}
