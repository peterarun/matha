package com.matha.sales;

import com.matha.domain.*;
import com.matha.repository.SalesRepository;
import com.matha.service.SchoolService;
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

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.matha.util.UtilConstants.EMPTY_STR;
import static com.matha.util.UtilConstants.SALES_NOTE;
import static com.matha.util.UtilConstants.SEMI_COLON_SIGN;
import static com.matha.util.Utils.calcFinYear;
import static com.matha.util.Utils.getDoubleVal;
import static com.matha.util.Utils.getIntegerVal;
import static java.util.stream.Collectors.toMap;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.matha.repository")
@EntityScan("com.matha.domain")
@ComponentScan(basePackages = { "com.matha.controller", "com.matha.service"})
@EnableAutoConfiguration
public class SalesCopier
{
	private static final Logger LOGGER = LogManager.getLogger(SalesCopier.class);
	private static final int FIN_YEAR = 11;

	@Autowired
	private SalesRepository salesRepository;

	@Autowired
	private SchoolService schoolService;

	public static ConfigurableApplicationContext ctx;

	public static void main(String[] args) throws IOException
	{
		ctx = SpringApplication.run(SalesCopier.class, args);
		SalesCopier mig = ctx.getBean(SalesCopier.class);

		String fileName = args[0];
		mig.doMigration(fileName);
	}

	public void doMigration(String fileName) throws IOException
	{
		BufferedReader fis = new BufferedReader(new FileReader(new File(fileName)));
		HashMap<Integer, School> schoolHashMap = new HashMap<>();
		List<Book> books = schoolService.fetchAllBooks();
		Map<String, Book> bookMap = books.stream().collect(toMap(Book::getBookNum, b -> b));
		List<SalesDet> orderItems = new ArrayList<>();
		String currLine = fis.readLine();
		String currSerialId = null;
		do
		{
			String[] lineCont = currLine.split(SEMI_COLON_SIGN);
			if(currSerialId != null && !currSerialId.equals(lineCont[0]))
			{
				Sales sale = prepareSale(lineCont, schoolHashMap);
				saveSale(sale, orderItems);

				currSerialId = lineCont[0];
				orderItems = new ArrayList<>();
			}

			SalesDet sd = prepareSalesDet(lineCont, bookMap);
			orderItems.add(sd);

			if((currLine = fis.readLine()) == null)
			{
				Sales sale = prepareSale(lineCont, schoolHashMap);
				saveSale(sale, orderItems);
			}
		}
		while (currLine != null);
	}

	public void saveSale(Sales sale, List<SalesDet> orderItems)
	{
		SalesTransaction salesTxn = sale.getSalesTxn();
		sale.setSalesTxn(null);
		Sales saleOut = schoolService.saveSalesData(sale, orderItems, salesTxn);
		LOGGER.info("Saved: " + saleOut.getId());
	}

	public Sales prepareSale(String[] lineCont, HashMap<Integer, School> schoolHashMap)
	{
		Integer invoiceNum = getIntegerVal(lineCont[1]);
		LocalDate ld = LocalDate.parse(lineCont[2].trim().substring(0,10),DateTimeFormatter.ISO_DATE);
		Integer schoolId = getIntegerVal(lineCont[3].trim());
		String despatchPer = lineCont[4];
		String docsThru = lineCont[5];
		String grNum = lineCont[6];
		String packageCnt = lineCont[7];
		Double subTotalVal = getDoubleVal(lineCont[8]);
		Double discAmtVal = getDoubleVal(lineCont[10]);
		Double others = getDoubleVal(lineCont[11]);
		Integer fy = getIntegerVal(lineCont[12]);
		String noteVal = lineCont[13];
		Double netAmtVal = getDoubleVal(lineCont[14]);

		if(!schoolHashMap.containsKey(schoolId))
		{
			School school = schoolService.fetchSchoolById(schoolId);
			schoolHashMap.put(schoolId, school);
		}
		School school = schoolHashMap.get(schoolId);

		Sales sale = new Sales();
		sale.setTxnDate(ld);
		sale.setFinancialYear(calcFinYear(sale.getTxnDate()));
		sale.setSchool(school);
		SalesTransaction salesTxn = new SalesTransaction();
		salesTxn.setSchool(school);

		sale.setDiscAmt(discAmtVal);
		sale.setDiscType(false);

		sale.setSubTotal(subTotalVal);
		salesTxn.setAmount(netAmtVal);
		sale.setDeletedAmt(netAmtVal);

		salesTxn.setNote(noteVal);
		salesTxn.setTxnDate(ld);

		sale.setDespatch(despatchPer);
		sale.setDocsThru(docsThru);
		sale.setGrNum(grNum);
		sale.setSerialNo(invoiceNum);
		sale.setPackages(packageCnt);
		sale.setOtherAmount(others);
		sale.setFinancialYear(fy);
		sale.setSalesTxn(salesTxn);

		return sale;
	}

	public SalesDet prepareSalesDet(String[] lineCont, Map<String, Book> bookHashMap)
	{
		String bookNum = lineCont[15];
		Integer qty = getIntegerVal(lineCont[16]);
		Double rate = getDoubleVal(lineCont[17]);
		Integer slNo = getIntegerVal(lineCont[21]);

		SalesDet sDet = new SalesDet();
		sDet.setBook(bookHashMap.get(bookNum));
		sDet.setQty(qty);
		sDet.setRate(rate);
		sDet.setSlNum(slNo);

		return sDet;
	}
}
