package com.matha.repository;

import com.matha.domain.OrderItem;
import com.matha.domain.SalesDet;
import com.matha.sales.SalesApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest(classes = SalesApplication.class)
@RunWith(SpringRunner.class)
public class SalesDetRepositoryTest {

    @Autowired
    private SalesDetRepository salesDetRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Test
    public void findAllByOrderItem() {
    }

    @Test
    public void findAllByOrderItems()
    {
        List<OrderItem> orderItems = new ArrayList<>();

        for (int i = 0; i < 5; i++)
        {
            OrderItem item1 = orderItemRepository.findOne(i);
            if(item1 != null)
            {
                orderItems.add(item1);
            }
        }
        for (OrderItem orderItem : orderItems)
        {
            System.out.println(orderItem);
        }

        List<SalesDet> sits = salesDetRepository.findAllByOrderItems(orderItems);
        for (SalesDet sit : sits)
        {
            System.out.println(sit);
        }
    }
}