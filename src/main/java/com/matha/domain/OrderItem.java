package com.matha.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;

import static org.hibernate.annotations.CascadeType.DELETE;
import static org.hibernate.annotations.CascadeType.SAVE_UPDATE;

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

	@OneToMany(fetch= FetchType.EAGER, mappedBy = "orderItem")
	@Cascade({DELETE})
	private Set<PurchaseDet> purchaseDet;

	@OneToMany(fetch= FetchType.EAGER, mappedBy = "orderItem")
	@Cascade({DELETE})
	private Set<SalesDet> salesDet;
	
	@ManyToOne()
	@JoinColumn(name = "BkNo")
	private Book book;

	@Column(name = "Qty")
	private Integer count;

//	@Column(name = "fullfilledCnt")
//	private Integer fullFilledCnt;

//	@Column(name = "soldCnt")
//	private Integer soldCnt;

	@Column(name = "SlNo")
	private Integer serialNum;

	@Column(name = "BookPrice")
	private Double bookPrice;

//	@ManyToOne()
//	@JoinColumn(name = "ReturnId")
//	private SchoolReturn bookReturn;
//
//	@ManyToOne()
//	@JoinColumn(name = "PurReturnId")
//	private PurchaseReturn purchReturn;

	public double getTotal()
	{
		return getBookPrice() * count;
	}

//	public double getTotalBought()
//	{
//		return getBookPrice() * fullFilledCnt;
//	}
//
//	public double getTotalSold()
//	{
//		return getBookPrice() * soldCnt;
//	}
//
	public Double getBookPrice()
	{
		if (bookPrice == null)
		{
			return book.getPrice();
		}
		return bookPrice;
	}

	public void setBookPrice(Double bookPrice)
	{
		this.bookPrice = bookPrice;
	}

//	public SchoolReturn getBookReturn()
//	{
//		return bookReturn;
//	}
//
//	public void setBookReturn(SchoolReturn bookReturn)
//	{
//		this.bookReturn = bookReturn;
//	}
//
//	public PurchaseReturn getPurchReturn()
//	{
//		return purchReturn;
//	}
//
//	public void setPurchReturn(PurchaseReturn purchReturn)
//	{
//		this.purchReturn = purchReturn;
//	}

	public String getBookName()
	{
		return book.getName();
	}

	public String getPublisherName()
	{
		return book.getPublisher().getName();
	}

	public String getSchoolName()
	{
		return this.order.getSchool().getName();
	}

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public Order getOrder()
	{
		return order;
	}

	public void setOrder(Order order)
	{
		this.order = order;
	}

//	public Purchase getSchoolReturn()
//	{
//		return purchase;
//	}
//
//	public void setSchoolReturn(Purchase purchase)
//	{
//		this.purchase = purchase;
//	}
//
//	public Sales getSchoolReturn()
//	{
//		return sale;
//	}
//
//	public void setSchoolReturn(Sales sale)
//	{
//		this.sale = sale;
//	}

	public Book getBook()
	{
		return book;
	}

	public void setBook(Book book)
	{
		this.book = book;
	}

	public Integer getCount()
	{
		return count;
	}

	public void setCount(Integer count)
	{
		this.count = count;
	}
	
//	public Integer getFullFilledCnt()
//	{
//		return fullFilledCnt;
//	}
//
//	public void setFullFilledCnt(Integer fullFilledCnt)
//	{
//		this.fullFilledCnt = fullFilledCnt;
//	}
//
//	public Integer getSoldCnt()
//	{
//		return soldCnt;
//	}
//
//	public void setSoldCnt(Integer soldCnt)
//	{
//		this.soldCnt = soldCnt;
//	}

	public Integer getSerialNum()
	{
		return serialNum;
	}

	public void setSerialNum(Integer serialNum)
	{
		this.serialNum = serialNum;
	}

	public String getOrderId()
	{
		return this.order.getId();
	}

	public Set<SalesDet> getSalesDet() {
		return salesDet;
	}

	public void setSalesDet(Set<SalesDet> salesDet) {
		this.salesDet = salesDet;
	}

	public Set<PurchaseDet> getPurchaseDet() {
		return purchaseDet;
	}

	public void setPurchaseDet(Set<PurchaseDet> purchaseDet) {
		this.purchaseDet = purchaseDet;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("OrderItem [id=");
		builder.append(id);
		builder.append(", serialId=");
		builder.append(order);
		builder.append(", book=");
		builder.append(book);
		// builder.append(", bookId=");
		// builder.append(bookId);
		builder.append(", count=");
		builder.append(count);
		builder.append(", serialNum=");
		builder.append(serialNum);
		builder.append("]");
		return builder.toString();
	}

	public String toStringMig()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("OrderItem [id=");
		builder.append(id);
		builder.append(", orderId=");
		builder.append(order.getId());
		builder.append(", bookNum=");
		builder.append(book.getBookNum());
		builder.append("]");
		return builder.toString();
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
