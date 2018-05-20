package com.matha.domain;

import java.time.LocalDate;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
@Table(name = "SReturn")
public class SchoolReturn {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SerialId")
	private Integer id;

	@OneToOne
	@JoinColumn(name = "TxnId")
	private SalesTransaction salesTxn;

	@OneToMany(fetch= FetchType.EAGER, mappedBy = "schoolReturn")
	@Cascade({CascadeType.SAVE_UPDATE, CascadeType.DELETE})	
	private Set<SalesReturnDet> salesReturnDetSet;
	
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
		return salesTxn.getAmount();
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

}
