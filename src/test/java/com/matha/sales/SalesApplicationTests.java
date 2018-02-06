package com.matha.sales;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.junit4.SpringRunner;

import com.matha.domain.OrderItem;
import com.matha.domain.Publisher;
import com.matha.domain.PurchaseTransaction;
import com.matha.repository.OrderItemRepository;
import com.matha.repository.PublisherRepository;
import com.matha.repository.PurchaseTxnRepository;
import com.matha.service.SchoolService;
import com.matha.util.UtilConstants;

import net.sf.jasperreports.engine.JRException;
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
	
	@Autowired
	OrderItemRepository orderItemRepo;

	@Test
	public void testJrxml()
	{

		String outFileName = "/test.pdf";

		JasperPrint jasperPrint = null;
		InputStream jasperStream = getClass().getResourceAsStream(UtilConstants.statementJrxml);
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

	@Test
	public void testInvoice()
	{

		String outFileName = "/testInv.pdf";

		JasperPrint jasperPrint = null;
		InputStream jasperStream = getClass().getResourceAsStream(UtilConstants.invoiceJrxml);
		HashMap<String, Object> hm = new HashMap<>();
		try
		{
			LocalDate toDateVal = LocalDate.now();
			LocalDate fromDateVal = toDateVal.minusMonths(6);

			Sort sort = new Sort(new Sort.Order(Direction.ASC, "txnDate"), new Sort.Order(Direction.ASC, "id"));
			Publisher pub = pubRepo.findOne("45");
			List<OrderItem> tableData = orderItemRepo.findAllByPurchaseIsNotNull();
			System.out.println(tableData);
			
			hm.put("reportData", tableData);
			hm.put("publisherName", pub.getName());
			hm.put("publisherDetails", pub.getStmtAddress());
			hm.put("partyName", "Matha");
			hm.put("partyAddress", "Chennai");
			hm.put("partyPhone", "934376232");
			hm.put("documentsThrough", "DIRECT");
			hm.put("despatchedTo", "SOmone");
			hm.put("invoiceNo", "2322M");
			hm.put("txnDate", "14-08-2017");
			hm.put("orderNumbers", "2223,5343/T,3234");
			hm.put("despatchedPer", "Person");
			hm.put("grNo", "3435232");
			hm.put("packageCnt", "5");
			hm.put("total", 20500.00);
			hm.put("discount", 500.00);
			hm.put("grandTotal", 20000.00);
			hm.put("imageFileName","Mayur_logo.jpg");
			
			JasperReport compiledFile = JasperCompileManager.compileReport(jasperStream);

			jasperPrint = JasperFillManager.fillReport(compiledFile, hm, new JRBeanCollectionDataSource(tableData));
			
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
