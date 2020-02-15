package com.matha.domain;

import static com.matha.util.Converters.convertTimestamp;
import static com.matha.util.UtilConstants.*;
import static com.matha.util.Utils.getStringVal;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "STransactions")
@EntityListeners(AuditingEntityListener.class)
public class SalesTransaction
{

	@Id
	@Column(name = "SerialId")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	// @CreatedDate
	@Column(name = "AddTime", columnDefinition = "DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date addTime;

	// @LastModifiedDate
	@Column(name = "ModTime", columnDefinition = "DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	protected Date lastModifiedDate;

	@Column(name = "TxnDate")
	private Timestamp txnDate;

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
	private SchoolPayment payment;

	@OneToOne
	@JoinColumn(name = "ReturnId")
	private SchoolReturn salesReturn;

	@Column(name = "Balance")
	private Double balance;
	
	@Column(name = "Note")
	private String note;

	@OneToOne
	@JoinColumn(name = "PrevTxnId")
	private SalesTransaction prevTxn;

	@OneToOne
	@JoinColumn(name = "NextTxnId")
	private SalesTransaction nextTxn;

	public String getSchoolName()
	{
		if(school != null)
		{
			return school.getName();
		}
		else
		{
			return EMPTY_STR;
		}
	}

	public String getType()
	{
		String type = "";
		if (sale != null)
			type = SALE_STR;
		else if (payment != null)
			type = PAYMENT_STR;
		else if (salesReturn != null)
			type = CREDIT_NOTE_STR;

		return type;
	}

	public Integer getMultiplier()
	{
		Integer multiplier = null;
		if (sale != null)
		{
			multiplier = 1;
		}
		else if (payment != null || salesReturn != null)
		{
			multiplier = -1;
		}
		return multiplier;
	}

	public Double getNetForBalance()
	{
		return this.amount * getMultiplier();
	}
	
	public String getInvoiceNum()
	{
		if(this.sale != null)
		{
			return getStringVal(this.sale.getSerialNo());
		}
		else if(this.payment != null)
		{
			return this.payment.getReceiptNum();
		}
		else if(this.salesReturn != null)
		{
			return this.salesReturn.getCreditNoteNum();
		}
		else
		{
			return EMPTY_STR;
		}
	}

	public String getDependentId()
	{
		if(this.sale != null)
		{
			return this.sale.getId();
		}
		else if(this.payment != null)
		{
			return String.valueOf(this.payment.getId());
		}
		else if(this.salesReturn != null)
		{
			return this.salesReturn.getId();
		}
		else
		{
			return EMPTY_STR;
		}
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

	public String getParticulars()
	{
		return this.getNote();
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

	public Timestamp getTxnDate()
	{
		return txnDate;
	}

	public String getTxnDateStr()
	{
		return DATE_CONV.toString(convertTimestamp(getTxnDate()));
	}

	public void setTxnDate(Timestamp txnDate)
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

	public School getSchool()
	{
		return school;
	}

	public void setSchool(School school)
	{
		this.school = school;
	}

	public Sales getSale()
	{
		return sale;
	}

	public void setSale(Sales salesId)
	{
		this.sale = salesId;
	}

	public SchoolPayment getPayment()
	{
		return payment;
	}

	public void setPayment(SchoolPayment paymentId)
	{
		this.payment = paymentId;
	}

	public SchoolReturn getSalesReturn()
	{
		return salesReturn;
	}

	public void setSalesReturn(SchoolReturn returnId)
	{
		this.salesReturn = returnId;
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

	public SalesTransaction getPrevTxn()
	{
		return prevTxn;
	}

	public void setPrevTxn(SalesTransaction prevTxn)
	{
		this.prevTxn = prevTxn;
	}

	public SalesTransaction getNextTxn()
	{
		return nextTxn;
	}

	public void setNextTxn(SalesTransaction nextTxn)
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
}
