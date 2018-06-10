package com.matha.domain;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;

import static com.matha.util.UtilConstants.DATE_CONV;
import static com.matha.util.UtilConstants.STATUS_MAP;
import static org.hibernate.annotations.CascadeType.DELETE;
import static org.hibernate.annotations.CascadeType.SAVE_UPDATE;

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

	@Column(name = "NetAmt")
	private Double deletedAmt;

	@OneToOne	
	@JoinColumn(name = "TxnId")
	private PurchaseTransaction salesTxn;

	@OneToMany(mappedBy = "purchase", fetch = FetchType.EAGER, orphanRemoval = true)
	@Cascade({DELETE, SAVE_UPDATE})
	private Set<PurchaseDet> purchaseItems;

	@ManyToOne
	@JoinColumn(name = "PublisherId")
	private Publisher publisher;

	@Column(name = "Status")
	private Integer statusInd;

	public String getPublisherName()
	{
		return publisher.getName();
	}

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

	public String getStatusStr()
	{
		return STATUS_MAP.get(getStatusInd());
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

	public Double getDeletedAmt() {
		return deletedAmt;
	}

	public void setDeletedAmt(Double deletedAmt) {
		this.deletedAmt = deletedAmt;
	}

	public Integer getStatusInd() {
		return statusInd;
	}

	public void setStatusInd(Integer statusInd) {
		this.statusInd = statusInd;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("Purchase{");
		sb.append("id='").append(id).append('\'');
		sb.append(", invoiceNo='").append(invoiceNo).append('\'');
		sb.append(", serialNo=").append(serialNo);
		sb.append(", despatchedTo='").append(despatchedTo).append('\'');
		sb.append(", docsThrough='").append(docsThrough).append('\'');
		sb.append(", despatchPer='").append(despatchPer).append('\'');
		sb.append(", grNum='").append(grNum).append('\'');
		sb.append(", packages=").append(packages);
		sb.append(", discAmt=").append(discAmt);
		sb.append(", discType=").append(discType);
		sb.append(", subTotal=").append(subTotal);
		sb.append(", financialYear=").append(financialYear);
		sb.append(", purchaseDate=").append(purchaseDate);
		sb.append(", salesTxn=").append(salesTxn == null ? "" : salesTxn.getId());
		sb.append(", publisher=").append(publisher == null ? "" : publisher.getName());
		sb.append('}');
		return sb.toString();
	}
}
