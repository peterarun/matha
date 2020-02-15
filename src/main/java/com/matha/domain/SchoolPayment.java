package com.matha.domain;

import java.sql.Timestamp;
import java.time.LocalDate;

import javax.persistence.*;

import static com.matha.util.UtilConstants.EMPTY_STR;
import static com.matha.util.UtilConstants.STATUS_MAP;

@Entity
@Table(name = "SPayment")
public class SchoolPayment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SerialId")
	private Integer id;

	@Column(name = "ReceiptNum")
	private String receiptNum;

	@Column(name = "Mode")
	private String paymentMode;

	@Column(name = "Dated")
	private Timestamp dated;

	@Column(name = "RefNum")
	private String referenceNum;

	@Column(name = "Status")
	private Integer statusInd;

	@ManyToOne
	@JoinColumn(name = "CustId")
	private School school;

	@OneToOne
	@JoinColumn(name = "TxnId")
	private SalesTransaction salesTxn;

	@Column(name = "DelAmt")
	private Double deletedAmt;

	public String getTxnDateStr()
	{
		if(salesTxn != null)
		{
			return this.salesTxn.getTxnDateStr();
		}
		else
		{
			return EMPTY_STR;
		}
	}

	public String getStatusStr()
	{
		return STATUS_MAP.get(getStatusInd());
	}

	public String getReceiptNum() {
		return receiptNum;
	}

	public void setReceiptNum(String receiptNum) {
		this.receiptNum = receiptNum;
	}

	public Timestamp getTxnDate()
	{
		return salesTxn.getTxnDate();
	}
	
	public Double getAmount()
	{
		return salesTxn.getAmount();
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	public Timestamp getDated() {
		return dated;
	}

	public void setDated(Timestamp dated) {
		this.dated = dated;
	}

	public String getReferenceNum() {
		return referenceNum;
	}

	public void setReferenceNum(String referenceNum) {
		this.referenceNum = referenceNum;
	}

	public SalesTransaction getSalesTxn() {
		return salesTxn;
	}

	public void setSalesTxn(SalesTransaction salesTxn) {
		this.salesTxn = salesTxn;
	}

	public Integer getStatusInd() {
		return statusInd;
	}

	public void setStatusInd(Integer statusInd) {
		this.statusInd = statusInd;
	}

	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}

	public Double getDeletedAmt()
	{
		return deletedAmt;
	}

	public void setDeletedAmt(Double deletedAmt)
	{
		this.deletedAmt = deletedAmt;
	}
}
