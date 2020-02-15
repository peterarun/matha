package com.matha.repository;

import com.matha.domain.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseDetRepository extends JpaRepository<PurchaseDet, Integer> {

    List<PurchaseDet> findAllByOrderItem(OrderItem orderItem);

    List<PurchaseDet> findAllByPurchase(Purchase purchase);

    @Query("select distinct pDet from PurchaseDet pDet where pDet.orderItem in (?1)")
    List<PurchaseDet> findAllByOrderItems(List<OrderItem> orderItems);

    @Query("select distinct pDet from PurchaseDet pDet join pDet.orderItem oDet join oDet.order ord where ord in (?1)")
    List<PurchaseDet> findAllByOrderList(List<Order> orders);

    List<PurchaseDet> findAllByBook(Book selectedOrder);
}
