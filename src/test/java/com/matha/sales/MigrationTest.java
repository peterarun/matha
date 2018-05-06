package com.matha.sales;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.Supplier;
import java.util.stream.Stream;

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

import com.matha.domain.Order;
import com.matha.domain.OrderItem;
import com.matha.domain.Sales;
import com.matha.domain.SalesDet;
import com.matha.domain.School;
import com.matha.repository.OrderItemRepository;
import com.matha.repository.OrderRepository;
import com.matha.repository.PublisherRepository;
import com.matha.repository.PurchaseTxnRepository;
import com.matha.repository.SalesDetRepository;
import com.matha.repository.SalesRepository;
import com.matha.repository.SchoolRepository;
import com.matha.util.LogUtil;

import static java.util.stream.Collectors.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MigrationTest
{
	
	private static final Logger LOGGER = LogManager.getLogger(LogUtil.class);
	private static final int FIN_YEAR = 9;

	@Autowired
	PurchaseTxnRepository pRepo;

	@Autowired
	PublisherRepository pubRepo;
	
	@Autowired
	OrderRepository orderRepo;
	
	@Autowired
	OrderItemRepository orderItemRepo;
	
	@Autowired
	SchoolRepository schoolRepository;
	
	@Autowired
	SalesRepository salesRepository;

	@Autowired
	SalesDetRepository salesDetRepository;
	
	@Before
	public void setUp()
	{
		Configurator.setLevel("com.matha", Level.DEBUG);
	}
	
	@Test
	public void testMigration()
	{
		List<School> schoolList = schoolRepository.findTop10ByNameLike("B%");
//		List<School> schoolList = schoolRepository.findAll();
		for (School sc : schoolList)
		{
			LOGGER.debug("School: " + sc);
			List<Order> ordList = orderRepo.findAllBySchoolOrderByOrderDateDesc(sc);
			Map<String, Set<String>> ordMap = ordList.stream().filter((Order o) -> o.getFinancialYear() == FIN_YEAR).map(o -> o.getOrderItem()).flatMap(List<OrderItem>::stream).collect(groupingBy(OrderItem::getOrderId, mapping(o -> o.getBook().getBookNum(), toSet())));
			LOGGER.debug("orderEnt");
			for (Entry<String, Set<String>> orderEnt : ordMap.entrySet())
			{
				LOGGER.debug(orderEnt.getKey() + " " + orderEnt.getValue());
			}
//			for (Order order : ordList)
//			{
//				if(order.getFinancialYear() == 9)
//				{
//					List<OrderItem> orddItem = order.getPurchaseReturnDetSet();
//					LOGGER.debug(orddItem.size());
//					for (OrderItem orderItemIn : orddItem)
//					{
//						LOGGER.debug(orderItemIn.toStringMig());
//					}
////					LogUtil.logListDebug(orddItem);
//					orddItem.stream().map(OrderItem::getBook).map(Book::getBookNum).collect(Collectors.toSet());
//				}
//			}
			
			List<Sales> sales = salesRepository.findAllBySchool(sc);
//			Map<String, Set<String>> saleMap = sales.stream().filter(s -> s.getFinancialYear() == 9).map(s -> s.getSaleItems()).flatMap(Set<SalesDet>::stream).collect(toMap(s -> s.getSchoolReturn().getId(), s -> s.getDetId().getBookNum()));
//			List<SalesDet> sLit = new ArrayList<>();
//			sLit.stream().collect(toMap(s -> s.getSchoolReturn().getId(), s -> s.getDetId().getBookNum()));
//			for (Entry<String, Set<String>> saleEnt : saleMap.entrySet())
//			{
//				LOGGER.debug(saleEnt.getKey() + " " + saleEnt.getValue());
//			}
			
			Map<String, Set<String>> saleMap = new HashMap<>();
			for (Sales salesIn : sales)
			{
				if(salesIn.getFinancialYear() == 9)
				{
//					LOGGER.debug(salesIn);
//					List<SalesDet> detList = salesDetRepository.findAllBySerialId(salesIn.getId());
//					LOGGER.debug(detList.size());
//					for (SalesDet salesDetIn : detList)
//					{
//						LOGGER.debug(salesDetIn.toStringMig());	
//					}
//					LogUtil.logListDebug(detList);
					String saleId = salesIn.getId();
					if(!saleMap.containsKey(saleId))
					{
						saleMap.put(saleId, new HashSet<>());						
					}
					Set<String> salesDetIn = salesIn.getSaleItems().stream()
							.map(sd -> sd.getBook().getBookNum())
							.collect(toSet());
					saleMap.get(saleId).addAll(salesDetIn);
				}
			}
			LOGGER.debug("salesEnt");
			for (Entry<String, Set<String>> salesEnt : saleMap.entrySet())
			{
				LOGGER.debug(salesEnt.getKey() + " " + salesEnt.getValue());
			}
			
			Map<String, String> corrMap = findCorrelation(ordMap, saleMap);
			LOGGER.debug("corrMap");
			for (Entry<String, String> corrEnt : corrMap.entrySet())
			{
				LOGGER.debug(corrEnt.getKey() + " - " + corrEnt.getValue());
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
		
		for (Entry<String, Set<String>> ordEntry : ordMap.entrySet())
		{
			Set<String> ordBooks = ordEntry.getValue();
			Map<String, Double> corrMap = new HashMap<>();
			for (Entry<String, Set<String>> saleEntry : saleMap.entrySet())
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
			String corrSaleKey = Collections.max(corrMap.entrySet(), Comparator.comparingDouble(Map.Entry::getValue)).getKey();
			correlationMap.put(ordEntry.getKey(), corrSaleKey);
		}
		return correlationMap;
	}

	@Test
	public void testMigration2()
	{
		int financialYear = FIN_YEAR;
//		List<School> schoolList = schoolRepository.findAllByNameLike("A%");
//		List<School> schoolList = schoolRepository.findTop10ByNameLike("A%");
		List<School> schoolList = schoolRepository.findAll();
		for (School sc : schoolList)
		{
			LOGGER.debug(sc.toBasicString());

			List<Order> ordList = orderRepo.findAllBySchoolAndFinancialYearOrderByOrderDateDesc(sc, financialYear);
			Supplier<Stream<OrderItem>> orderStreamSupp = () -> ordList.stream()
					.map(o -> o.getOrderItem())
					.flatMap(List<OrderItem>::stream);
			Map<String, Set<String>> ordMap = orderStreamSupp.get().collect(groupingBy(OrderItem::getOrderId, mapping(o -> o.getBook().getBookNum(), toSet())));
			Map<String, Order> orderMap = ordList.stream()
					.collect(toMap(o -> o.getId(), o -> o));
			LOGGER.debug("orderEnt");
			for (Entry<String, Set<String>> orderEnt : ordMap.entrySet())
			{
				LOGGER.debug(orderEnt.getKey() + " " + orderEnt.getValue());
			}

			List<Sales> sales = salesRepository.findAllBySchoolAndFinancialYear(sc, financialYear);
			Supplier<Stream<SalesDet>> saleStreamSupp = () -> sales.stream()
					.map(o -> o.getSaleItems())
					.flatMap(Set<SalesDet>::stream);
			Map<String, Set<String>> saleMap = saleStreamSupp.get().collect(groupingBy(sd -> sd.getSale().getId(), mapping(sd -> sd.getBook().getBookNum(), toSet())));
			Map<Integer, SalesDet> saleDetMap = saleStreamSupp.get().collect(toMap(sd -> sd.getSalesDetId(), sd -> sd));
			Map<String, Sales> salesMap = sales.stream()
					.collect(toMap(o -> o.getId(), o -> o));
			LOGGER.debug("salesEnt");
			for (Entry<String, Set<String>> salesEnt : saleMap.entrySet())
			{
				LOGGER.debug(salesEnt.getKey() + " " + salesEnt.getValue());
			}

			Map<String, String> corrMap = findCorrelation(ordMap, saleMap);
			LOGGER.debug("corrMap");
			for (Entry<String, String> corrEnt : corrMap.entrySet())
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

}
