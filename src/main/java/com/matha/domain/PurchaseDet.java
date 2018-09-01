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
	@JoinColumn(name = "BookId")
//	private Integer bookId;
	private Book book;

	@Column(name="BookName")
	private String bookName;

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
//		this.bookId = orderItem.getBookId();
		this.book = orderItem.getBook();
		this.bookName = orderItem.getBookName();
		this.orderItem = orderItem;
	}

	public PurchaseDet(Integer slNum, Integer qty, Double rate, Book book)
	{
		this.slNum = slNum;
		this.qty = qty;
		this.rate = rate;
//		this.bookId = bookId;
		this.book = book;
		this.bookName = book.getName();
	}

	public double getTotalBought()
	{
		return getBookPrice() * this.qty;
	}

	public Double getBookPrice()
	{
		return rate == null ? (book == null ? 0 : (book.getPrice() == null ? 0 : book.getPrice())) : rate;
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
		final StringBuilder sb = new StringBuilder("PurchaseDet{");
		sb.append("purDetId=").append(purDetId);
		sb.append(", purchase=").append(purchase);
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
		PurchaseDet salesDet = (PurchaseDet) o;
		String thatOi = salesDet.getBookName();
		return Objects.equals(purDetId, salesDet.purDetId) && Objects.equals(bookName, thatOi);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(purDetId, bookName);
	}

	@Override
	public Integer getQuantity()
	{
		return this.qty;
	}
}
