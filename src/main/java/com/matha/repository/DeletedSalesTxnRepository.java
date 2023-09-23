package com.matha.repository;

import com.matha.domain.DeletedSalesTransaction;
import com.matha.domain.SalesTransaction;
import com.matha.domain.School;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DeletedSalesTxnRepository extends JpaRepository<DeletedSalesTransaction, Integer> {

}
