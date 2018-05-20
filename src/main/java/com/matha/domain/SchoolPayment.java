package com.matha.domain;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "SPayment")
public class SchoolPayment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SerialId")
	private Integer id;

	@Column(name = "Mode")
	private String paymentMode;

	@OneToOne
	@JoinColumn(name = "TxnId")
	private SalesTransaction salesTxn;

	public LocalDate getTxnDate()
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

	public SalesTransaction getSalesTxn() {
		return salesTxn;
	}

	public void setSalesTxn(SalesTransaction salesTxn) {
		this.salesTxn = salesTxn;
	}

}
