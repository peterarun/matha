package com.matha.domain;

import javax.persistence.*;
import java.time.LocalDate;

import static com.matha.util.UtilConstants.EMPTY_STR;
import static com.matha.util.UtilConstants.STATUS_MAP;

@Entity
@Table(name = "SAdjustment")
public class SchoolAdjustment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SerialId")
	private Integer id;

	@Column(name = "Type")
	private String adjType;

	@Column(name = "Dated")
	private LocalDate adjDate;

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

	public String getAdjType() {
		return adjType;
	}

	public void setAdjType(String adjType) {
		this.adjType = adjType;
	}

	public LocalDate getAdjDate() {
		return adjDate;
	}

	public void setAdjDate(LocalDate adjDate) {
		this.adjDate = adjDate;
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
