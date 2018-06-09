package com.matha.domain;

import java.util.Objects;

import javax.persistence.*;

/**
 * @author Norman
 *
 */
@Entity
@Table(name = "SalesDet")
public class SalesDet implements InventoryData
{
	@Id
	@Column(name = "SalesDetId")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer salesDetId;

	@ManyToOne
	@JoinColumn(name = "SerialId")
	private Sales sale;

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

	public SalesDet()
	{

	}

	public SalesDet(Integer salesDetId, Sales sale, Integer slNum, OrderItem orderItem)
	{
		this.salesDetId = salesDetId;
		this.sale = sale;
		this.slNum = slNum;
		this.qty = orderItem.getCount();
		this.rate = orderItem.getBookPrice();
		this.book = orderItem.getBook();
		this.orderItem = orderItem;
	}

	public SalesDet(Integer slNum, Integer qty, Double rate, Book book)
	{
		this.slNum = slNum;
		this.qty = qty;
		this.rate = rate;
		this.book = book;
	}

	public double getTotalSold()
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

	public Integer getSalesDetId() {
		return salesDetId;
	}

	public void setSalesDetId(Integer salesDetId) {
		this.salesDetId = salesDetId;
	}

	public Sales getSale() {
		return sale;
	}

	public void setSale(Sales sale) {
		this.sale = sale;
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
		sb.append("salesDetId='").append(salesDetId).append('\'');
		sb.append(", sale=").append(sale == null ? "" : sale.getId());
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
		SalesDet salesDet = (SalesDet) o;
		Book thatBook = salesDet.getBook();
		return Objects.equals(salesDetId, salesDet.salesDetId);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(salesDetId);
	}

	@Override
	public Integer getQuantity()
	{
		return this.qty;
	}
}
