package com.matha.sales;

import com.matha.domain.Sales;
import com.matha.domain.SalesTransaction;
import com.matha.repository.SalesRepository;
import com.matha.service.SchoolService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import java.util.List;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.matha.repository")
@EntityScan("com.matha.domain")
@ComponentScan(basePackages = { "com.matha.controller", "com.matha.service"})
@EnableAutoConfiguration
public class SalesTxnMigration
{
	private static final Logger LOGGER = LogManager.getLogger(SalesTxnMigration.class);
	private static final int FIN_YEAR = 11;

	@Autowired
	private SalesRepository salesRepository;

	@Autowired
	private SchoolService schoolService;

	public static ConfigurableApplicationContext ctx;

	public static void main(String[] args)
	{
		ctx = SpringApplication.run(SalesTxnMigration.class, args);
		SalesTxnMigration mig = ctx.getBean(SalesTxnMigration.class);
		Integer finYear = FIN_YEAR;
		if(args != null && args.length > 0 && args[0] != null)
		{
			finYear = Integer.parseInt(args[0]);
		}
		mig.doMigration(finYear);
	}

	public void doMigration(Integer finYear)
	{
		LocalDate ld = LocalDate.of(2017, Month.NOVEMBER, 1);
		Sort idSort = new Sort(new Sort.Order(Sort.Direction.ASC, "id"));
		List<Sales> purchases = salesRepository.findAllByFinancialYear( finYear, idSort);
//		List<Sales> purchases = salesRepository.findAllByTxnDateAfter( ld, idSort);
		for (Sales sale : purchases)
		{
			LOGGER.debug(sale);
			if(sale.getSalesTxn() == null)
			{
				LOGGER.info("Processing Sales Item: " + sale.getId());
				SalesTransaction pTrans = new SalesTransaction();
				pTrans.setSchool(sale.getSchool());
				pTrans.setAmount(sale.getNetAmount());
				pTrans.setTxnDate(sale.getInvoiceDate());
				schoolService.saveSalesData(sale, sale.getSaleItems(), pTrans);
			}
		}
	}
}
