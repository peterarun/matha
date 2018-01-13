package com.matha.service;

import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.matha.domain.Order;
import com.matha.domain.School;
import com.matha.sales.SalesApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SalesApplication.class)
public class SchoolServiceTest {

	@Autowired
	private SchoolService schoolService;

	@Test
	public void testFetchSchoolsLike() {
		fail("Not yet implemented");
	}

	@Test
	public void testFetchAllSchools() {
		fail("Not yet implemented");
	}

	@Test
	public void testInsertSchool() {
		fail("Not yet implemented");
	}

	@Test
	public void testFetchAllStates() {
		fail("Not yet implemented");
	}

	@Test
	public void testFetchAllDistricts() {
		fail("Not yet implemented");
	}

	@Test
	public void testFetchAllBooks() {
		fail("Not yet implemented");
	}

	@Test
	public void testSaveOrder() {
		fail("Not yet implemented");
	}

	@Test
	public void testFetchOrderItems() {
		fail("Not yet implemented");
	}

	@Test
	public void testFetchOrders() {
		fail("Not yet implemented");
	}

	@Test
	public void testFetchOrderForSchool() {
		School school = schoolService.fetchSchoolById("412");
		List<Order> orderList = schoolService.fetchOrderForSchool(school);
		System.out.println(orderList);
	}

}
