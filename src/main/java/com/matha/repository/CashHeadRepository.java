package com.matha.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.matha.domain.CashHead;

@Repository
public interface CashHeadRepository extends JpaRepository<CashHead, String> {

}
