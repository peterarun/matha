package com.matha.repository;

import com.matha.domain.PurchaseTransaction;
import com.matha.sales.SalesApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest(classes = SalesApplication.class)
@RunWith(SpringRunner.class)
public class PurchaseTxnRepositoryTest
{

    @Autowired
    private PurchaseTxnRepository purchaseTxnRepository;

    @Test
    public void test_findByTypeAndTxnDateBetween()
    {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "txnDate"), new Sort.Order(Sort.Direction.ASC, "id"));
        List<PurchaseTransaction> purTxns = purchaseTxnRepository.findByPurchaseIsNotNullAndTxnDateBetween(LocalDate.of(2017, Month.FEBRUARY, 1), LocalDate.now(), sort);
    }
}