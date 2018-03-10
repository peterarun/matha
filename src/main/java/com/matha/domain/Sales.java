package com.matha.domain;

import static com.matha.util.UtilConstants.DATE_CONV;

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

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "Sales")
public class Sales
{

	@Id
	@GenericGenerator(name = "salesId", strategy = "com.matha.generator.SalesIdGenerator")
	@GeneratedValue(generator = "salesId")
	@Column(name = "SerialId")
	private String id;

	@Column(name = "Despatch")
	private String despatch;

	@Column(name = "Documents")
	private String docsThru;

	@Column(name = "GRNo")
	private String grNum;

	@Column(name = "SerialNo")
	private Integer invoiceNo;
	
	@Column(name = "Packages")
	private String packages;

	@Column(name = "DAmt")
	private Double discAmt;

	@Column(name = "discType")
	private Boolean discType;

	@Column(name = "SubTotal")
	private Double subTotal;

	@Column(name = "Others")
	private Double otherAmount;
	
	@Column(name = "Fy")
	private Integer financialYear;	

	@Column(name = "NetAmt")
	private Double deletedAmt;	
	
	@OneToOne
	@JoinColumn(name = "TxnId")
	private SalesTransaction salesTxn;

	@OneToMany(mappedBy = "sale", fetch = FetchType.EAGER)
	private Set<OrderItem> orderItems;

	public LocalDate getInvoiceDate()
	{
		return salesTxn.getTxnDate();
	}

	public String getInvoiceDateStr()
	{		
		return DATE_CONV.toString(getInvoiceDate());
	}

	public double getCalcDisc()
	{
		if(subTotal == null || discAmt == null)
		{
			return 0.0;
		}
		if (discType)
		{
			return subTotal * discAmt / 100;
		}
		return discAmt;
	}
	
	public Double getNetAmount()
	{
		return salesTxn.getAmount();
	}

	public SalesTransaction getSalesTxn()
	{
		return salesTxn;
	}

	public void setSalesTxn(SalesTransaction salesTxn)
	{
		this.salesTxn = salesTxn;
	}
	
	public Set<OrderItem> getOrderItems()
	{
		return orderItems;
	}

	public void setOrderItems(Set<OrderItem> orderItems)
	{
		this.orderItems = orderItems;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public Integer getInvoiceNo()
	{
		return invoiceNo;
	}

	public void setInvoiceNo(Integer invoiceNo)
	{
		this.invoiceNo = invoiceNo;
	}

	public String getDespatch()
	{
		return despatch;
	}

	public void setDespatch(String despatch)
	{
		this.despatch = despatch;
	}

	public String getDocsThru()
	{
		return docsThru;
	}

	public void setDocsThru(String docsThru)
	{
		this.docsThru = docsThru;
	}

	public String getGrNum()
	{
		return grNum;
	}

	public void setGrNum(String grNum)
	{
		this.grNum = grNum;
	}

	public String getPackages()
	{
		return packages;
	}

	public void setPackages(String packages)
	{
		this.packages = packages;
	}

	public Double getDiscAmt()
	{
		return discAmt;
	}

	public Boolean getDiscType()
	{
		return discType;
	}

	public void setDiscType(Boolean discType)
	{
		this.discType = discType;
	}

	public void setDiscAmt(Double discAmt)
	{
		this.discAmt = discAmt;
	}

	public Double getSubTotal()
	{
		return subTotal;
	}

	public void setSubTotal(Double subTotal)
	{
		this.subTotal = subTotal;
	}

	public Double getOtherAmount()
	{
		return otherAmount;
	}

	public void setOtherAmount(Double otherAmount)
	{
		this.otherAmount = otherAmount;
	}

	public Integer getFinancialYear()
	{
		return financialYear;
	}

	public void setFinancialYear(Integer financialYear)
	{
		this.financialYear = financialYear;
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
