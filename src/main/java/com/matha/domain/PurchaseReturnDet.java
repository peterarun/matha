package com.matha.domain;

import javax.persistence.*;
import java.util.Objects;

/**
 * @author Norman
 *
 */
@Entity
@Table(name = "PReturnDet")
public class PurchaseReturnDet implements InventoryData
{
	@Id
	@Column(name = "PReturnDetId")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer purReturnDetId;

	@ManyToOne
	@JoinColumn(name = "SerialId")
	private PurchaseReturn purchaseReturn;

	@Column(name = "SlNo")
	private Integer slNum;

	@Column(name = "Qty")
	private Integer qty;

	@Column(name = "Rate")
	private Double rate;

	@OneToOne
	@JoinColumn(name = "BkNo")
	private Book book;

	public PurchaseReturnDet()
	{
	}

	public PurchaseReturnDet(Integer salesDetId, PurchaseReturn purchaseReturn, Integer slNum, OrderItem orderItem)
	{
		this.purReturnDetId = salesDetId;
		this.purchaseReturn = purchaseReturn;
		this.slNum = slNum;
		this.qty = orderItem.getCount();
		this.rate = orderItem.getBookPrice();
		this.book = orderItem.getBook();
	}

	public PurchaseReturnDet(Integer slNum, Integer qty, Double rate, Book book)
	{
		this.slNum = slNum;
		this.qty = qty;
		this.rate = rate;
		this.book = book;
	}

	public double getTotalBought()
	{
		return getBookPrice() * this.qty;
	}

	public String getBookName()
	{
		return book.getName();
	}

	public Double getBookPrice()
	{
		if (rate == null)
		{
			return book.getPrice();
		}
		return rate;
	}

	public Integer getPurReturnDetId() {
		return purReturnDetId;
	}

	public void setPurReturnDetId(Integer purReturnDetId) {
		this.purReturnDetId = purReturnDetId;
	}

	public PurchaseReturn getPurchaseReturn() {
		return purchaseReturn;
	}

	public void setPurchaseReturn(PurchaseReturn purchaseReturn) {
		this.purchaseReturn = purchaseReturn;
	}

	public Integer getSlNum() {
		return slNum;
	}

	public void setSlNum(Integer slNum) {
		this.slNum = slNum;
	}

	public Integer getQty() {
		return qty;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	@Override
	public String toString()
	{
		final StringBuilder sb = new StringBuilder("SalesDet{");
		sb.append("purReturnDetId='").append(purReturnDetId).append('\'');
		sb.append(", purchaseReturn=").append(purchaseReturn);
		sb.append(", slNum='").append(slNum).append('\'');
		sb.append(", qty='").append(qty).append('\'');
		sb.append(", rate=").append(rate);
		sb.append(", book=").append(book);
		sb.append('}');
		return sb.toString();
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		PurchaseReturnDet salesDet = (PurchaseReturnDet) o;
		return Objects.equals(purReturnDetId, salesDet.purReturnDetId);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(purReturnDetId);
	}

	@Override
	public Integer getQuantity()
	{
		return this.qty;
	}
}
