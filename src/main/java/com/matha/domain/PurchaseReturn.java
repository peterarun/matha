package com.matha.domain;

import static com.matha.util.UtilConstants.DATE_CONV;

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

}
