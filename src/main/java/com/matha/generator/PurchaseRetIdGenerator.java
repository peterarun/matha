package com.matha.generator;

import com.matha.repository.OrderRepository;
import com.matha.repository.PurchaseReturnRepository;
import com.matha.sales.SalesApplication;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;

//@Component
public class PurchaseRetIdGenerator implements IdentifierGenerator {

//	@Autowired
//	private OrderRepository orderRepository = SalesApplication.ctx.getBean(OrderRepository.class);

	@Override
	public Serializable generate(SharedSessionContractImplementor arg0, Object arg1) throws HibernateException {
		PurchaseReturnRepository purchaseReturnRepository = SalesApplication.ctx.getBean(PurchaseReturnRepository.class);
		return "PR-" + purchaseReturnRepository.fetchNextSeqVal();
	}

}
