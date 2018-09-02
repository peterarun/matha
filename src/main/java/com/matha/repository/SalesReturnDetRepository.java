package com.matha.repository;

import com.matha.domain.Book;
import com.matha.domain.OrderItem;
import com.matha.domain.SalesDet;
import com.matha.domain.SalesReturnDet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalesReturnDetRepository extends JpaRepository<SalesReturnDet, Integer>
{
    List<SalesReturnDet> findAllByBook(Book selectedOrder);
}
