package com.matha.domain;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

public class Book {

	@Id
	private String id;
	private String name;
	private String bookNum;
	private String shortName;
	private Integer inventoryCount;
	private Date insertTime;
	private Date updateTime;

	@DBRef
	private BookCategory category;

	@DBRef
	private Publisher publisher;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBookNum() {
		return bookNum;
	}

	public void setBookNum(String bookNum) {
		this.bookNum = bookNum;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public Integer getInventoryCount() {
		return inventoryCount;
	}

	public void setInventoryCount(Integer inventoryCount) {
		this.inventoryCount = inventoryCount;
	}

	public Date getInsertTime() {
		return insertTime;
	}

	public void setInsertTime(Date insertTime) {
		this.insertTime = insertTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public BookCategory getCategory() {
		return category;
	}

	public void setCategory(BookCategory category) {
		this.category = category;
	}

	public Publisher getPublisher() {
		return publisher;
	}

	public void setPublisher(Publisher publisher) {
		this.publisher = publisher;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Book [id=");
		builder.append(id);
		builder.append(", name=");
		builder.append(name);
		builder.append(", bookNum=");
		builder.append(bookNum);
		builder.append(", shortName=");
		builder.append(shortName);
		builder.append(", inventoryCount=");
		builder.append(inventoryCount);
		builder.append(", insertTime=");
		builder.append(insertTime);
		builder.append(", updateTime=");
		builder.append(updateTime);
		builder.append(", category=");
		builder.append(category);
		builder.append(", publisher=");
		builder.append(publisher);
		builder.append("]");
		return builder.toString();
	}

}
