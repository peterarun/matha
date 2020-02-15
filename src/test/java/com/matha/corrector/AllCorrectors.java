package com.matha.corrector;

import com.matha.domain.SchoolPayment;
import com.matha.repository.SchoolPayRepository;
import com.matha.sales.SalesApplication;
import com.matha.service.SchoolService;
import com.matha.util.LogUtil;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SalesApplication.class)
public class AllCorrectors
{

	private static final Logger LOGGER = LogManager.getLogger(LogUtil.class);

	@Autowired
	SchoolService schoolService;

	@Autowired
	SchoolPayRepository schoolPayRepository;

	@Before
	public void setUp()
	{
		Configurator.setLevel("com.matha", Level.DEBUG);
	}

	@Test
	public void deletePayment()
	{
		Integer[] paymentIds = {93};
		for (Integer paymentId : paymentIds)
		{
			LOGGER.debug(paymentId);
			SchoolPayment payment = schoolPayRepository.getOne(paymentId);
			LOGGER.debug(payment);
			schoolService.deletePayment(payment);
		}
	}
}
