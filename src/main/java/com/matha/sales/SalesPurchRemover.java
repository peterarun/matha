package com.matha.sales;

import com.matha.domain.*;
import com.matha.repository.PurchaseRepository;
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
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.matha.util.UtilConstants.SEMI_COLON_SIGN;
import static com.matha.util.Utils.*;
import static java.util.stream.Collectors.toMap;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.matha.repository")
@EntityScan("com.matha.domain")
@ComponentScan(basePackages = { "com.matha.controller", "com.matha.service"})
@EnableAutoConfiguration
public class SalesPurchRemover
{
	private static final Logger LOGGER = LogManager.getLogger(SalesPurchRemover.class);

	@Autowired
	private SalesRepository salesRepository;

	@Autowired
	private PurchaseRepository purchaseRepository;

	@Autowired
	private SchoolService schoolService;

	public static ConfigurableApplicationContext ctx;

	public static void main(String[] args)
	{
		ctx = SpringApplication.run(SalesPurchRemover.class, args);
		SalesPurchRemover mig = ctx.getBean(SalesPurchRemover.class);

		mig.doMigration();
	}

	public void doMigration()
	{
		List<Sales> salesToDelete = salesRepository.findAllBySalesTxnIsNull();
		for (Sales sales : salesToDelete)
		{
			LOGGER.debug(sales);
		}
		List<Purchase> purchasesToDelete = purchaseRepository.findAllBySalesTxnIsNull();
		for (Purchase purchase : purchasesToDelete) {
			LOGGER.debug(purchase);
		}
	}
}
