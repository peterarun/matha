package com.matha.domain;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "Sales")
public class Sales {

	@Id
	@GenericGenerator(name = "salesId", strategy = "com.matha.generator.SalesIdGenerator")
	@GeneratedValue(generator = "salesId")
	@Column(name = "SerialId")
	private String id;

	@Column(name = "Packages")
	private Integer packages;

	@Column(name = "DPer")
	private Double discPerc;

	@Column(name = "DAmt")
	private Double discAmt;

	@Column(name = "SubTotal")
	private Double subTotal;

	@OneToOne
	@JoinColumn(name = "TxnId")
	private SalesTransaction salesTxn;

	@OneToMany(mappedBy = "sale")
	private Set<Order> order;

	public Set<Order> getOrder() {
		return order;
	}

	public void setOrder(Set<Order> order) {
		this.order = order;
	}

	public SalesTransaction getSalesTxn() {
		return salesTxn;
	}

	public void setSalesTxn(SalesTransaction salesTxn) {
		this.salesTxn = salesTxn;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getPackages() {
		return packages;
	}

	public void setPackages(Integer packages) {
		this.packages = packages;
	}

	public Double getDiscPerc() {
		return discPerc;
	}

	public void setDiscPerc(Double discPerc) {
		this.discPerc = discPerc;
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
