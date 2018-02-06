package com.matha.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "Book")
public class Book implements Comparable<Book>
{

	// @Id
	// @Column(name = "SerialId")
	// @GenericGenerator(name = "kaugen", strategy = "increment")
	// @GeneratedValue(generator = "kaugen")
	// private Integer id;

	@Column(name = "Bname")
	private String name;

	@Id
	@GenericGenerator(name = "kaugen", strategy = "guid")
	@GeneratedValue(generator = "kaugen")
	@Column(name = "BookNo")
	private String bookNum;

	@Column(name = "Short")
	private String shortName;

	@Column(name = "Price")
	private Double price;

	@Column(name = "Inventory")
	private Integer inventory;

	@ManyToOne
	@JoinColumn(name = "Catid")
	private BookCategory category;

	@ManyToOne
	@JoinColumn(name = "Pubid")
	private Publisher publisher;

	public String getPublisherName()
	{
		return publisher.getName();
	}

	public String getCategoryName()
	{
		return category.getName();
	}

	// public Integer getId()
	// {
	// return id;
	// }
	//
	// public void setId(Integer id)
	// {
	// this.id = id;
	// }

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

	public String getShortName()
	{
		return shortName;
	}

	public void setShortName(String shortName)
	{
		this.shortName = shortName;
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

	public BookCategory getCategory()
	{
		return category;
	}

	public void setCategory(BookCategory category)
	{
		this.category = category;
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
		// builder.append(id);
		builder.append("name=");
		builder.append(name);
		builder.append(", bookNum=");
		builder.append(bookNum);
		builder.append(", shortName=");
		builder.append(shortName);
		builder.append(", category=");
		builder.append(category);
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
