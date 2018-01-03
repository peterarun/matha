package com.matha.vo;

import com.matha.domain.Book;

public class BookItem {

	private Book book;
	private int count;

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	public String getName()
	{
		return this.book.getName();
	}

	public String getPublisher()
	{
		return this.book.getPublisher().getName();
	}
}
