package com.matha.domain;

import java.time.LocalDate;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;

import static com.matha.util.UtilConstants.DATE_CONV;
import static com.matha.util.UtilConstants.STATUS_MAP;

@Entity
@Table(name = "SReturn")
public class SchoolReturn {

	@Id
	@GenericGenerator(name = "saleReturnId", strategy = "com.matha.generator.SalesRetIdGenerator")
	@GeneratedValue(generator = "saleReturnId")
	@Column(name = "SerialId")
	private String id;

	@Column(name = "CreditNoteNum")
	private String creditNoteNum;

	@Column(name = "DAmt")
	private Double discAmt;

	@Column(name = "discType")
	private Boolean discType;

	@Column(name = "SubTotal")
	private Double subTotal;

	@Column(name = "Status")
	private Integer statusInd;

	@OneToOne
	@JoinColumn(name = "TxnId")
	private SalesTransaction salesTxn;

	@ManyToOne
	@JoinColumn(name = "CustId")
	private School school;

	@OneToMany(fetch= FetchType.EAGER, mappedBy = "schoolReturn", orphanRemoval = true)
	@Cascade({CascadeType.SAVE_UPDATE, CascadeType.DELETE})
	private Set<SalesReturnDet> salesReturnDetSet;

	public Double getDiscount()
	{
		if(subTotal == null || discAmt == null)
		{
			return 0.0;
		}
		if (discType != null && discType)
		{
			return subTotal * discAmt / 100;
		}
		return discAmt;
	}

	public String getStatusStr()
	{
		return STATUS_MAP.get(getStatusInd());
	}

	public Double getCalcNetTotal()
	{
		return subTotal - getDiscount();
	}

	public String getTxnDateStr()
	{
		return DATE_CONV.toString(salesTxn.getTxnDate());
	}

	public String getNotes()
	{
		return salesTxn.getNote();
	}

	public LocalDate getTxnDate()
	{
		return salesTxn.getTxnDate();
	}
	
	public Double getNetAmount()
	{
		if(salesTxn != null)
		{
			return salesTxn.getAmount();
		}
		else
		{
			return getCalcNetTotal();
		}
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreditNoteNum() {
		return creditNoteNum;
	}

	public void setCreditNoteNum(String creditNoteNum) {
		this.creditNoteNum = creditNoteNum;
	}

	public SalesTransaction getSalesTxn() {
		return salesTxn;
	}

	public void setSalesTxn(SalesTransaction salesTxn) {
		this.salesTxn = salesTxn;
	}

	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}

	public Set<SalesReturnDet> getSalesReturnDetSet() {
		return salesReturnDetSet;
	}

	public void setSalesReturnDetSet(Set<SalesReturnDet> salesReturnDetSet) {
		this.salesReturnDetSet = salesReturnDetSet;
	}

	public Double getAmount()
	{
		return this.getSalesTxn().getAmount();
	}

	public Double getDiscAmt() {
		return discAmt;
	}

	public void setDiscAmt(Double discAmt) {
		this.discAmt = discAmt;
	}

	public Boolean getDiscType() {
		return discType;
	}

	public void setDiscType(Boolean discType) {
		this.discType = discType;
	}

	public Double getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(Double subTotal) {
		this.subTotal = subTotal;
	}

	public Integer getStatusInd() {
		return statusInd;
	}

	public void setStatusInd(Integer statusInd) {
		this.statusInd = statusInd;
	}
}
