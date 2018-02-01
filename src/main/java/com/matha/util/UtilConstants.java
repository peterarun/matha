package com.matha.util;

import java.util.HashMap;

public interface UtilConstants {

	public static final String[] discTypes = { "Rupees", "Percentage" };
	public static final String RUPEE_SIGN = "₹";
	public static final String PERCENT_SIGN = "%";
	public static final String NEW_LINE = System.getProperty("line.separator");	 
	
	public static final String landingPageFxmlFile = "/fxml/landing.fxml";
	public static final String schoolsFxml = "/fxml/Schools.fxml";
	public static final String purchasePageFxmlFile = "/fxml/Purchases.fxml";
	public static final String cashBookFxml = "/fxml/CashBook.fxml";
	public static final String booksFxml = "/fxml/Books.fxml";
	
	public static final String createOrderFxmlFile = "/fxml/createOrder.fxml";
	public static final String addTransactionFxmlFile = "/fxml/addTransaction.fxml";	
	public static final String printOrderFxmlFile = "/fxml/orderPrint.fxml";
	public static final String addBillFxmlFile = "/fxml/createBill.fxml";
	public static final String addPaymentFxmlFile = "/fxml/createPayment.fxml";
	public static final String addReturnFxmlFile = "/fxml/createReturn.fxml";
	public static final String createPurchaseFxmlFile = "/fxml/createPurchaseBill.fxml";
	public static final String createPurchaseRetFxmlFile = "/fxml/createPurchaseReturn.fxml";
	public static final String createPurchasePayFxmlFile = "/fxml/createPurchasePay.fxml";
	
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
