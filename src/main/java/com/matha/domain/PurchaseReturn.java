package com.matha.domain;

import static com.matha.util.UtilConstants.DATE_CONV;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.*;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "PReturn")
public class PurchaseReturn {

	@Id
	@GenericGenerator(name = "pReturnId", strategy = "com.matha.generator.PurchaseRetIdGenerator")
	@GeneratedValue(generator = "pReturnId")
	@Column(name = "SerialId")
	private String id;

	@OneToOne
	@JoinColumn(name = "TxnId")
	private PurchaseTransaction salesTxn;

	@Column(name = "CreditNoteNum")
	private String creditNoteNum;

	@Column(name = "Discount")
	private Double discAmt;

	@Column(name = "discPerc")
	private Double discPerc;

	@Column(name = "SubTotal")
	private Double subTotal;

	@Column(name = "TDate")
	private LocalDate returnDate;

	@ManyToOne
	@JoinColumn(name = "PublisherId")
	private Publisher publisher;

	@OneToMany(fetch= FetchType.EAGER, mappedBy = "purchaseReturn")
	@Cascade({CascadeType.SAVE_UPDATE, CascadeType.DELETE})	
	private Set<PurchaseReturnDet> purchaseReturnDetSet;
	
	public Integer getUnitCount()
	{
		int unitCount = 0;
		if(getPurchaseReturnDetSet() != null)
		{
			unitCount = getPurchaseReturnDetSet().stream().collect(Collectors.summingInt(PurchaseReturnDet::getQty));
		}
		
		return unitCount;
	}

	public Double getDiscount()
	{
		if(discAmt != null)
		{
			return discAmt;
		}
		else if (discPerc != null && subTotal != null)
		{
			return subTotal * discPerc / 100;
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

	public String getNotes()
	{
		return salesTxn.getNote();
	}

	public LocalDate getTxnDate()
	{
		return salesTxn.getTxnDate();
	}
	
	public String getTxnDateStr()
	{		
		return DATE_CONV.toString(getTxnDate());
	}

	public Double getNetAmount()
	{
		return salesTxn.getAmount();
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public PurchaseTransaction getSalesTxn() {
		return salesTxn;
	}

	public void setSalesTxn(PurchaseTransaction salesTxn) {
		this.salesTxn = salesTxn;
	}

	public Set<PurchaseReturnDet> getPurchaseReturnDetSet() {
		return purchaseReturnDetSet;
	}

	public void setPurchaseReturnDetSet(Set<PurchaseReturnDet> purchaseReturnDetSet) {
		this.purchaseReturnDetSet = purchaseReturnDetSet;
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

	public Double getDiscPerc() {
		return discPerc;
	}

	public void setDiscPerc(Double discPerc) {
		this.discPerc = discPerc;
	}

	public Double getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(Double subTotal) {
		this.subTotal = subTotal;
	}

	public String getCreditNoteNum() {
		return creditNoteNum;
	}

	public void setCreditNoteNum(String creditNoteNum) {
		this.creditNoteNum = creditNoteNum;
	}

	public LocalDate getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(LocalDate returnDate) {
		this.returnDate = returnDate;
	}

	public Publisher getPublisher() {
		return publisher;
	}

	public void setPublisher(Publisher publisher) {
		this.publisher = publisher;
	}
}
