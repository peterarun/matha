package com.matha.repository;

import com.matha.controller.SchoolController;
import com.matha.domain.Order;
import com.matha.domain.PurchaseDet;
import com.matha.sales.SalesApplication;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest(classes = SalesApplication.class)
@RunWith(SpringRunner.class)
public class PurchaseDetRepositoryTest
{
    private static final Logger LOGGER = LogManager.getLogger(SchoolController.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PurchaseDetRepository purchaseDetRepository;

    @Test
    public void findAllByOrderItems() {
    }

    @Test
    public void findAllByOrderList()
    {
        List<Order> orderList = orderRepository.findAll();
        List<PurchaseDet> purDetList = purchaseDetRepository.findAllByOrderList(orderList);
        for (PurchaseDet order : purDetList)
        {
            LOGGER.info(order);
        }
    }
}