package com.matha.generator;

import java.io.Serializable;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;

import com.matha.repository.SalesRepository;
import com.matha.sales.SalesApplication;

//@Component
public class SalesIdGenerator implements IdentifierGenerator {

//	@Autowired
//	private OrderRepository orderRepository = SalesApplication.ctx.getBean(OrderRepository.class);

	@Override
	public Serializable generate(SessionImplementor arg0, Object arg1) throws HibernateException {
		SalesRepository salesRepository = SalesApplication.ctx.getBean(SalesRepository.class);
		return "S-"+ salesRepository.fetchNextSeqVal();
	}

}
