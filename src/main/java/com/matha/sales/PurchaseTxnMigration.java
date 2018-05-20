package com.matha.sales;

import com.matha.domain.Purchase;
import com.matha.domain.PurchaseDet;
import com.matha.domain.PurchaseTransaction;
import com.matha.repository.PurchaseRepository;
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
import java.util.List;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.matha.repository")
@EntityScan("com.matha.domain")
@ComponentScan(basePackages = { "com.matha.controller", "com.matha.service"})
@EnableAutoConfiguration
public class PurchaseTxnMigration
{
	private static final Logger LOGGER = LogManager.getLogger(PurchaseTxnMigration.class);
	private static final int FIN_YEAR = 11;

	@Autowired
	private PurchaseRepository purchaseRepository;

	@Autowired
	private SchoolService schoolService;

	public static ConfigurableApplicationContext ctx;

	public static void main(String[] args)
	{
		ctx = SpringApplication.run(PurchaseTxnMigration.class, args);
		PurchaseTxnMigration mig = ctx.getBean(PurchaseTxnMigration.class);
		Integer finYear = FIN_YEAR;
		if(args != null && args.length > 0 && args[0] != null)
		{
			finYear = Integer.parseInt(args[0]);
		}
		Configurator.setLevel("com.matha", Level.DEBUG);

		LOGGER.debug("Running Purchase Transaction Creation for FY: " + finYear);
		mig.doMigration(finYear);
	}

	public void doMigration(Integer finYear)
	{
		Sort idSort = new Sort(new Sort.Order(Sort.Direction.ASC, "id"));
		LocalDate ld = LocalDate.of(2017, Month.NOVEMBER, 1);
		List<Purchase> purchases = purchaseRepository.findAllByFinancialYear(finYear, idSort);
//		List<Purchase> purchases = purchaseRepository.findAllByPurchaseDateAfter(ld, idSort);
		for (Purchase purchase : purchases)
		{
			LOGGER.info(purchase);
			if(purchase.getSalesTxn() == null)
			{
				PurchaseTransaction pTrans = new PurchaseTransaction();
				pTrans.setPublisher(purchase.getPublisher());
				pTrans.setAmount(purchase.getNetAmount());
				pTrans.setTxnDate(purchase.getPurchaseDate());
				List<PurchaseDet> orderList = new ArrayList<>(purchase.getPurchaseItems());
				schoolService.savePurchase(purchase, orderList, pTrans);
			}
		}
	}
}
