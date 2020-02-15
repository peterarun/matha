package com.matha.generator;

import java.io.Serializable;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import com.matha.repository.OrderRepository;
import com.matha.sales.SalesApplication;

//@Component
public class OrderIdGenerator implements IdentifierGenerator {

//	@Autowired
//	private OrderRepository orderRepository = SalesApplication.ctx.getBean(OrderRepository.class);

	@Override
	public Serializable generate(SharedSessionContractImplementor arg0, Object arg1) throws HibernateException {
		OrderRepository orderRepository = SalesApplication.ctx.getBean(OrderRepository.class);
		return "SO-" + orderRepository.fetchNextSeqVal();
	}

}
