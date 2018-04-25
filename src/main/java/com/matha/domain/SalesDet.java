package com.matha.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Norman
 *
 */
@Entity
@Table(name = "SalesDet")
public class SalesDet
{

	@EmbeddedId
	private SaleDetPK detId;

	@Column(name = "BkName")
	private String bookName;

	@Column(name = "Qty")
	private String qty;

	@Column(name = "Rate")
	private Double rate;
	
	public String getBookNum()
	{
		return this.detId.getBookNum();
	}
	
	public SaleDetPK getDetId()
	{
		return detId;
	}

	public void setDetId(SaleDetPK detId)
	{
		this.detId = detId;
	}

	public String getBookName()
	{
		return bookName;
	}

	public void setBookName(String bookName)
	{
		this.bookName = bookName;
	}

	public String getQty()
	{
		return qty;
	}

	public void setQty(String qty)
	{
		this.qty = qty;
	}

	public Double getRate()
	{
		return rate;
	}

	public void setRate(Double rate)
	{
		this.rate = rate;
	}
	
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("SalesDet [PK=");
		builder.append(detId.toString());
		builder.append(", bookName=");
		builder.append(bookName);
		builder.append(", qty=");
		builder.append(qty);
		builder.append(", rate=");
		builder.append(rate);
		builder.append("]");
		return builder.toString();
	}

	public String toStringMig()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("SalesDet [PK=");
		builder.append(detId.toString());
		builder.append("]");
		return builder.toString();
	}

}

@Embeddable
class SaleDetPK implements Serializable 
{
	private static final long serialVersionUID = 1224472033071467065L;
	
	@Column(name = "SerialId")
	protected String serialId;
	
	@Column(name = "BkNo")
    protected String bookNum;

    public SaleDetPK() {}

    public SaleDetPK(String serialId, String bookNum) 
    {
        this.serialId = serialId;
        this.bookNum = bookNum;
    }
    // equals, hashCode
    
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("SaleDetPK [serialId=");
		builder.append(serialId);
		builder.append(", bookNum=");
		builder.append(bookNum);
		builder.append("]");
		return builder.toString();
	}

	public String getSerialId()
	{
		return serialId;
	}

	public void setSerialId(String serialId)
	{
		this.serialId = serialId;
	}

	public String getBookNum()
	{
		return bookNum;
	}

	public void setBookNum(String bookNum)
	{
		this.bookNum = bookNum;
	}
}