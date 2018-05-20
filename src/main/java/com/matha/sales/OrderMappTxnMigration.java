package com.matha.sales;

import com.matha.domain.*;
import com.matha.repository.OrderRepository;
import com.matha.repository.PublisherRepository;
import com.matha.repository.PurchaseDetRepository;
import com.matha.repository.PurchaseRepository;
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

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.Collections.max;
import static java.util.Comparator.comparingDouble;
import static java.util.stream.Collectors.*;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.matha.repository")
@EntityScan("com.matha.domain")
@ComponentScan(basePackages = { "com.matha.controller", "com.matha.service"})
@EnableAutoConfiguration
public class OrderMappTxnMigration
{
	private static final Logger LOGGER = LogManager.getLogger(OrderMappTxnMigration.class);
	private static final int FIN_YEAR = 11;

	@Autowired
	private PublisherRepository publisherRepository;

	@Autowired
	private OrderRepository orderRepo;

	@Autowired
	private PurchaseRepository purchaseRepository;

	@Autowired
	private PurchaseDetRepository purchaseDetRepository;

	public static ConfigurableApplicationContext ctx;

	public static void main(String[] args)
	{
		ctx = SpringApplication.run(OrderMappTxnMigration.class, args);
		OrderMappTxnMigration mig = ctx.getBean(OrderMappTxnMigration.class);
		Integer finYear = FIN_YEAR;
		if(args != null && args.length > 0 && args[0] != null)
		{
			finYear = Integer.parseInt(args[0]);
		}
		mig.doMigration(finYear);
	}

	public void doMigration(Integer finYear)
	{
//		List<School> schoolList = schoolRepository.findAllByNameLike("A%");
//		List<School> schoolList = schoolRepository.findTop10ByNameLike("A%");
		List<Publisher> schoolList = publisherRepository.findAll();
		for (Publisher sc : schoolList)
		{
			LOGGER.debug(sc.toBasicString());

			List<Order> ordList = orderRepo.fetchOrdersForPublisherAndFy(sc, finYear);
			Supplier<Stream<OrderItem>> orderStreamSupp = () -> ordList.stream()
					.map(o -> o.getOrderItem())
					.flatMap(List<OrderItem>::stream);
			Map<String, Set<String>> ordMap = orderStreamSupp.get().collect(groupingBy(OrderItem::getOrderId, mapping(o -> o.getBook().getBookNum(), toSet())));
			Map<String, Order> orderMap = ordList.stream()
					.collect(toMap(o -> o.getId(), o -> o));
			LOGGER.debug("orderEnt");
			for (Map.Entry<String, Set<String>> orderEnt : ordMap.entrySet())
			{
				LOGGER.debug(orderEnt.getKey() + " " + orderEnt.getValue());
			}

			List<Purchase> sales = purchaseRepository.findAllByPublisherAndFinancialYear(sc, finYear);
			Supplier<Stream<PurchaseDet>> saleStreamSupp = () -> sales.stream()
					.map(o -> o.getPurchaseItems())
					.flatMap(Set<PurchaseDet>::stream);
			Map<String, Set<String>> saleMap = saleStreamSupp.get().collect(groupingBy(sd -> sd.getPurchase().getId(), mapping(sd -> sd.getBook().getBookNum(), toSet())));
			Map<String, Purchase> salesMap = sales.stream()
					.collect(toMap(o -> o.getId(), o -> o));
			LOGGER.debug("salesEnt");
			for (Map.Entry<String, Set<String>> salesEnt : saleMap.entrySet())
			{
				LOGGER.debug(salesEnt.getKey() + " " + salesEnt.getValue());
			}

			Map<String, String> corrMap = findCorrelation(ordMap, saleMap);
			LOGGER.debug("corrMap");
			for (Map.Entry<String, String> corrEnt : corrMap.entrySet())
			{
				LOGGER.debug(corrEnt.getKey() + " - " + corrEnt.getValue());
				Purchase currSale = salesMap.get(corrEnt.getValue());
				Set<PurchaseDet> saleItems = currSale.getPurchaseItems();
				for (PurchaseDet saleItem : saleItems)
				{
					Order order = orderMap.get(corrEnt.getKey());
					List<OrderItem> orderItems = order.getOrderItem();
					Optional<OrderItem> foundBook = orderItems.stream().filter(o -> o.getBook().getBookNum().equals(saleItem.getBook().getBookNum())).findFirst();
					if(foundBook.isPresent())
					{
						saleItem.setOrderItem(foundBook.get());
						LOGGER.debug("Mapping " + saleItem.getPurDetId() + " to " + foundBook.get().getId());
						purchaseDetRepository.save(saleItem);
					}
				}
			}
		}
	}

	public Map<String, String> findCorrelation(Map<String, Set<String>> ordMap, Map<String, Set<String>> saleMap)
	{
		Map<String, String> correlationMap = new HashMap<>();
		if(ordMap == null || ordMap.isEmpty() || saleMap == null || saleMap.isEmpty())
		{
			return correlationMap;
		}

		for (Map.Entry<String, Set<String>> ordEntry : ordMap.entrySet())
		{
			Set<String> ordBooks = ordEntry.getValue();
			Map<String, Double> corrMap = new HashMap<>();
			for (Map.Entry<String, Set<String>> saleEntry : saleMap.entrySet())
			{
				Set<String> saleBooks = saleEntry.getValue();

				HashSet<String> ordBooksDup = new HashSet<String>(ordBooks);
//				LOGGER.debug("ordBooksDup 1: " + ordBooksDup);
				int totSize = ordBooksDup.size();
//				LOGGER.debug("saleBooks: " + saleBooks);
				ordBooksDup.retainAll(saleBooks);
//				LOGGER.debug("ordBooksDup 2: " + ordBooksDup);
				int retSize = ordBooksDup.size();
				double corrVal = (double)retSize / totSize;
//				LOGGER.debug("corrVal:" + corrVal);
				corrMap.put(saleEntry.getKey(), corrVal);
			}
			String corrSaleKey = max(corrMap.entrySet(), comparingDouble(Map.Entry::getValue)).getKey();
			correlationMap.put(ordEntry.getKey(), corrSaleKey);
		}
		return correlationMap;
	}

}
