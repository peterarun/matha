package com.matha.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

import static com.matha.util.UtilConstants.*;
import static com.matha.util.Utils.getStringVal;

@Entity
@Table(name = "DeletedSTransactions")
@EntityListeners(AuditingEntityListener.class)
public class DeletedSalesTransaction
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
	private SchoolPayment payment;

	@OneToOne
	@JoinColumn(name = "ReturnId")
	private SchoolReturn salesReturn;

	@Column(name = "Balance")
	private Double balance;
	
	@Column(name = "Note")
	private String note;

	@Column(name = "PrevTxnId")
	private Integer prevTxn;

	@Column(name = "NextTxnId")
	private Integer nextTxn;

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

	public LocalDate getTxnDate()
	{
		return txnDate;
	}

	public String getTxnDateStr()
	{
		return DATE_CONV.toString(getTxnDate());
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
				.append("school", school == null ? null : school.getId() + "::" + school.getName())
				.append("sale", sale == null ? null : sale.getId() + "::" + sale.getSerialNo())
				.append("payment", payment == null ? null : payment.getId() + "::" + payment.getReceiptNum())
				.append("salesReturn", salesReturn == null ? null : salesReturn.getId() + "::" + salesReturn.getCreditNoteNum())
				.append("balance", balance)
				.append("note", note)
				.append("prevTxn", prevTxn)
				.append("nextTxn", nextTxn)
				.toString();
	}
}
