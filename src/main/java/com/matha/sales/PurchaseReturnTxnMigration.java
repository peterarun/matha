package com.matha.sales;

import com.matha.domain.*;
import com.matha.repository.PurchaseRepository;
import com.matha.repository.PurchaseReturnRepository;
import com.matha.service.SchoolService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.matha.repository")
@EntityScan("com.matha.domain")
@ComponentScan(basePackages = { "com.matha.controller", "com.matha.service"})
@EnableAutoConfiguration
public class PurchaseReturnTxnMigration
{
	private static final Logger LOGGER = LogManager.getLogger(PurchaseReturnTxnMigration.class);

	@Autowired
	private PurchaseReturnRepository purchaseReturnRepository;

	@Autowired
	private SchoolService schoolService;

	public static ConfigurableApplicationContext ctx;

	public static void main(String[] args)
	{
		ctx = SpringApplication.run(PurchaseReturnTxnMigration.class, args);
		PurchaseReturnTxnMigration mig = ctx.getBean(PurchaseReturnTxnMigration.class);
		Configurator.setLevel("com.matha", Level.DEBUG);

		LocalDate ld = LocalDate.of(2017, Month.OCTOBER, 1);
		LOGGER.debug("Running Purchase Transaction Creation for FY: " + ld);

		mig.doMigration(ld);
	}

	public void doMigration(LocalDate ld)
	{
		Sort idSort = new Sort(new Sort.Order(Sort.Direction.ASC, "id"));

		List<PurchaseReturn> purchases = purchaseReturnRepository.findAllByReturnDateAfter(ld, idSort);
		for (PurchaseReturn purchase : purchases)
		{
			LOGGER.info(purchase);
			if(purchase.getSalesTxn() == null)
			{
				PurchaseTransaction pTrans = new PurchaseTransaction();
				pTrans.setPublisher(purchase.getPublisher());
				pTrans.setAmount(purchase.getNetAmount());
				pTrans.setTxnDate(purchase.getReturnDate());
				Set<PurchaseReturnDet> orderList = new HashSet<>(purchase.getPurchaseReturnDetSet());
				schoolService.savePurchaseReturn(purchase, pTrans, orderList);
			}
		}
	}
}
