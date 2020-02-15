package com.matha.domain;

import static com.matha.util.Converters.convertTimestamp;
import static com.matha.util.UtilConstants.DATE_CONV;
import static com.matha.util.UtilConstants.EMPTY_STR;
import static com.matha.util.UtilConstants.STATUS_MAP;
import static org.hibernate.annotations.CascadeType.DELETE;
import static org.hibernate.annotations.CascadeType.SAVE_UPDATE;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

import javax.persistence.*;

import com.matha.util.UtilConstants;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;

/**
 * @author Norman
 *
 */
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
	private Integer serialNo;

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

//	@OneToMany(mappedBy = "sale", fetch = FetchType.EAGER)
//	private Set<OrderItem> orderItems;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "sale", orphanRemoval = true)
	@Cascade({DELETE, SAVE_UPDATE})
	private Set<SalesDet> saleItems;

	@ManyToOne
	@JoinColumn(name = "CustId")
	private School school;

	@Column(name = "TDate")
	private Timestamp txnDate;

	@Column(name = "Status")
	private Integer statusInd;

	public String getSchoolName()
	{
		return school.getName();
	}

	public LocalDate getInvoiceDate()
	{
		if(getTxnDate() != null)
		{
			return convertTimestamp(getTxnDate());
		}
		else if(salesTxn != null)
		{
			return convertTimestamp(salesTxn.getTxnDate());
		}
		else
		{
			return null;
		}
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
		if (discType != null && discType)
		{
			return subTotal * discAmt / 100;
		}
		return discAmt;
	}
	
	public Double getNetAmount()
	{
		if(salesTxn == null)
		{
			return getSubTotal() - getCalcDisc();
		}
		return salesTxn.getAmount();
	}

	public String getStatusStr()
	{
		if(getStatusInd() != null && STATUS_MAP.containsKey(getStatusInd()))
		{
			return STATUS_MAP.get(getStatusInd());
		}
		else
		{
			return EMPTY_STR;
		}
	}

	public SalesTransaction getSalesTxn()
	{
		return salesTxn;
	}

	public void setSalesTxn(SalesTransaction salesTxn)
	{
		this.salesTxn = salesTxn;
	}

	public Set<SalesDet> getSaleItems()
	{
		return saleItems;
	}

	public void setSaleItems(Set<SalesDet> saleItems)
	{
		this.saleItems = saleItems;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public Integer getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(Integer serialNo) {
		this.serialNo = serialNo;
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

	public School getSchool()
	{
		return school;
	}

	public void setSchool(School school)
	{
		this.school = school;
	}

	public Timestamp getTxnDate() {
		return txnDate;
	}

	public void setTxnDate(Timestamp txnDate) {
		this.txnDate = txnDate;
	}

	public Integer getStatusInd() {
		return statusInd;
	}

	public void setStatusInd(Integer statusInd) {
		this.statusInd = statusInd;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("Sales{");
		sb.append("id='").append(id).append('\'');
		sb.append(", despatch='").append(despatch).append('\'');
		sb.append(", docsThru='").append(docsThru).append('\'');
		sb.append(", grNum='").append(grNum).append('\'');
		sb.append(", serialNo=").append(serialNo);
		sb.append(", packages='").append(packages).append('\'');
		sb.append(", discAmt=").append(discAmt);
		sb.append(", discType=").append(discType);
		sb.append(", subTotal=").append(subTotal);
		sb.append(", otherAmount=").append(otherAmount);
		sb.append(", financialYear=").append(financialYear);
		sb.append(", deletedAmt=").append(deletedAmt);
		sb.append(", salesTxn=").append(salesTxn);
//		sb.append(", saleItems=").append(saleItems);
		sb.append(", school=").append(school);
		sb.append(", txnDate=").append(txnDate);
		sb.append('}');
		return sb.toString();
	}
}
