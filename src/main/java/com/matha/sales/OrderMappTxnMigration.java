package com.matha.sales;

import com.matha.domain.*;
import com.matha.repository.*;
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
//	private static final int FIN_YEAR = 11;

	@Autowired
	private SchoolRepository schoolRepository;

	@Autowired
	private SalesRepository salesRepository;

	@Autowired
	private SalesDetRepository salesDetRepository;

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
//		Integer finYear = FIN_YEAR;
		if(args != null && args.length > 0 && args[0] != null)
		{
//			finYear = Integer.parseInt(args[0]);
		}
		Configurator.setLevel("com.matha", Level.DEBUG);
		LocalDate orderMinDate = LocalDate.of(2017, Month.OCTOBER, 1);
//		LOGGER.debug("Running Sales Mapping for FY: " + orderMinDate);
//		mig.doSalesMigration(orderMinDate);

		LOGGER.debug("Running Purchase Mapping for FY: " + orderMinDate);
		mig.doPurchMigration(orderMinDate);
	}

	public void doSalesMigration(LocalDate orderMinDate)
	{
//		List<School> schoolList = schoolRepository.findAllByNameLike("A%");
//		List<School> schoolList = schoolRepository.findTop10ByNameLike("A%");
		List<School> schoolList = schoolRepository.fetchSchools(0);
		for (School sc : schoolList)
		{
			LOGGER.debug(sc.toBasicString());

//			List<Order> ordList = orderRepo.findAllBySchoolAndFinancialYearOrderByOrderDateDesc(sc, finYear);
			List<Order> ordList = orderRepo.findAllBySchoolAndOrderDateAfterOrderByOrderDateDesc(sc, orderMinDate);
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


			Sort sortIn = new Sort(new Sort.Order(Sort.Direction.DESC, "txnDate"));
			LocalDate salesMinDate = LocalDate.of(2018, Month.FEBRUARY, 1);
			List<Sales> sales = salesRepository.findAllBySchoolAndTxnDateAfter(sc, salesMinDate, sortIn);
			Supplier<Stream<SalesDet>> saleStreamSupp = () -> sales.stream()
					.map(o -> o.getSaleItems())
					.flatMap(Set<SalesDet>::stream);
			Map<String, Set<String>> saleMap = saleStreamSupp.get().collect(groupingBy(sd -> sd.getSale().getId(), mapping(sd -> sd.getBook().getBookNum(), toSet())));
			Map<Integer, SalesDet> saleDetMap = saleStreamSupp.get().collect(toMap(sd -> sd.getSalesDetId(), sd -> sd));
			Map<String, Sales> salesMap = sales.stream()
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
				Sales currSale = salesMap.get(corrEnt.getValue());
				Set<SalesDet> saleItems = currSale.getSaleItems();
				for (SalesDet saleItem : saleItems)
				{
					Order order = orderMap.get(corrEnt.getKey());
					List<OrderItem> orderItems = order.getOrderItem();
					Optional<OrderItem> foundBook = orderItems.stream().filter(o -> o.getBook().getBookNum().equals(saleItem.getBook().getBookNum())).findFirst();
					if(foundBook.isPresent())
					{
						saleItem.setOrderItem(foundBook.get());
						LOGGER.debug("Mapping " + saleItem.getSalesDetId() + " to " + foundBook.get().getId());
						salesDetRepository.save(saleItem);
					}
				}
			}
		}
	}

	public void doPurchMigration(LocalDate orderMinDate)
	{
		List<Publisher> schoolList = publisherRepository.findAll();
		for (Publisher sc : schoolList)
		{
			LOGGER.debug(sc.toBasicString());

			Sort sortIn = new Sort(new Sort.Order(Sort.Direction.DESC, "orderDate"));
			List<Order> ordList = orderRepo.findAllByPublisherAndOrderDateAfter(sc, orderMinDate, sortIn);
			Supplier<Stream<OrderItem>> orderStreamSupp = () -> ordList.stream()
					.map(o -> o.getOrderItem())
					.flatMap(List<OrderItem>::stream);
			Map<String, Set<String>> ordMap = orderStreamSupp.get().filter(oi -> oi.getBook().getPublisher().getId().equals(sc.getId())).collect(groupingBy(OrderItem::getOrderId, mapping(o -> o.getBook().getBookNum(), toSet())));
			Map<String, Order> orderMap = ordList.stream()
					.collect(toMap(o -> o.getId(), o -> o));
			LOGGER.debug("orderEnt");
			for (Map.Entry<String, Set<String>> orderEnt : ordMap.entrySet())
			{
				LOGGER.debug(orderEnt.getKey() + " " + orderEnt.getValue());
			}

			LocalDate purchaseMinDate = LocalDate.of(2017, Month.OCTOBER, 1);
			Sort purSortIn = new Sort(new Sort.Order(Sort.Direction.DESC, "purchaseDate"));
			List<Purchase> sales = purchaseRepository.findAllByPublisherAndPurchaseDateAfter(sc, purchaseMinDate, purSortIn);
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
				Order order = orderMap.get(corrEnt.getKey());
				List<OrderItem> orderItems = order.getOrderItem();
				Map<String, OrderItem> ordMapIn = orderItems.stream().collect(toMap(oi -> oi.getBook().getBookNum(), oi -> oi));

				for (PurchaseDet saleItem : saleItems)
				{
					OrderItem itemIn = ordMapIn.get(saleItem.getBook().getBookNum());
					if(itemIn != null)
					{
						saleItem.setOrderItem(itemIn);
//						LOGGER.debug("Mapping " + saleItem.getPurDetId() + " to " + foundBook.get().getId());
						purchaseDetRepository.save(saleItem);
						orderItems.remove(itemIn);
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

		for (int i = 0; i < 2; i++)
		{
			for (Map.Entry<String, Set<String>> ordEntry : ordMap.entrySet())
			{
				Set<String> ordBooks = ordEntry.getValue();
				if(ordBooks.isEmpty())
				{
					continue;
				}
				LOGGER.debug("ordBooks:");
				LOGGER.debug(ordBooks);
				Map<String, Double> corrMap = new HashMap<>();
				for (Map.Entry<String, Set<String>> saleEntry : saleMap.entrySet())
				{
					Set<String> saleBooks = saleEntry.getValue();
					if(saleBooks.isEmpty())
					{
						continue;
					}
					LOGGER.debug("saleBooks:");
					LOGGER.debug(saleBooks);
					long retSize = ordBooks.stream().filter(o -> saleBooks.contains(o)).count();
					if (retSize == 0)
					{
						continue;
					}
					double corrVal = (double) retSize / saleBooks.size();
					LOGGER.debug("corrVal:" + corrVal);
					corrMap.put(saleEntry.getKey(), corrVal);
					if(corrVal == 1.0)
					{
						break;
					}
				}
				if (corrMap.isEmpty())
				{
					LOGGER.debug("No Correlation");
					continue;
				}
				String corrSaleKey = max(corrMap.entrySet(), comparingDouble(Map.Entry::getValue)).getKey();
				correlationMap.put(ordEntry.getKey(), corrSaleKey);
				Set<String> saleSet = saleMap.get(corrSaleKey);
				LOGGER.debug("Correlation Found");
				LOGGER.debug("saleSet:");
				LOGGER.debug(saleSet);

				HashSet<String> ordBooksDup = new HashSet<>(ordBooks);
				ordBooks.removeAll(saleSet);
				saleSet.removeAll(ordBooksDup);
			}
		}

		return correlationMap;
	}
}
