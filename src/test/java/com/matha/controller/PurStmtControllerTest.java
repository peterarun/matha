package com.matha.controller;

import com.matha.domain.SalesTransaction;
import com.matha.domain.School;
import com.matha.repository.SchoolRepository;
import com.matha.sales.SalesApplication;
import com.matha.service.SchoolService;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
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

import java.io.File;
import java.time.LocalDate;
import java.util.List;

@SpringBootTest(classes = SalesApplication.class)
@RunWith(SpringRunner.class)
public class PurStmtControllerTest
{

	private static final Logger LOGGER = LogManager.getLogger(PurStmtControllerTest.class);
	
	@Autowired
	private SchoolService schoolService;

	@Autowired
	private SchoolRepository schoolRepoitory;
	
	@Autowired
	private PrintSalesStmtController printSalesStmtController;

	@Before
	public void setUp()
	{
		Configurator.setLevel("com.matha",Level.DEBUG);
	}
	
	@Test
	public void testPrintStmt()
	{
		try
		{
			List<School> schools = schoolRepoitory.findByNameLike("BAPU%");
			for (School school : schools)
			{
				LOGGER.info(school);
			}

			LocalDate fromDateVal = LocalDate.now().minusMonths(6);
			LocalDate toDateVal = LocalDate.now();
			List<SalesTransaction> tableData = schoolService.fetchTransactions(schools.get(0), fromDateVal, toDateVal);
			JasperPrint jPrint = printSalesStmtController.prepareJasperPrint(schools.get(0), tableData, fromDateVal, toDateVal);
			File fileIn = new File("E:\\Arun\\Bapuji_Statement_Saved.pdf");
			JasperExportManager.exportReportToPdfFile(jPrint, fileIn.getAbsolutePath());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
