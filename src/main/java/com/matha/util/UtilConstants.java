package com.matha.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;

import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.util.StringConverter;

public interface UtilConstants {

	public static final String[] discTypes = { "Rupees", "Percentage" };
	public static final String RUPEE_SIGN = "₹";
	public static final String PERCENT_SIGN = "%";
	public static final String COMMA_SIGN = ",";
	public static final String HYPHEN_SPC_SIGN = " - ";
	public static final String SPACE_SIGN = " ";
	public static final String SEMI_COLON_SIGN = ";";
	public static final String NEW_LINE = System.getProperty("line.separator");
	public static final String PDF = "PDF";
	public static final String Excel = "Excel";	
	public static final String Docx = "Word";
	public static final String DATE_FORMAT = "dd/MM/yyyy";
	public static final String SALES_NOTE = "Sales Bill Note";
	public static final String DELETED_STR = "DELETED";
	
	public static final StringConverter<LocalDate> DATE_CONV = Converters.getLocalDateConverter();
	public static final StringConverter<LocalDateTime> DATE_TIME_CONV = Converters.getLocalDateTimeConverter();
	
	public static final String landingPageFxmlFile = "/fxml/landing.fxml";
	public static final String schoolsFxml = "/fxml/Schools.fxml";
	public static final String addSchoolFxmlFile = "/fxml/addSchool.fxml";
	public static final String purchasePageFxmlFile = "/fxml/Purchases.fxml";
	public static final String cashBookFxml = "/fxml/CashBook.fxml";
	public static final String booksFxml = "/fxml/Books.fxml";
	public static final String addPublisherFxml = "/fxml/addPublisher.fxml";
	
	public static final String createOrderFxmlFile = "/fxml/createOrder.fxml";
	public static final String addTransactionFxmlFile = "/fxml/addTransaction.fxml";	
	public static final String printOrderFxmlFile = "/fxml/printOrder.fxml";
	public static final String addBillFxmlFile = "/fxml/createBill.fxml";
	public static final String viewBillFxmlFile = "/fxml/viewBill.fxml";
	public static final String addPaymentFxmlFile = "/fxml/createPayment.fxml";
	public static final String addReturnFxmlFile = "/fxml/createReturn.fxml";
	public static final String createPurchaseFxmlFile = "/fxml/createPurchaseBill.fxml";
	public static final String printPurchaseFxmlFile = "/fxml/printPurchase.fxml";
	public static final String printSaleFxmlFile = "/fxml/printSales.fxml";
	public static final String createPurchaseRetFxmlFile = "/fxml/createPurchaseReturn.fxml";
	public static final String createPurchasePayFxmlFile = "/fxml/createPurchasePay.fxml";
	
	public static final String statementJrxml = "/jrxml/Statement.jrxml";
	public static final String invoiceJrxml = "/jrxml/Invoice.jrxml";
	public static final String salesInvoiceJrxml = "/jrxml/SalesInvoice.jrxml";
	public static final String orderJrxml = "/jrxml/Order.jrxml";
	
	public static final ButtonType buttonTypeOne = new ButtonType("Yes", ButtonData.YES);
	public static final ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

	public static enum CRDR_ENUM{
		
		CREDIT(1, "CR");
		
		private Integer multiplier;
		private String repStr;
		private HashMap<Integer, String> crdrMap = new HashMap<>();
		
		CRDR_ENUM(Integer multiplierIn, String repStrIn)
		{
			this.multiplier = multiplierIn;
			this.repStr = repStrIn;
			crdrMap.put(multiplierIn, repStrIn);
		}
		
		public Integer getMultiplier()
		{
			return multiplier;
		}
		
		public String getType()
		{
			return repStr;
		}
		
		public HashMap<Integer,String> getCrDrMap()
		{
			return crdrMap;
		}
	}
}
