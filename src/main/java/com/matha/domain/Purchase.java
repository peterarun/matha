package com.matha.domain;

import static com.matha.util.UtilConstants.DATE_CONV;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "BookPurchase")
public class Purchase implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3847049966939941709L;

	@Id
	@Column(name = "Id")
	private String id;

	@Column(name = "DespatchedTo")
	private String despatchedTo;

	@Column(name = "DocumentsThrough")
	private String docsThrough;

	@Column(name = "DespatchPer")
	private String despatchPer;

	@Column(name = "GrNo")
	private String grNum;

	@Column(name = "Packages")
	private Integer packages;

	@Column(name = "Discount")
	private Double discAmt;

	@Column(name = "DiscountType")
	private Boolean discType;

	@Column(name = "SubTotal")
	private Double subTotal;

	@Column(name = "PurchaseDate")
	private LocalDate purchaseDate;

	@OneToOne	
	@JoinColumn(name = "TxnId")
	private PurchaseTransaction salesTxn;

	@OneToMany(mappedBy = "purchase", fetch = FetchType.EAGER)
	private Set<OrderItem> orderItems;

	public LocalDate getTxnDate()
	{
		return this.getSalesTxn().getTxnDate();
	}
	
	public String getTxnDateStr()
	{		
		return DATE_CONV.toString(getTxnDate());
	}
	
	public Integer getUnitCount()
	{
		int unitCount = 0;
		if(getOrderItems() != null)
		{
			unitCount = getOrderItems().stream().collect(Collectors.summingInt(OrderItem::getFullFilledCnt));
		}
		
		return unitCount;
	}
	
	public Double getNetAmount()
	{
		return this.getSalesTxn().getAmount();
	}
	
	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getDespatchedTo()
	{
		return despatchedTo;
	}

	public void setDespatchedTo(String despatchedTo)
	{
		this.despatchedTo = despatchedTo;
	}

	public String getDocsThrough()
	{
		return docsThrough;
	}

	public void setDocsThrough(String docsThrough)
	{
		this.docsThrough = docsThrough;
	}

	public String getDespatchPer()
	{
		return despatchPer;
	}

	public void setDespatchPer(String despatchPer)
	{
		this.despatchPer = despatchPer;
	}

	public String getGrNum()
	{
		return grNum;
	}

	public void setGrNum(String grNum)
	{
		this.grNum = grNum;
	}

	public Integer getPackages()
	{
		return packages;
	}

	public void setPackages(Integer packages)
	{
		this.packages = packages;
	}

	public Double getDiscAmt()
	{
		return discAmt;
	}

	public void setDiscAmt(Double discAmt)
	{
		this.discAmt = discAmt;
	}

	public Boolean getDiscType()
	{
		return discType;
	}

	public void setDiscType(Boolean discType)
	{
		this.discType = discType;
	}

	public Double getSubTotal()
	{
		return subTotal;
	}

	public void setSubTotal(Double subTotal)
	{
		this.subTotal = subTotal;
	}

	public LocalDate getPurchaseDate()
	{
		return purchaseDate;
	}

	public void setPurchaseDate(LocalDate purchaseDate)
	{
		this.purchaseDate = purchaseDate;
	}

	public PurchaseTransaction getSalesTxn()
	{
		return salesTxn;
	}

	public void setSalesTxn(PurchaseTransaction salesTxn)
	{
		this.salesTxn = salesTxn;
	}

	public Set<OrderItem> getOrderItems()
	{
		return orderItems;
	}

	public void setOrderItems(Set<OrderItem> order)
	{
		this.orderItems = order;
	}

}
