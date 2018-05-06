package com.matha.domain;

import org.hibernate.annotations.GenericGenerator;

import static com.matha.util.UtilConstants.DATE_CONV;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.*;

@Entity
@Table(name = "Purchase")
public class Purchase implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3847049966939941709L;

	@Id
	@Column(name = "SerialId")
	@GenericGenerator(name = "serialId", strategy = "com.matha.generator.PurchaseIdGenerator")
	@GeneratedValue(generator = "serialId")
	private String id;

	@Column(name = "InvNo")
	private String invoiceNo;

	@Column(name = "serialNo")
	private Integer serialNo;

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

	@Column(name = "Fy")
	private Integer financialYear;

	@Column(name = "TDate")
	private LocalDate purchaseDate;

	@OneToOne	
	@JoinColumn(name = "TxnId")
	private PurchaseTransaction salesTxn;

	@OneToMany(mappedBy = "purchase", fetch = FetchType.EAGER)
	private Set<PurchaseDet> purchaseItems;

	@ManyToOne
	@JoinColumn(name = "PublisherId")
	private Publisher publisher;

	public LocalDate getTxnDate()
	{
		if(salesTxn != null)
		{
			return salesTxn.getTxnDate();
		}
		return this.getPurchaseDate();
	}

	public String getTxnDateStr()
	{		
		return DATE_CONV.toString(getTxnDate());
	}
	
	public Integer getUnitCount()
	{
		int unitCount = 0;
		if(getPurchaseItems() != null)
		{
			unitCount = getPurchaseItems().stream().collect(Collectors.summingInt(PurchaseDet::getQty));
		}
		
		return unitCount;
	}
	
	public Double getCalculatedDisc()
	{
		Double discCalc = discAmt;
		if(discAmt != null)
		{			
			if(discType != null && discType && subTotal != null)
			{
				discCalc = subTotal * discAmt / 100;
			}			
		}
		return discCalc;
	}
	
	public Double getNetAmount()
	{
		if(salesTxn == null)
		{
			return getSubTotal() - getCalculatedDisc();
		}
		return salesTxn.getAmount();
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

	public Integer getFinancialYear() {
		return financialYear;
	}

	public void setFinancialYear(Integer financialYear) {
		this.financialYear = financialYear;
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

	public Set<PurchaseDet> getPurchaseItems()
	{
		return purchaseItems;
	}

	public void setPurchaseItems(Set<PurchaseDet> order)
	{
		this.purchaseItems = order;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public Integer getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(Integer serialNo) {
		this.serialNo = serialNo;
	}

	public Publisher getPublisher() {
		return publisher;
	}

	public void setPublisher(Publisher publisher) {
		this.publisher = publisher;
	}
}
