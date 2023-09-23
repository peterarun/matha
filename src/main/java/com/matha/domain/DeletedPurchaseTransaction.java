package com.matha.domain;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

import static com.matha.util.UtilConstants.*;


@Entity
@Table(name = "DeletedPTransactions")
@EntityListeners(AuditingEntityListener.class)
public class DeletedPurchaseTransaction
{

	@Id
	@Column(name = "SerialId")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

//	@CreatedDate
	@Column(name = "AddTime", columnDefinition="DATETIME")
	@Temporal(TemporalType.TIMESTAMP)	
	private Date addTime;

//    @LastModifiedDate
    @Column(name = "ModTime", columnDefinition="DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    protected Date lastModifiedDate;
    
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

	@Column(name = "Balance")
	private Double balance;

	@Column(name = "Note")
	private String note;

	@Column(name = "PrevTxnId")
	private Integer prevTxn;

	@Column(name = "NextTxnId")
	private Integer nextTxn;

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public Date getAddTime()
	{
		return addTime;
	}

	public void setAddTime(Date addTime)
	{
		this.addTime = addTime;
	}

	public Date getLastModifiedDate()
	{
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate)
	{
		this.lastModifiedDate = lastModifiedDate;
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

	public Double getBalance()
	{
		return balance;
	}

	public void setBalance(Double balance)
	{
		this.balance = balance;
	}

	public String getNote()
	{
		return note;
	}

	public void setNote(String note)
	{
		this.note = note;
	}

	public Integer getPrevTxn()
	{
		return prevTxn;
	}

	public void setPrevTxn(Integer prevTxn)
	{
		this.prevTxn = prevTxn;
	}

	public Integer getNextTxn()
	{
		return nextTxn;
	}

	public void setNextTxn(Integer nextTxn)
	{
		this.nextTxn = nextTxn;
	}
}
