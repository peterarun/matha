package com.matha.generator;

import com.matha.repository.PurchaseReturnRepository;
import com.matha.repository.SchoolReturnRepository;
import com.matha.sales.SalesApplication;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class SalesRetIdGenerator implements IdentifierGenerator {

//	@Autowired
//	private OrderRepository orderRepository = SalesApplication.ctx.getBean(OrderRepository.class);

	@Override
	public Serializable generate(SharedSessionContractImplementor arg0, Object arg1) throws HibernateException {
		SchoolReturnRepository purchaseReturnRepository = SalesApplication.ctx.getBean(SchoolReturnRepository.class);
		return "SR-" + purchaseReturnRepository.fetchNextSeqVal();
	}

}
