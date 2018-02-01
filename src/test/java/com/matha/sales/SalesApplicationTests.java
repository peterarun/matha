package com.matha.sales;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.matha.domain.PurchaseTransaction;
import com.matha.repository.PurchaseTxnRepository;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SalesApplicationTests {

	@Autowired
	PurchaseTxnRepository pRepo;
	
	@Test
	public void generateJasper() throws FileNotFoundException, JRException
	{
		OutputStream outputStream = new FileOutputStream("/Simple_Blue.jasper");
		JasperCompileManager.compileReportToStream(getClass().getResourceAsStream("/jrxml/Simple_Blue.jrxml"), outputStream);;			
		
	}
	
	@Test
	public void testJrxml() {

//		String fileName = "classpath:/jrxml/Invoice.jrxml";
		InputStream jasperStream = getClass().getResourceAsStream("/jrxml/Invoice.jrxml");
		String outFileName = "/test.pdf";
		HashMap hm = new HashMap();
		try
		{
//			OutputStream outputStream = new FileOutputStream("/Simple_Blue.jasper");
//			JasperCompileManager.compileReportToStream(getClass().getResourceAsStream("/jrxml/Simple_Blue.jrxml"), outputStream);;			
			ArrayList<PurchaseTransaction> tableData = new ArrayList<>(pRepo.findAll());
			
			hm.put("reportData", tableData);
//			JRBeanCollectionDataSource bs = new JRBeanCollectionDataSource(tableData);
			JasperReport compiledFile = JasperCompileManager.compileReport(jasperStream );
		
			JasperPrint print = JasperFillManager.fillReport(
				compiledFile,
				hm);
			JRExporter exporter = 
				new net.sf.jasperreports.engine.export.JRPdfExporter();

			exporter.setParameter(
				JRExporterParameter.OUTPUT_FILE_NAME,
				outFileName);
			exporter.setParameter(
			JRExporterParameter.JASPER_PRINT,print);
			exporter.exportReport();
			System.out.println("Created file: " + outFileName);
		}
		catch (JRException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}	
	}

}
