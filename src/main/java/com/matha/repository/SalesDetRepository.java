package com.matha.repository;

import java.util.List;

import com.matha.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesDetRepository extends JpaRepository<SalesDet, Integer> {

//	List<SalesDet> findAllBySerialId(String id);

    List<SalesDet> findAllByOrderItem(OrderItem orderItem);

    @Query("select distinct pDet from SalesDet pDet where pDet.orderItem in (?1)")
    List<SalesDet> findAllByOrderItems(List<OrderItem> orderItems);

    List<SalesDet> findAllByBook(Book selectedOrder);
}
