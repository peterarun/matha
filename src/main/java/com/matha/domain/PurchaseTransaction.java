package com.matha.domain;

import java.time.LocalDate;
import java.util.Date;

import javax.persistence.*;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import static com.matha.util.UtilConstants.*;
import static com.matha.util.Utils.getStringVal;


@Entity
@Table(name = "PTransactions")
@EntityListeners(AuditingEntityListener.class)
public class PurchaseTransaction
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

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PrevTxnId")
	private PurchaseTransaction prevTxn;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "NextTxnId")
	private PurchaseTransaction nextTxn;

	public String getVoucherNum()
	{
		if (purchase != null)
		{
			return purchase.getInvoiceNo();
		}
		else if (payment != null)
		{
			return payment.getReceiptNum();
		}
		else if (purchaseReturn != null)
		{
			return purchaseReturn.getCreditNoteNum();
		}

		return EMPTY_STR;
	}

	public String getPublisherName()
	{
		if(publisher != null)
		{
			return publisher.getName();
		}
		else
		{
			return EMPTY_STR;
		}
	}

	public String getType()
	{
		String type = "";
		if (purchase != null)
			type = PURCHASE_STR;
		else if (payment != null)
			type = PAYMENT_STR;
		else if (purchaseReturn != null)
			type = RETURN_STR;

		return type;
	}

	public String getRefNum()
	{
		if(this.payment != null && this.payment.getReferenceNum() != null)
		{
			return this.payment.getReferenceNum();
		}
		else
		{
			return EMPTY_STR;
		}
	}

	public String getMode()
	{
		if(this.payment != null)
		{
			return this.payment.getPaymentMode();
		}
		else
		{
			return EMPTY_STR;
		}
	}

	public Integer getMultiplier()
	{
		Integer multiplier = null;
		if (purchase != null)
		{
			multiplier = 1;
		}
		else if (payment != null || purchaseReturn != null)
		{
			multiplier = -1;
		}
		return multiplier;
	}

	public String getDirection()
	{
		String multiplier = null;
		if (purchase != null)
		{
			multiplier = "CR";
		}
		else if (payment != null || purchaseReturn != null)
		{
			multiplier = "DR";
		}
		return multiplier;
	}

	public Double getNetForBalance()
	{
		return this.amount * getMultiplier();
	}

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

	public String getNote()
	{
		return note;
	}

	public void setNote(String note)
	{
		this.note = note;
	}

	public Double getBalance()
	{
		return balance;
	}

	public void setBalance(Double balance)
	{
		this.balance = balance;
	}

	public PurchaseTransaction getPrevTxn()
	{
		return prevTxn;
	}

	public void setPrevTxn(PurchaseTransaction prevTxn)
	{
		this.prevTxn = prevTxn;
	}

	public PurchaseTransaction getNextTxn()
	{
		return nextTxn;
	}

	public void setNextTxn(PurchaseTransaction nextTxn)
	{
		this.nextTxn = nextTxn;
	}

    @PrePersist
    public void onPrePersist() {
    	Date dt = new Date();
        setAddTime(dt);
        setLastModifiedDate(dt);
    }
      
    @PreUpdate
    public void onPreUpdate() {
        setLastModifiedDate(new Date());
    }

	@Override
	public String toString()
	{
		return new ToStringBuilder(this)
				.append("id", id)
				.append("txnDate", txnDate)
				.append("amount", amount)
				.append("publisher", publisher == null ? null : publisher.getName())
				.append("purchase", purchase == null ? null: purchase.getId())
				.append("payment", payment == null ? null: payment.getId() + "::" + payment.getReceiptNum())
				.append("purchaseReturn", purchaseReturn == null ? null : purchaseReturn.getId())
				.append("balance", balance)
				.append("note", note)
				.append("prevTxn", prevTxn == null ? null : prevTxn.getId())
				.append("nextTxn", nextTxn == null ? null : nextTxn.getId())
				.toString();
	}
}
