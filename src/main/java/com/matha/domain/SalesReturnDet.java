package com.matha.domain;

import javax.persistence.*;
import java.util.Objects;

/**
 * @author Norman
 *
 */
@Entity
@Table(name = "SReturnDet")
public class SalesReturnDet implements InventoryData
{
	@Id
	@Column(name = "SReturnDetId")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer purReturnDetId;

	@ManyToOne
	@JoinColumn(name = "SerialId")
	private SchoolReturn schoolReturn;

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

	public SalesReturnDet()
	{
	}

	public SalesReturnDet(Integer salesDetId, SchoolReturn schoolReturn, Integer slNum, OrderItem orderItem)
	{
		this.purReturnDetId = salesDetId;
		this.schoolReturn = schoolReturn;
		this.slNum = slNum;
		this.qty = orderItem.getCount();
		this.rate = orderItem.getBookPrice();
		this.book = orderItem.getBook();
		this.bookName = orderItem.getBookName();
	}

	public SalesReturnDet(Integer slNum, Integer qty, Double rate, Book book)
	{
		this.slNum = slNum;
		this.qty = qty;
		this.rate = rate;
		this.book = book;
		this.bookName = book.getName();
	}

	public double getTotalBought()
	{
		return getBookPrice() * this.qty;
	}

	public Double getBookPrice()
	{
		return rate;
	}

	public Integer getPurReturnDetId() {
		return purReturnDetId;
	}

	public void setPurReturnDetId(Integer purReturnDetId) {
		this.purReturnDetId = purReturnDetId;
	}

	public SchoolReturn getSchoolReturn() {
		return schoolReturn;
	}

	public void setSchoolReturn(SchoolReturn schoolReturn) {
		this.schoolReturn = schoolReturn;
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

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("SalesReturnDet{");
		sb.append("purReturnDetId=").append(purReturnDetId);
		sb.append(", schoolReturn=").append(schoolReturn);
		sb.append(", slNum=").append(slNum);
		sb.append(", qty=").append(qty);
		sb.append(", rate=").append(rate);
//		sb.append(", bookId=").append(bookId);
		sb.append(", bookName='").append(bookName).append('\'');
		sb.append('}');
		return sb.toString();
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		SalesReturnDet salesDet = (SalesReturnDet) o;
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
