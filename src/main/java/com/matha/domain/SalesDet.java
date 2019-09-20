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
	@JoinColumn(name = "BookId")
//	private Integer bookId;
	private Book book;

	@Column(name="BookName")
	private String bookName;

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
		this.bookName = orderItem.getBookName();
		this.orderItem = orderItem;
	}

	public SalesDet(Integer slNum, Integer qty, Double rate, Book book)
	{
		this.slNum = slNum;
		this.qty = qty;
		this.rate = rate;
		this.book = book;
		this.bookName = book.getName();
	}

	public double getTotalSold()
	{
		return getBookPrice() * this.qty;
	}

	public Double getBookPrice()
	{
		return rate == null ? (book == null ? 0 : (book.getPrice() == null ? 0 : book.getPrice())) : rate;
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
//
//	@Override
//	public Integer getBookId() {
//		return bookId;
//	}
//
//	public void setBookId(Integer bookId) {
//		this.bookId = bookId;
//	}


	@Override
	public Book getBook() {
		return book;
	}

	@Override
	public boolean isUnFilled()
	{
		return this.qty == null || this.qty == 0 || this.rate == null || this.rate == 0;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	public OrderItem getOrderItem() {
		return orderItem;
	}

	public void setOrderItem(OrderItem orderItem) {
		this.orderItem = orderItem;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("SalesDet{");
		sb.append("salesDetId=").append(salesDetId);
		sb.append(", sale=").append(sale);
		sb.append(", slNum=").append(slNum);
		sb.append(", qty=").append(qty);
		sb.append(", rate=").append(rate);
//		sb.append(", bookId=").append(bookId);
		sb.append(", bookName='").append(bookName).append('\'');
		sb.append(", orderItem=").append(orderItem);
		sb.append('}');
		return sb.toString();
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		SalesDet salesDet = (SalesDet) o;
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
