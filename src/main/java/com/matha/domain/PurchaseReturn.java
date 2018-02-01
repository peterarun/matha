package com.matha.domain;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

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
@Table(name = "SchoolReturn")
public class PurchaseReturn {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SerialId")
	private Integer id;

	@OneToOne
	@JoinColumn(name = "TxnId")
	private PurchaseTransaction salesTxn;

	@OneToMany(fetch= FetchType.EAGER, mappedBy = "purchReturn")
	@Cascade({CascadeType.SAVE_UPDATE, CascadeType.DELETE})	
	private Set<OrderItem> orderItem;
	
	public Integer getUnitCount()
	{
		int unitCount = 0;
		if(getOrderItem() != null)
		{
			unitCount = getOrderItem().stream().collect(Collectors.summingInt(OrderItem::getCount));
		}
		
		return unitCount;
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
		return salesTxn.getAmount();
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public PurchaseTransaction getSalesTxn() {
		return salesTxn;
	}

	public void setSalesTxn(PurchaseTransaction salesTxn) {
		this.salesTxn = salesTxn;
	}

	public Set<OrderItem> getOrderItem() {
		return orderItem;
	}

	public void setOrderItem(Set<OrderItem> orderItem) {
		this.orderItem = orderItem;
	}

	public Double getAmount()
	{
		return this.getSalesTxn().getAmount();
	}

}
