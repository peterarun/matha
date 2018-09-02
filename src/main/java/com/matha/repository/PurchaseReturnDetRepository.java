package com.matha.repository;

import com.matha.domain.Book;
import com.matha.domain.OrderItem;
import com.matha.domain.PurchaseReturnDet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseReturnDetRepository extends JpaRepository<PurchaseReturnDet, Integer>
{
    List<PurchaseReturnDet> findAllByBook(Book selectedOrder);
}
