package com.matha.sales;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.junit4.SpringRunner;

import com.matha.controller.PurchaseController;
import com.matha.domain.Publisher;
import com.matha.domain.PurchaseTransaction;
import com.matha.repository.PublisherRepository;
import com.matha.repository.PurchaseTxnRepository;
import com.matha.service.SchoolService;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SalesApplicationTests
{

	@Autowired
	PurchaseTxnRepository pRepo;

	@Autowired
	SchoolService schoolService;

	@Autowired
	PublisherRepository pubRepo;
	
	@Test
	public void generateJasper() throws FileNotFoundException, JRException
	{
		OutputStream outputStream = new FileOutputStream("/Simple_Blue.jasper");
		JasperCompileManager.compileReportToStream(getClass().getResourceAsStream("/jrxml/Simple_Blue.jrxml"), outputStream);
		;

	}

	@Test
	public void testJrxml()
	{

		String outFileName = "/test.pdf";

		JasperPrint jasperPrint = null;
		InputStream jasperStream = getClass().getResourceAsStream("/jrxml/Invoice.jrxml");
		HashMap<String, Object> hm = new HashMap<>();
		try
		{
			LocalDate toDateVal = LocalDate.now();
			LocalDate fromDateVal = toDateVal.minusMonths(6);

			Sort sort = new Sort(new Sort.Order(Direction.ASC, "txnDate"), new Sort.Order(Direction.ASC, "id"));
			Publisher pub = pubRepo.findOne("45");
			List<PurchaseTransaction> tableData = schoolService.fetchPurTransactions(pub, fromDateVal, toDateVal, sort);
			System.out.println(tableData);
			Double openingBalance = 0.0;
			if(tableData != null && !tableData.isEmpty())
			{
				if(tableData.get(0).getPrevTxn() != null)
				{
					openingBalance = tableData.get(0).getPrevTxn().getBalance();
				}
			}
			hm.put("openingBalance", openingBalance);
			hm.put("reportData", tableData);
			hm.put("publisherName", pub.getName());
			hm.put("publisherDetails", pub.getStmtAddress());
			hm.put("fromDate", fromDateVal);
			hm.put("toDate", toDateVal);
			hm.put("accountDetails", "Matha Distributors (Chennai)");
			hm.put("totalDebit", 120232.34);
			hm.put("totalCredit", 439823.34);
			JasperReport compiledFile = JasperCompileManager.compileReport(jasperStream);

			jasperPrint = JasperFillManager.fillReport(compiledFile, hm);
			
			JasperExportManager.exportReportToPdfFile(jasperPrint, outFileName);

		}
		catch (JRException e)
		{
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
