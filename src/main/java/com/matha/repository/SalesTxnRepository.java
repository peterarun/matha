package com.matha.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.matha.domain.SalesTransaction;

@Repository
public interface SalesTxnRepository extends JpaRepository<SalesTransaction, Integer> {

}
