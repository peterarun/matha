package com.matha.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "SOrderDet")
public class OrderItem implements Serializable, Comparable<OrderItem>
{

	private static final long serialVersionUID = -2960499864752422068L;

	@Id
	@Column(name = "OrderDetId")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "SerialId")
	private Order order;

	@OneToOne
	@JoinColumn(name = "BookId")
//	private Integer bookId;
	private Book book;

	@Column(name="BookName")
	private String bookName;

	@Column(name = "Qty")
	private Integer count;

	@Column(name = "SlNo")
	private Integer serialNum;

	@Column(name = "BookPrice")
	private Double bookPrice;

	public double getTotal()
	{
		return getBookPrice() * count;
	}

	public String getOrderId()
	{
		if(order != null)
		{
			return order.getId();
		}
		else
		{
			return null;
		}
	}

	public String getSchoolName()
	{
		return this.order.getSchool().getName();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}
//
//	public Integer getBookId() {
//		return bookId;
//	}
//
//	public void setBookId(Integer bookId) {
//		this.bookId = bookId;
//	}


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

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Integer getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(Integer serialNum) {
		this.serialNum = serialNum;
	}

	public Double getBookPrice() {
		return bookPrice;
	}

	public void setBookPrice(Double bookPrice) {
		this.bookPrice = bookPrice;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("OrderItem{");
		sb.append("id=").append(id);
//		sb.append(", orderId=").append(getOrderId());
//		sb.append(", bookId=").append(bookId);
		sb.append(", bookName='").append(bookName).append('\'');
		sb.append(", count=").append(count);
		sb.append(", serialNum=").append(serialNum);
		sb.append(", bookPrice=").append(bookPrice);
		sb.append('}');
		return sb.toString();
	}

	@Override
	public int compareTo(OrderItem o)
	{
		if (o != null && o.getId() != null && this.getId() != null && this.getId().equals(o.getId()))
		{
			return 0;
		}
		else
		{
			return -1;
		}

	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrderItem other = (OrderItem) obj;
		if (id == null)
		{
			if (other.id != null)
				return false;
		}
		else if (!id.equals(other.id))
			return false;
		return true;
	}


}
