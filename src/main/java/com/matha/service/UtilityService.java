package com.matha.service;

import com.matha.controller.PrintSalesBillController;
import com.matha.domain.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static com.matha.util.UtilConstants.*;
import static com.matha.util.Utils.convertDouble;
import static com.matha.util.Utils.getStringVal;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@Service
public class UtilityService
{

	private static final Logger LOGGER = LogManager.getLogger(UtilityService.class);

	@Value("${salesBillPartyName}")
	private String salesBillPartyName;

	@Value("${purchasePartyName}")
	private String purchasePartyName;

	@Value("${creditNoteAgencyName}")
	private String creditNoteAgencyName;

	public Scene preparePrintScene(Sales purchase, FXMLLoader createOrderLoader, InputStream jasperStream, Address addrIn, String salesBankDetails)
	{
		Scene addOrderScene = null;
		try
		{
			Parent addOrderRoot = createOrderLoader.load();
			PrintSalesBillController ctrl = createOrderLoader.getController();
			JasperPrint jasperPrint = prepareSaleBillPrint(purchase.getSalesTxn().getSchool(), purchase, addrIn, jasperStream, salesBankDetails);
			ctrl.initData(jasperPrint);
			addOrderScene = new Scene(addOrderRoot);
		}
		catch (Exception e)
		{
			LOGGER.error("Error...", e);
			e.printStackTrace();
		}
		return addOrderScene;
	}

	@Transactional
	public JasperPrint prepareJasperPrint(Publisher pub, Purchase purchase, Address salesAddr, InputStream jasperStream)
	{
		JasperPrint jasperPrint = null;
		HashMap<String, Object> hm = new HashMap<>();
		try
		{
			StringBuilder strBuild = new StringBuilder();
			strBuild.append(salesAddr.getAddress1());
			strBuild.append(NEW_LINE);
			strBuild.append(salesAddr.getAddress2());
			strBuild.append(COMMA_SIGN);
			strBuild.append(salesAddr.getAddress3());
			strBuild.append(HYPHEN_SPC_SIGN);
			strBuild.append(salesAddr.getPin());

			Set<PurchaseDet> tableData = purchase.getPurchaseItems();
			Set<String> orderIdSet = tableData.stream()
					.filter(pd -> pd.getOrderItem() != null)
					.map(PurchaseDet::getOrderItem)
					.filter(oi -> oi.getOrder() != null)
					.map(oi -> oi.getOrder().getSerialNo())
					.collect(toSet());
			String orderIds = String.join(",", orderIdSet);
			Double subTotal = purchase.getSubTotal();
			Double discAmt = purchase.getCalculatedDisc();

			hm.put("publisherName", pub.getName());
			hm.put("publisherDetails", pub.getInvAddress());
			hm.put("partyName", purchasePartyName);
			hm.put("partyAddress", strBuild.toString());
			hm.put("partyPhone", salesAddr.getPhone1());
			hm.put("documentsThrough", purchase.getDocsThrough());
			hm.put("despatchedTo", purchase.getDespatchedTo());
			hm.put("invoiceNo", purchase.getId());
			hm.put("txnDate", purchase.getTxnDate());
			hm.put("orderNumbers", orderIds);
			hm.put("despatchedPer", purchase.getDespatchPer());
			hm.put("grNo", purchase.getGrNum());
			hm.put("packageCnt", getStringVal(purchase.getPackages()));
			hm.put("total", purchase.getSubTotal());
			hm.put("discount", discAmt);
			hm.put("grandTotal", purchase.getNetAmount());
			hm.put("grandTotalInWords", convertDouble(purchase.getNetAmount()));

//			JasperReport compiledFile = JasperCompileManager.compileReport(jasperStream);

			jasperPrint = JasperFillManager.fillReport(jasperStream, hm, new JRBeanCollectionDataSource(tableData));
		}
		catch (JRException e)
		{
			LOGGER.error("Error...", e);
			e.printStackTrace();
		}
		catch (Exception e)
		{
			LOGGER.error("Error...", e);
			e.printStackTrace();
		}

		return jasperPrint;
	}

	public JasperPrint prepareSaleBillPrint(School sch, Sales sale, Address salesAddr, InputStream jasperStream, String salesBankDetails)
	{
		JasperPrint jasperPrint = null;
		HashMap<String, Object> hm = new HashMap<>();
		try
		{
			StringBuilder strBuild = new StringBuilder();
			strBuild.append(salesAddr.getAddress1());
			strBuild.append(NEW_LINE);
			strBuild.append(salesAddr.getAddress2());
			strBuild.append(COMMA_SIGN);
			strBuild.append(SPACE_SIGN);
			strBuild.append(salesAddr.getAddress3());
			strBuild.append(HYPHEN_SPC_SIGN);
			strBuild.append(salesAddr.getPin());
			strBuild.append(NEW_LINE);
			strBuild.append("Ph: ");
			strBuild.append(salesAddr.getPhone1());
			strBuild.append(SPACE_SIGN);
			strBuild.append("Mob: ");
			strBuild.append(salesAddr.getPhone2());
			strBuild.append(NEW_LINE);
			strBuild.append("Email: ");
			strBuild.append(salesAddr.getEmail());

			List<SalesDet> tableData = sale.getSaleItems().stream().sorted(comparing(sd -> sd.getSlNum())).collect(toList());
			Set<String> orderIdSet = tableData.stream()
					.map(SalesDet::getOrderItem)
					.filter(o-> o != null)
					.map(o -> o.getOrder().getSerialNo())
					.collect(toSet());
			String orderIds = String.join(",", orderIdSet);
			Double subTotal = sale.getSubTotal();
			Double discAmt = sale.getDiscAmt();
			if(discAmt != null)
			{
				discAmt = sale.getDiscType() != null && sale.getDiscType() ? subTotal * discAmt /100 : discAmt;
			}

			hm.put("partyName", sch.getName());
			hm.put("partyAddress", sch.addressText());
			hm.put("agencyName", salesBillPartyName);
			hm.put("agencyDetails", strBuild.toString());
			hm.put("partyPhone", sch.getPhone1() == null ? sch.getPhone2() : sch.getPhone1());
			hm.put("documentsThrough", sale.getDocsThru());
			hm.put("invoiceNo", getStringVal(sale.getSerialNo()));
			hm.put("txnDate", sale.getInvoiceDateStr());
			hm.put("orderNumbers", orderIds);
			hm.put("despatchedPer", sale.getDespatch());
			hm.put("grNo", sale.getGrNum());
			hm.put("packageCnt", sale.getPackages());
			hm.put("total", sale.getSubTotal());
			hm.put("discount", discAmt);
			hm.put("grandTotal", sale.getNetAmount());
			hm.put("accountDet", salesBankDetails);
			hm.put("grandTotalInWords", convertDouble(sale.getNetAmount()));
			hm.put("otherCharges", sale.getOtherAmount());

//			JasperReport compiledFile = JasperCompileManager.compileReport(jasperStream);

			jasperPrint = JasperFillManager.fillReport(jasperStream, hm, new JRBeanCollectionDataSource(tableData));
		}
		catch (JRException e)
		{
			LOGGER.error("Error...", e);
			e.printStackTrace();
		}
		catch (Exception e)
		{
			LOGGER.error("Error...", e);
			e.printStackTrace();
		}

		return jasperPrint;

	}

	public JasperPrint prepareCreditNotePrint(School sch, SchoolReturn schoolReturn, Address salesAddr, String salesBankDetails, InputStream jasperStream)
	{

		JasperPrint jasperPrint = null;
		HashMap<String, Object> hm = new HashMap<>();
		try
		{
			StringBuilder strBuild = new StringBuilder();
			strBuild.append(salesAddr.getAddress1());
			strBuild.append(NEW_LINE);
			strBuild.append(salesAddr.getAddress2());
			strBuild.append(COMMA_SIGN);
			strBuild.append(SPACE_SIGN);
			strBuild.append(salesAddr.getAddress3());
			strBuild.append(HYPHEN_SPC_SIGN);
			strBuild.append(salesAddr.getPin());
			strBuild.append(NEW_LINE);
			strBuild.append("Ph: ");
			strBuild.append(salesAddr.getPhone1());
			strBuild.append(SPACE_SIGN);
			strBuild.append("Mob: ");
			strBuild.append(salesAddr.getPhone2());
			strBuild.append(NEW_LINE);
			strBuild.append("Email: ");
			strBuild.append(salesAddr.getEmail());

			List<SalesReturnDet> tableData = schoolReturn.getSalesReturnDetSet().stream().sorted(comparing(sd -> sd.getSlNum())).collect(toList());
			Double discAmt = schoolReturn.getDiscount();

			hm.put("partyName", sch.getName());
			hm.put("partyAddress", sch.addressText());
			hm.put("agencyName", creditNoteAgencyName);
			hm.put("agencyDetails", strBuild.toString());
			hm.put("partyPhone", sch.getPhone1() == null ? sch.getPhone2() : sch.getPhone1());
			hm.put("creditNoteNum", schoolReturn.getCreditNoteNum());
			hm.put("txnDate", schoolReturn.getTxnDateStr());
			hm.put("total", schoolReturn.getSubTotal());
			hm.put("discount", discAmt);
			hm.put("grandTotal", schoolReturn.getNetAmount());
			hm.put("accountDet", salesBankDetails);
			hm.put("grandTotalInWords", convertDouble(schoolReturn.getNetAmount()));

//			JasperReport compiledFile = JasperCompileManager.compileReport(jasperStream);

			jasperPrint = JasperFillManager.fillReport(jasperStream, hm, new JRBeanCollectionDataSource(tableData));
		}
		catch (JRException e)
		{
			LOGGER.error("Error...", e);
			e.printStackTrace();
		}
		catch (Exception e)
		{
			LOGGER.error("Error...", e);
			e.printStackTrace();
		}

		return jasperPrint;
	}

	public JasperPrint prepareJasperPrint(Publisher pub, PurchaseReturn purchase, Address salesAddr, InputStream jasperStream)
	{
		JasperPrint jasperPrint = null;
		HashMap<String, Object> hm = new HashMap<>();
		try
		{
			StringBuilder strBuild = new StringBuilder();
			strBuild.append(salesAddr.getAddress1());
			strBuild.append(NEW_LINE);
			strBuild.append(salesAddr.getAddress2());
			strBuild.append(COMMA_SIGN);
			strBuild.append(salesAddr.getAddress3());
			strBuild.append(HYPHEN_SPC_SIGN);
			strBuild.append(salesAddr.getPin());
			strBuild.append(NEW_LINE);
			strBuild.append(salesAddr.getPhone1());

			Double discAmt = purchase.getDiscount();

			hm.put("publisherName", pub.getName());
			hm.put("publisherDetails", pub.getInvAddress());
			hm.put("partyName", purchasePartyName);
			hm.put("partyAddress", strBuild.toString());
			hm.put("partyPhone", pub.getPhone1());
			hm.put("returnNum", purchase.getCreditNoteNum());
			hm.put("txnDate", purchase.getTxnDate());
			hm.put("total", purchase.getSubTotal());
			hm.put("discount", discAmt);
			hm.put("grandTotal", purchase.getNetAmount());
			hm.put("grandTotalInWords", convertDouble(purchase.getNetAmount()));

//			JasperReport compiledFile = JasperCompileManager.compileReport(jasperStream);

			jasperPrint = JasperFillManager.fillReport(jasperStream, hm, new JRBeanCollectionDataSource(purchase.getPurchaseReturnDetSet()));
		}
		catch (JRException e)
		{
			LOGGER.error("Error...", e);
			e.printStackTrace();
		}
		catch (Exception e)
		{
			LOGGER.error("Error...", e);
			e.printStackTrace();
		}

		return jasperPrint;
	}

}
