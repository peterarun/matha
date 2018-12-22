package com.matha.domain;

import javax.persistence.*;

@Entity
@Table(name = "Book")
public class Book implements Comparable<Book>
{

	@Id
	@Column(name = "BookId")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "Bname")
	private String name;

	@Column(name = "BookNo")
	private String bookNum;

	@Column(name = "Price")
	private Double price;

	@Column(name = "Inventory")
	private Integer inventory;

	@ManyToOne
	@JoinColumn(name = "Pubid")
	private Publisher publisher;

	public String getPublisherName()
	{
		if(publisher != null)
		{
			return publisher.getName();
		}
		else
		{
			return null;
		}
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getBookNum()
	{
		return bookNum;
	}

	public void setBookNum(String bookNum)
	{
		this.bookNum = bookNum;
	}

	public Double getPrice()
	{
		return price;
	}

	public void setPrice(Double price)
	{
		this.price = price;
	}

	public Integer getInventory()
	{
		return inventory;
	}

	public void setInventory(Integer inventory)
	{
		this.inventory = inventory;
	}

	public Publisher getPublisher()
	{
		return publisher;
	}

	public void setPublisher(Publisher publisher)
	{
		this.publisher = publisher;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("Book [");
		builder.append(id);
		builder.append("name=");
		builder.append(name);
		builder.append(", bookNum=");
		builder.append(bookNum);
		builder.append(", publisher=");
		builder.append(publisher);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int compareTo(Book o)
	{
		if(o != null)
		{
			return o.getBookNum().compareTo(bookNum);
		}
		return -1;
	}

	public void addInventory(int count)
	{
		if(this.inventory == null)
		{
			this.inventory = 0;	
		}
		this.inventory += count;		
	}

	public void clearInventory(int count)
	{
		if(this.inventory == null)
		{
			this.inventory = 0;	
		}
		this.inventory -= count;		
	}

}
