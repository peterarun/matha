package com.matha.domain;

import javax.persistence.*;
import java.util.Objects;

/**
 * @author Norman
 *
 */
@Entity
@Table(name = "PurDet")
public class PurchaseDet implements InventoryData
{
	@Id
	@Column(name = "PurDetId")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer purDetId;

	@ManyToOne
	@JoinColumn(name = "SerialId")
	private Purchase purchase;

	@Column(name = "SlNo")
	private Integer slNum;

	@Column(name = "Qty")
	private Integer qty;

	@Column(name = "Rate")
	private Double rate;

	@OneToOne
	@JoinColumn(name = "BkNo")
	private Book book;

	@OneToOne
	@JoinColumn(name = "OrderItemId")
	private OrderItem orderItem;

	public PurchaseDet()
	{
	}

	public PurchaseDet(Integer salesDetId, Purchase purchase, Integer slNum, OrderItem orderItem)
	{
		this.purDetId = salesDetId;
		this.purchase = purchase;
		this.slNum = slNum;
		this.qty = orderItem.getCount();
		this.rate = orderItem.getBookPrice();
		this.book = orderItem.getBook();
		this.orderItem = orderItem;
	}

	public PurchaseDet(Integer slNum, Integer qty, Double rate, Book book)
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

	public Double getBookPrice()
	{
		if (rate == null)
		{
			return book.getPrice();
		}
		return rate;
	}

	public String getBookName()
	{
		return book.getName();
	}

	public Integer getPurDetId() {
		return purDetId;
	}

	public void setPurDetId(Integer purDetId) {
		this.purDetId = purDetId;
	}

	public Purchase getPurchase() {
		return purchase;
	}

	public void setPurchase(Purchase purchase) {
		this.purchase = purchase;
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

	public OrderItem getOrderItem() {
		return orderItem;
	}

	public void setOrderItem(OrderItem orderItem) {
		this.orderItem = orderItem;
	}

	@Override
	public String toString()
	{
		final StringBuilder sb = new StringBuilder("SalesDet{");
		sb.append("purDetId='").append(purDetId).append('\'');
		sb.append(", purchase=").append(purchase);
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
		PurchaseDet salesDet = (PurchaseDet) o;
		return Objects.equals(purDetId, salesDet.purDetId);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(purDetId);
	}

	@Override
	public Integer getQuantity()
	{
		return this.qty;
	}
}
