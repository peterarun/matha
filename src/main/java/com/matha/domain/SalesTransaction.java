package com.matha.domain;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "STransactions")
public class SalesTransaction {

	@Id
	@Column(name = "SerialId")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "TxnDate")
	private LocalDate txnDate;

	@Column(name = "Amount")
	private Double amount;

	@ManyToOne
	@JoinColumn(name = "SchoolId")
	private School school;

	@OneToOne
	@JoinColumn(name = "SalesId")
	private Sales sale;

	@OneToOne
	@JoinColumn(name = "PaymentId")
	private SchoolPayment paymentId;

	@OneToOne
	@JoinColumn(name = "ReturnId")
	private SchoolReturn returnId;

	@Column(name = "Note")
	private String note;

	public String getType()
	{
		String type = "";
		if(sale != null) type = "Book Bill";
		else if(paymentId != null) type = "Payment";
		else if(returnId != null) type = "Credit Note";
		
		return type;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LocalDate getTxnDate() {
		return txnDate;
	}

	public void setTxnDate(LocalDate txnDate) {
		this.txnDate = txnDate;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}

	public Sales getSale() {
		return sale;
	}

	public void setSale(Sales salesId) {
		this.sale = salesId;
	}

	public SchoolPayment getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(SchoolPayment paymentId) {
		this.paymentId = paymentId;
	}

	public SchoolReturn getReturnId() {
		return returnId;
	}

	public void setReturnId(SchoolReturn returnId) {
		this.returnId = returnId;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

}
