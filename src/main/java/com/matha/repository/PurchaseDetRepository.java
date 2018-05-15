package com.matha.repository;

import com.matha.domain.Publisher;
import com.matha.domain.Purchase;
import com.matha.domain.PurchaseDet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseDetRepository extends JpaRepository<PurchaseDet, String> {

}
