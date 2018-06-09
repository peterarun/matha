package com.matha.domain;

import java.time.LocalDate;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;

import static com.matha.util.UtilConstants.DATE_CONV;

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

	@Column(name = "DPer")
	private Double discPercent;

	@Column(name = "DAmt")
	private Double discAmt;

	@Column(name = "SubTotal")
	private Double subTotal;

	@OneToOne
	@JoinColumn(name = "TxnId")
	private SalesTransaction salesTxn;

	@OneToMany(fetch= FetchType.EAGER, mappedBy = "schoolReturn", orphanRemoval = true)
	@Cascade({CascadeType.SAVE_UPDATE, CascadeType.DELETE})
	private Set<SalesReturnDet> salesReturnDetSet;

	public School getSchool()
	{
		if(salesTxn != null)
		{
			return salesTxn.getSchool();
		}
		else
		{
			return null;
		}
	}

	public Double getDiscount()
	{
		if (this.discPercent != null)
		{
			if(this.subTotal == null)
			{
				this.subTotal = Double.valueOf(0.0);
			}
			return this.subTotal * this.discPercent / 100;
		}
		else if(this.discAmt != null)
		{
			return this.discAmt;
		}
		else
		{
			return Double.valueOf(0.0);
		}
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

	public Double getDiscPercent() {
		return discPercent;
	}

	public void setDiscPercent(Double discPercent) {
		this.discPercent = discPercent;
	}

	public Double getDiscAmt() {
		return discAmt;
	}

	public void setDiscAmt(Double discAmt) {
		this.discAmt = discAmt;
	}

	public Double getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(Double subTotal) {
		this.subTotal = subTotal;
	}
}
