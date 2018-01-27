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
@Table(name = "PTransactions")
public class PurchaseTransaction
{

	@Id
	@Column(name = "SerialId")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "TxnDate")
	private LocalDate txnDate;

	@Column(name = "Amount")
	private Double amount;

	@ManyToOne
	@JoinColumn(name = "PublisherId")
	private Publisher publisher;

	@OneToOne
	@JoinColumn(name = "PurchaseId")
	private Purchase purchase;

	@OneToOne
	@JoinColumn(name = "PaymentId")
	private PurchasePayment payment;

	@OneToOne
	@JoinColumn(name = "ReturnId")
	private PurchaseReturn purchaseReturn;

	@Column(name = "Note")
	private String note;

	public String getType()
	{
		String type = "";
		if (purchase != null)
			type = "Purchase";
		else if (payment != null)
			type = "Payment";
		else if (purchaseReturn != null)
			type = "Credit Note";

		return type;
	}

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public LocalDate getTxnDate()
	{
		return txnDate;
	}

	public void setTxnDate(LocalDate txnDate)
	{
		this.txnDate = txnDate;
	}

	public Double getAmount()
	{
		return amount;
	}

	public void setAmount(Double amount)
	{
		this.amount = amount;
	}

	public Publisher getPublisher()
	{
		return publisher;
	}

	public void setPublisher(Publisher publisher)
	{
		this.publisher = publisher;
	}

	public Purchase getPurchase()
	{
		return purchase;
	}

	public void setPurchase(Purchase purchase)
	{
		this.purchase = purchase;
	}

	public PurchasePayment getPayment()
	{
		return payment;
	}

	public void setPayment(PurchasePayment payment)
	{
		this.payment = payment;
	}

	public PurchaseReturn getPurchaseReturn()
	{
		return purchaseReturn;
	}

	public void setPurchaseReturn(PurchaseReturn purchaseReturn)
	{
		this.purchaseReturn = purchaseReturn;
	}

	public String getNote()
	{
		return note;
	}

	public void setNote(String note)
	{
		this.note = note;
	}

}
