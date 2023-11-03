package com.matha.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.util.StringConverter;

import static java.util.stream.Collectors.toMap;

public interface UtilConstants {

	public static final String[] discTypes = { "Rupees", "Percentage" };
	public static final String RUPEE_SIGN = "₹";
	public static final String PERCENT_SIGN = "%";
	public static final String COMMA_SIGN = ",";
	public static final String HYPHEN_SPC_SIGN = " - ";
	public static final String EMPTY_STR = "";
	public static final String SPACE_SIGN = " ";
	public static final String SEMI_COLON_SIGN = ";";
	public static final String NEW_LINE = System.getProperty("line.separator");
	public static final String USUAL_DISC_TXT = "15";
	public static final String PDF = "PDF";
	public static final String Excel = "Excel";	
	public static final String Docx = "Word";
	public static final String DATE_FORMAT = "dd/MM/yyyy";
	public static final String SALES_NOTE = "Sales Bill Note";
	public static final String DELETED_STR = "DELETED";
	public static final String ACTIVE_STR = "ACTIVE";
	public static final String ARCHIVED_STR = "ARCHIVED";
	public static final String PURCHASE_STR = "Purchase";
	public static final String PAYMENT_STR = "Payment";
	public static final String RETURN_STR = "Return";
	public static final String SALE_STR = "Sales";
	public static final String CREDIT_NOTE_STR = "Credit Note";
	public static final String ADJUSTMENT_STR = "Adjustment";
	public static final String ORDER_SEARCH_STR = "Order Search";
	public static final String PUR_BILL_SEARCH_STR = "Purchase Search";
	public static final String SALE_BILL_SEARCH_STR = "Sale Search";
	public static final Integer DELETED_IND = Integer.valueOf(-2);
	public static final Integer ARCHIVED_IND = Integer.valueOf(-1);
	public static final Integer ACTIVE_IND = Integer.valueOf(0);
	public static Map<Integer, String> STATUS_MAP = Collections.unmodifiableMap(Stream.of(
			new AbstractMap.SimpleEntry<>(DELETED_IND, DELETED_STR),
			new AbstractMap.SimpleEntry<>(ARCHIVED_IND, ARCHIVED_STR),
			new AbstractMap.SimpleEntry<>(ACTIVE_IND, ACTIVE_STR))
			.collect(toMap((e) -> e.getKey(), (e) -> e.getValue())));

	public static final StringConverter<LocalDate> DATE_CONV = Converters.getLocalDateConverter();
	public static final StringConverter<LocalDateTime> DATE_TIME_CONV = Converters.getLocalDateTimeConverter();
	
	public static final String landingPageFxmlFile = "/fxml/landing.fxml";
	public static final String schoolsFxml = "/fxml/Schools.fxml";
	public static final String addSchoolFxmlFile = "/fxml/addSchool.fxml";
	public static final String purchasePageFxmlFile = "/fxml/Purchases.fxml";
	public static final String cashBookFxml = "/fxml/CashBook.fxml";
	public static final String booksFxml = "/fxml/Books.fxml";
	public static final String addBookFxmlFile = "/fxml/addBook.fxml";
	public static final String statementsFxml = "/fxml/masterStatement.fxml";

	public static final String addPublisherFxml = "/fxml/addPublisher.fxml";
	
	public static final String createOrderFxmlFile = "/fxml/createOrder.fxml";
	public static final String addTransactionFxmlFile = "/fxml/addTransaction.fxml";	
	public static final String printOrderFxmlFile = "/fxml/printOrder.fxml";
	public static final String addBillFxmlFile = "/fxml/createBill.fxml";
	public static final String viewBillFxmlFile = "/fxml/viewBill.fxml";
	public static final String addPaymentFxmlFile = "/fxml/createPayment.fxml";
	public static final String addAdjustmentFxmlFile = "/fxml/createAdjustment.fxml";
	public static final String addReturnFxmlFile = "/fxml/createReturn.fxml";
	public static final String createPurchaseFxmlFile = "/fxml/createPurchaseBill.fxml";
	public static final String printPurchaseFxmlFile = "/fxml/printPurchase.fxml";
	public static final String printSaleFxmlFile = "/fxml/printSales.fxml";
	public static final String printCreditNoteFxmlFile = "/fxml/printCreditNote.fxml";
	public static final String printPurchaseReturnFxmlFile = "/fxml/printPurRet.fxml";
	public static final String createPurchaseRetFxmlFile = "/fxml/createPurchaseReturn.fxml";
	public static final String createPurchasePayFxmlFile = "/fxml/createPurchasePay.fxml";
	
	public static final String salesStmtFxmlFile = "/fxml/printSalesStmt.fxml";
	public static final String masterStmtFxmlFile = "/fxml/printMasterStmt.fxml";

	public static final String masterSearchFxmlFile = "/fxml/masterSearch.fxml";
	
//	public static final String statementJrxml = "/jrxml/Statement.jrxml";
//	public static final String salesStmtJrxml = "/jrxml/SalesStmt.jrxml";
//	public static final String invoiceJrxml = "/jrxml/Invoice.jrxml";
//	public static final String salesInvoiceJrxml = "/jrxml/SalesInvoice.jrxml";
//  public static final String creditNoteJrxml = "/jrxml/CreditNote.jrxml";
//	public static final String puReturnJrxml = "/jrxml/PurReturn.jrxml";
//	public static final String orderJrxml = "/jrxml/Order.jrxml";
//	public static final String masterPurStmtJrxml = "/jrxml/MasterPurchaseStmt.jrxml";
//	public static final String masterSaleStmtJrxml = "/jrxml/MasterSalesStmt.jrxml";

	public static final String salesInvoiceJrxml = "/jasper/SalesInvoice.jasper";
	public static final String salesStmtJrxml = "/jasper/SalesStmt.jasper";
	public static final String creditNoteJrxml = "/jasper/CreditNote.jasper";
	public static final String invoiceJrxml = "/jasper/Invoice.jasper";
	public static final String puReturnJrxml = "/jasper/PurReturn.jasper";
	public static final String statementJrxml = "/jasper/Statement.jasper";
	public static final String masterPurStmtJrxml = "/jasper/MasterPurchaseStmt.jasper";
	public static final String masterSaleStmtJrxml = "/jasper/MasterSalesStmt.jasper";
	public static final String orderJrxml = "/jasper/Order.jasper";

	public static final ButtonType buttonTypeOne = new ButtonType("Yes", ButtonData.YES);
	public static final ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

}
