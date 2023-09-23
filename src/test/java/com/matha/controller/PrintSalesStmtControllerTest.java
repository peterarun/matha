package com.matha.controller;

import java.io.File;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import com.matha.domain.Publisher;
import com.matha.domain.PurchaseTransaction;
import com.matha.repository.PublisherRepository;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.matha.domain.SalesTransaction;
import com.matha.domain.School;
import com.matha.repository.SchoolRepository;
import com.matha.sales.SalesApplication;
import com.matha.service.SchoolService;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;

import static com.matha.util.UtilConstants.statementJrxml;

@SpringBootTest(classes = SalesApplication.class)
@RunWith(SpringRunner.class)
@ActiveProfiles("vklmbooks")
public class PrintSalesStmtControllerTest
{

	private static final Logger LOGGER = LogManager.getLogger(PrintSalesStmtControllerTest.class);
	
	@Autowired
	private SchoolService schoolService;

	@Autowired
	private SchoolRepository schoolRepoitory;
	
	@Autowired
	private PrintSalesStmtController printSalesStmtController;

	@Autowired
	private PublisherRepository publisherRepository;

	@Value("${agencyName}")
	private String agencyName;

	@Value("#{'${datedPurPaymentModes}'.split(',')}")
	private List<String> datedSchoolPaymentModes;

	@Before
	public void setUp()
	{
		Configurator.setLevel("com.matha",Level.DEBUG);
	}
	
	@Test
	public void testPrintStmt() throws Exception
	{

		JasperPrint jasperPrint = null;
		InputStream jasperStream = getClass().getResourceAsStream(statementJrxml);
		HashMap<String, Object> hm = new HashMap<>();

		LocalDate fromDateVal = LocalDate.now().minusMonths(2);
		LocalDate toDateVal = LocalDate.now();

		Sort sort = Sort.by(new Sort.Order(Sort.Direction.ASC, "id"), new Sort.Order(Sort.Direction.ASC, "txnDate"));
		Publisher pub = publisherRepository.findById(48).orElseThrow(() -> new Exception("Cant get publisher"));

		List<PurchaseTransaction> tableData = schoolService.fetchPurTransactions(pub, fromDateVal, toDateVal, sort);
		Double openingBalance = 0.0;
		if (tableData != null && !tableData.isEmpty())
		{
			PurchaseTransaction prevTxn = schoolService.fetchPrevTxn(tableData.get(0));
			if (prevTxn != null)
			{
				openingBalance = prevTxn.getBalance();
			}
		}
		Double totalDebit = tableData.stream().collect(Collectors.summingDouble(o -> o.getMultiplier() == 1 ? o.getAmount() : 0.0));
		Double totalCredit = tableData.stream().collect(Collectors.summingDouble(o -> o.getMultiplier() == -1 ? o.getAmount() : 0.0));
		hm.put("openingBalance", openingBalance);
//			hm.put("reportData", tableData);
		hm.put("publisherName", pub.getName());
		hm.put("publisherDetails", pub.getStmtAddress());
		hm.put("fromDate", fromDateVal);
		hm.put("toDate", toDateVal);
		hm.put("accountDetails", agencyName);
		hm.put("totalDebit", totalDebit);
		hm.put("totalCredit", totalCredit);
		hm.put("datedSchoolPaymentModes", datedSchoolPaymentModes);
//			JasperReport compiledFile = JasperCompileManager.compileReport(jasperStream);

		jasperPrint = JasperFillManager.fillReport(jasperStream, hm, new JRBeanCollectionDataSource(tableData));

		File fileIn = new File("F:\\Work\\Matha\\temp\\Mayur_Statement_Saved.pdf");
		JasperExportManager.exportReportToPdfFile(jasperPrint, fileIn.getAbsolutePath());

	}

}
